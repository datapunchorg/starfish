package org.datapunch.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datapunch.starfish.api.spark.SparkApplicationStatus;

public class GetSparkApplicationResponse {
    private String clusterFqid;
    private String submissionId;

    private SparkApplicationStatus status;

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

    @JsonProperty
    public SparkApplicationStatus getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(SparkApplicationStatus status) {
        this.status = status;
    }
}
