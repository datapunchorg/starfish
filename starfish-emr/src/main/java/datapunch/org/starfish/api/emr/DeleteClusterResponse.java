package datapunch.org.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteClusterResponse {
    private String clusterId;

    @JsonProperty
    public String getClusterId() {
        return clusterId;
    }

    @JsonProperty
    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
