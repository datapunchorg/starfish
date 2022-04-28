package datapunch.org.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClusterResponse {
    private String clusterId;
    private ClusterStatus status;

    @JsonProperty
    public String getClusterId() {
        return clusterId;
    }

    @JsonProperty
    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @JsonProperty
    public ClusterStatus getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(ClusterStatus status) {
        this.status = status;
    }
}
