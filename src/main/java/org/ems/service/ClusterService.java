package org.ems.service;

import org.ems.clusterization.ClusterizationUtil;
import org.ems.model.Cluster;
import org.ems.model.MatrixCoordinate;
import org.ems.model.SlopeInfo;

import java.util.*;

/**
 * Created by stas on 10/16/14.
 */
public class ClusterService {
    public static final double CLUSTER_AREA = 4;

    public static final Comparator<Cluster> CLUSTER_SIZE_COMPARATOR = new Comparator<Cluster>() {
        @Override
        public int compare(Cluster o1, Cluster o2) {
            return o2.getSize() - o1.getSize();
        }
    };
    private Collection<SlopeInfo> data = new ArrayList<>();

    public void addDirectionPoints(Collection<SlopeInfo> scanResults) {
        data.addAll(scanResults);
    }

    public List<Cluster> buildClusters() {
        List<Cluster> clusters = new ArrayList<>();

        HashSet<MatrixCoordinate> coordinates = new HashSet<>();
        for (SlopeInfo slopeInfo : data) {
            coordinates.add(slopeInfo.getHighPoint());
        }

        //TODO: adjustable CLUSTER_AREA
        List<List<MatrixCoordinate>> pointClusters = ClusterizationUtil.clusterize(new ArrayList<>(coordinates), CLUSTER_AREA);

        for (List<MatrixCoordinate> clusterCoordinates : pointClusters) {
            Cluster cluster = new Cluster();
            clusters.add(cluster);
            for (Iterator<SlopeInfo> it = data.iterator(); it.hasNext() ; ) {
                SlopeInfo slopeInfo = it.next();
                if (clusterCoordinates.contains(slopeInfo.getHighPoint())) {
                    it.remove();
                    cluster.add(slopeInfo);
                }
            }
        }
        Collections.sort(clusters, CLUSTER_SIZE_COMPARATOR);
        return clusters;
    }
}
