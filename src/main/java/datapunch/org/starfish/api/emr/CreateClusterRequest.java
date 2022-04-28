package datapunch.org.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClusterRequest {
    private String clusterName;
    private String emrRelease;
    private String subnetId;

    @JsonProperty
    public String getClusterName() {
        return clusterName;
    }

    @JsonProperty
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @JsonProperty
    public String getEmrRelease() {
        return emrRelease;
    }

    @JsonProperty
    public void setEmrRelease(String emrRelease) {
        this.emrRelease = emrRelease;
    }

    @JsonProperty
    public String getSubnetId() {
        return subnetId;
    }

    @JsonProperty
    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }
}
