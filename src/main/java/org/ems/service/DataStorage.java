package org.ems.service;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ems.model.Coordinate;
import org.ems.model.hgt.HGT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by stas on 6/20/14.
 */
public class DataStorage {
    public static final String DATA_LOCATION_URL = "http://dds.cr.usgs.gov/srtm/version2_1/SRTM3/Eurasia/";

    public HGT get(Coordinate coordinate) throws IOException, ArchiveException {
        File file = downloadAndCache(coordinate);
        try (ArchiveInputStream input = new ArchiveStreamFactory()
                .createArchiveInputStream(Files.newInputStream(file.toPath()))) {
            return HGT.create(coordinate, input);
        }
    }

    private static File downloadAndCache(Coordinate coordinate) throws IOException {
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
                        System.err.println("Can not find file " + cellFileName + " in " + DATA_LOCATION_URL);
                        System.exit(1);
                    }
                    Files.copy(response1.getEntity().getContent(), heightsDataFile.toPath());

                    System.out.println("File " + cellFileName + " is downloaded to " + heightsDataDir);

                }
            }
        }
        System.out.println(heightsDataFile);
        return heightsDataFile;
    }
}
