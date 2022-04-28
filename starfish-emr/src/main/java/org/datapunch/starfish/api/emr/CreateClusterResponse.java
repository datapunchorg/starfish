package org.datapunch.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClusterResponse {
    private String clusterFqid;

    @JsonProperty
    public String getClusterFqid() {
        return clusterFqid;
    }

    @JsonProperty
    public void setClusterFqid(String clusterFqid) {
        this.clusterFqid = clusterFqid;
    }
}
