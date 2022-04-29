package org.datapunch.starfish.core;

import org.datapunch.starfish.util.AwsUtil;

import java.util.Objects;

public class EmrClusterFqid {
    private String region;
    private String clusterId;

    public EmrClusterFqid(String id) {
        String regionProbe = AwsUtil.tryGetRegionFromPrefix(id);
        if (regionProbe != null) {
            String remaining = id.substring(regionProbe.length());
            if (remaining.startsWith("-") || remaining.startsWith("_") || remaining.startsWith(".")) {
                remaining = remaining.substring(1);
            }
            this.region = regionProbe;
            this.clusterId = remaining;
        } else {
            throw new IllegalArgumentException(String.format("Could not get region and cluster id from %s", id));
        }
    }

    public EmrClusterFqid(String region, String clusterId) {
        this.region = region;
        this.clusterId = clusterId;
    }

    public String getRegion() {
        return region;
    }

    public String getClusterId() {
        return clusterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmrClusterFqid that = (EmrClusterFqid) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(clusterId, that.clusterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, clusterId);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", region, clusterId);
    }
}
