package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitSparkApplicationResponse {
    private String submissionId;

    @JsonProperty
    public String getSubmissionId() {
        return submissionId;
    }

    @JsonProperty
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
}
