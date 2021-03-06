package org.datapunch.starfish.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClusterRequest {
    private String region;
    private String clusterName;
    private String emrRelease;
    private String subnetId;
    private String logUri;
    private String serviceRoleName;
    private String jobFlowRoleName;
    private Integer instanceCount;
    private String masterInstanceType;
    private String slaveInstanceType;

    @JsonProperty
    public String getRegion() {
        return region;
    }

    @JsonProperty
    public void setRegion(String region) {
        this.region = region;
    }

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

    @JsonProperty
    public String getLogUri() {
        return logUri;
    }

    @JsonProperty
    public void setLogUri(String logUri) {
        this.logUri = logUri;
    }

    @JsonProperty
    public String getServiceRoleName() {
        return serviceRoleName;
    }

    @JsonProperty
    public void setServiceRoleName(String serviceRoleName) {
        this.serviceRoleName = serviceRoleName;
    }

    @JsonProperty
    public String getJobFlowRoleName() {
        return jobFlowRoleName;
    }

    @JsonProperty
    public void setJobFlowRoleName(String jobFlowRoleName) {
        this.jobFlowRoleName = jobFlowRoleName;
    }

    @JsonProperty
    public Integer getInstanceCount() {
        return instanceCount;
    }

    @JsonProperty
    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    @JsonProperty
    public String getMasterInstanceType() {
        return masterInstanceType;
    }

    @JsonProperty
    public void setMasterInstanceType(String masterInstanceType) {
        this.masterInstanceType = masterInstanceType;
    }

    @JsonProperty
    public String getSlaveInstanceType() {
        return slaveInstanceType;
    }

    @JsonProperty
    public void setSlaveInstanceType(String slaveInstanceType) {
        this.slaveInstanceType = slaveInstanceType;
    }
}
