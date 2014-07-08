package org.ems.service;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ems.model.GeoCoordinate;
import org.ems.model.hgt.HGT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Created by stas on 6/20/14.
 */
public class DataStorage {
    public static final String DATA_LOCATION_URL = "http://dds.cr.usgs.gov/srtm/version2_1/SRTM3/Eurasia/";

    public HGT get(GeoCoordinate coordinate) throws IOException, CompressorException {
        File file = downloadAndCache(coordinate);
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipArchiveEntry zipArchiveEntry = zipFile.getEntries().nextElement();
            try (InputStream is = zipFile.getInputStream(zipArchiveEntry)) {
                return HGT.create(coordinate, zipArchiveEntry.getSize(), is);
            }
        }
    }

    private static File downloadAndCache(GeoCoordinate coordinate) throws IOException {
        File heightsDataDir = new File(System.getProperty("user.home") + "/.heightsDataDir");
        if (!heightsDataDir.exists()) {
            heightsDataDir.mkdir();
        }
        String cellFileName = String.format("%s%2d%s%03d.hgt.zip",
                coordinate.getLatitude().getHemisphere().getShort(), coordinate.getLatitude().getDegrees(),
                coordinate.getLongitude().getHemisphere().getShort(), coordinate.getLongitude().getDegrees());
        File heightsDataFile = new File(heightsDataDir, cellFileName);
        if (!heightsDataFile.exists()) {
            HttpGet httpGet = new HttpGet(DATA_LOCATION_URL + cellFileName);
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                    if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                        throw new FileNotFoundException("Can not find file " + cellFileName + " in " + DATA_LOCATION_URL);
                    }
                    Files.copy(response1.getEntity().getContent(), heightsDataFile.toPath());

                    System.out.println("File " + cellFileName + " is downloaded to " + heightsDataDir);

                }
            }
        }
//        System.out.println(heightsDataFile);
        return heightsDataFile;
    }
}
