package org.datapunch.starfish.core;

import org.datapunch.starfish.util.AwsUtil;

public class EmrClusterFqid {
    private String region;
    private String clusterId;

    public EmrClusterFqid(String id) {
        this.region = AwsUtil.getRegionFromPrefix(id);
        id = id.substring(region.length());
        if (id.startsWith("-")) {
            id = id.substring(1);
        }
        this.clusterId = id;
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
}
