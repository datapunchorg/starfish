package org.datapunch.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClusterResponse {
    private String clusterFqid;
    private ClusterStatus status;

    @JsonProperty
    public String getClusterFqid() {
        return clusterFqid;
    }

    @JsonProperty
    public void setClusterFqid(String clusterFqid) {
        this.clusterFqid = clusterFqid;
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
