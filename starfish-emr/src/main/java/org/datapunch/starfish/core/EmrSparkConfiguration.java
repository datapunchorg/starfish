package org.datapunch.starfish.core;

import java.util.List;

public class EmrSparkConfiguration {
    private String region;
    private String emrRelease;
    private List<String> subnetIds;

    // e.g. "s3://path/to/my/emr/logs"
    private String logUri;

    private String serviceRoleName;
    private String jobFlowRoleName;
    private String masterInstanceType;
    private String slaveInstanceType;

    private List<String> clusterConfigurationFiles;

    public EmrSparkConfiguration() {
        this.region = "us-west-1";
        this.emrRelease = "emr-6.5.0";
        this.serviceRoleName = "EMR_DefaultRole";
        this.jobFlowRoleName = "EMR_EC2_DefaultRole";
        this.masterInstanceType = "m4.large";
        this.slaveInstanceType = "m4.large";
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmrRelease() {
        return emrRelease;
    }

    public void setEmrRelease(String emrRelease) {
        this.emrRelease = emrRelease;
    }

    public List<String> getSubnetIds() {
        return subnetIds;
    }

    public void setSubnetIds(List<String> subnetIds) {
        this.subnetIds = subnetIds;
    }

    public String getLogUri() {
        return logUri;
    }

    public void setLogUri(String logUri) {
        this.logUri = logUri;
    }

    public String getServiceRoleName() {
        return serviceRoleName;
    }

    public void setServiceRoleName(String serviceRoleName) {
        this.serviceRoleName = serviceRoleName;
    }

    public String getJobFlowRoleName() {
        return jobFlowRoleName;
    }

    public void setJobFlowRoleName(String jobFlowRoleName) {
        this.jobFlowRoleName = jobFlowRoleName;
    }

    public String getMasterInstanceType() {
        return masterInstanceType;
    }

    public void setMasterInstanceType(String masterInstanceType) {
        this.masterInstanceType = masterInstanceType;
    }

    public String getSlaveInstanceType() {
        return slaveInstanceType;
    }

    public void setSlaveInstanceType(String slaveInstanceType) {
        this.slaveInstanceType = slaveInstanceType;
    }
}
