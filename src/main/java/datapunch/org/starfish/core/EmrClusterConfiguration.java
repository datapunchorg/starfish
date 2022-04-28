package datapunch.org.starfish.core;

public class EmrClusterConfiguration {
    private String emrRelease;
    private String subnetId;

    public EmrClusterConfiguration() {
        this.emrRelease = "emr-5.20.0";
    }

    public String getEmrRelease() {
        return emrRelease;
    }

    public void setEmrRelease(String emrRelease) {
        this.emrRelease = emrRelease;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }
}
