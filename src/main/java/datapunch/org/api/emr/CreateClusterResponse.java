package datapunch.org.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClusterResponse {
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
