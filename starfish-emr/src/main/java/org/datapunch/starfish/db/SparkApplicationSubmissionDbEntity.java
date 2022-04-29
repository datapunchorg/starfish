package org.datapunch.starfish.db;

public class SparkApplicationSubmissionDbEntity {
    private Long createTimeMillis;
    private String environment = "default";
    private String cluster;
    private String submissionId;
    private String submissionRequest;

    public Long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(Long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmissionRequest() {
        return submissionRequest;
    }

    public void setSubmissionRequest(String submissionRequest) {
        this.submissionRequest = submissionRequest;
    }
}
