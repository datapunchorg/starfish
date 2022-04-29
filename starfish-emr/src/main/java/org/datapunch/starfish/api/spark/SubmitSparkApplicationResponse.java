package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitSparkApplicationResponse {
    private String clusterFqid;
    private String submissionId;

    @JsonProperty
    public String getClusterFqid() {
        return clusterFqid;
    }

    @JsonProperty
    public void setClusterFqid(String clusterFqid) {
        this.clusterFqid = clusterFqid;
    }

    @JsonProperty
    public String getSubmissionId() {
        return submissionId;
    }

    @JsonProperty
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
}
