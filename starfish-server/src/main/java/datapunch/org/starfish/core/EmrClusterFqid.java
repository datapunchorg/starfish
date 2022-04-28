package datapunch.org.starfish.core;

public class EmrClusterFqid {
    private String region;
    private String clusterId;

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
