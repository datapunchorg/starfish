package datapunch.org.starfish.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import datapunch.org.starfish.api.emr.*;
import datapunch.org.starfish.api.emr.ClusterStatus;
import datapunch.org.starfish.util.AwsUtil;
import datapunch.org.starfish.util.ListUtil;
import datapunch.org.starfish.util.StringUtil;

import java.util.Arrays;
import java.util.UUID;

public class EmrClusterController {
    private final EmrClusterConfiguration config;

    public EmrClusterController(EmrClusterConfiguration config) {
        this.config = config == null ? new EmrClusterConfiguration() : config;
    }

    public CreateClusterResponse createCluster(CreateClusterRequest request) {
        // create a step to enable debugging in the AWS Management Console
        StepFactory stepFactory = new StepFactory();
        StepConfig enabledebugging = new StepConfig()
                .withName("Enable debugging")
                .withActionOnFailure("TERMINATE_JOB_FLOW")
                .withHadoopJarStep(stepFactory.newEnableDebuggingStep());

        // specify applications to be installed and configured when EMR creates the cluster
        Application spark = new Application().withName("Spark");

        String region = request.getRegion();
        if (StringUtil.isNullOrEmpty(region) && !StringUtil.isNullOrEmpty(config.getRegion())) {
            region = config.getRegion();
        }
        String clusterName = request.getClusterName();
        if (StringUtil.isNullOrEmpty(clusterName)) {
            clusterName = UUID.randomUUID().toString();
        }
        String releaseLabel = request.getEmrRelease();
        if (StringUtil.isNullOrEmpty(releaseLabel) && !StringUtil.isNullOrEmpty(config.getEmrRelease())) {
            releaseLabel = config.getEmrRelease();
        }
        String subnetId = request.getSubnetId();
        if (StringUtil.isNullOrEmpty(subnetId) && !ListUtil.isNullOrEmpty(config.getSubnetIds())) {
            subnetId = ListUtil.getRandomValue(config.getSubnetIds());
        }
        String logUri = request.getLogUri();
        if (StringUtil.isNullOrEmpty(logUri) && !StringUtil.isNullOrEmpty(config.getLogUri())) {
            logUri = config.getLogUri();
        }
        String serviceRoleName = request.getServiceRoleName();
        if (StringUtil.isNullOrEmpty(serviceRoleName) && !StringUtil.isNullOrEmpty(config.getServiceRoleName())) {
            serviceRoleName = config.getServiceRoleName();
        }
        String jobFlowRoleName = request.getServiceRoleName();
        if (StringUtil.isNullOrEmpty(jobFlowRoleName) && !StringUtil.isNullOrEmpty(config.getJobFlowRoleName())) {
            jobFlowRoleName = config.getJobFlowRoleName();
        }
        int instanceCount = request.getInstanceCount();
        if (instanceCount == 0) {
            instanceCount = 3;
        }
        String masterInstanceType = request.getMasterInstanceType();
        if (StringUtil.isNullOrEmpty(masterInstanceType) && !StringUtil.isNullOrEmpty(config.getMasterInstanceType())) {
            masterInstanceType = config.getMasterInstanceType();
        }
        String slaveInstanceType = request.getSlaveInstanceType();
        if (StringUtil.isNullOrEmpty(slaveInstanceType) && !StringUtil.isNullOrEmpty(config.getSlaveInstanceType())) {
            slaveInstanceType = config.getSlaveInstanceType();
        }

        // create the cluster
        AmazonElasticMapReduce emr = getEmr(region);
        RunJobFlowRequest runJobFlowRequest = new RunJobFlowRequest()
                .withName(clusterName)
                .withReleaseLabel(releaseLabel)
                .withSteps(enabledebugging)
                .withApplications(spark)
                .withLogUri(logUri)
                .withServiceRole(serviceRoleName)
                .withJobFlowRole(jobFlowRoleName)
                .withInstances(new JobFlowInstancesConfig()
                        .withEc2SubnetId(subnetId)
                        // .withEc2KeyName("myEc2Key")
                        .withInstanceCount(instanceCount)
                        .withKeepJobFlowAliveWhenNoSteps(true)
                        .withMasterInstanceType(masterInstanceType)
                        .withSlaveInstanceType(slaveInstanceType));
        RunJobFlowResult runJobFlowResult = emr.runJobFlow(runJobFlowRequest);
        CreateClusterResponse response = new CreateClusterResponse();
        response.setClusterId(String.format("%s-%s", region, runJobFlowResult.getJobFlowId()));
        return response;
    }

    public GetClusterResponse getCluster(String id) {
        EmrClusterFqid clusterFqid = getClusterFqid(id);
        AmazonElasticMapReduce emr = getEmr(clusterFqid.getRegion());
        DescribeClusterRequest describeClusterRequest = new DescribeClusterRequest();
        describeClusterRequest.setClusterId(clusterFqid.getClusterId());
        DescribeClusterResult describeClusterResult = emr.describeCluster(describeClusterRequest);
        ClusterStatus status = new ClusterStatus();
        status.setState(describeClusterResult.getCluster().getStatus().getState());
        status.setCode(describeClusterResult.getCluster().getStatus().getStateChangeReason().getCode());
        status.setInformation(describeClusterResult.getCluster().getStatus().getStateChangeReason().getMessage());
        GetClusterResponse response = new GetClusterResponse();
        response.setClusterId(id);
        response.setStatus(status);
        return response;
    }

    public DeleteClusterResponse deleteCluster(String id) {
        EmrClusterFqid clusterFqid = getClusterFqid(id);
        AmazonElasticMapReduce emr = getEmr(clusterFqid.getRegion());
        TerminateJobFlowsRequest terminateJobFlowsRequest = new TerminateJobFlowsRequest();
        terminateJobFlowsRequest.setJobFlowIds(Arrays.asList(clusterFqid.getClusterId()));
        emr.terminateJobFlows(terminateJobFlowsRequest);
        DeleteClusterResponse response = new DeleteClusterResponse();
        response.setClusterId(id);
        return response;
    }

    private EmrClusterFqid getClusterFqid(String id) {
        String region = AwsUtil.getRegionFromPrefix(id);
        id = id.substring(region.length());
        if (id.startsWith("-")) {
            id = id.substring(1);
        }
        return new EmrClusterFqid(region, id);
    }

    private AmazonElasticMapReduce getEmr(String region) {
        DefaultAWSCredentialsProviderChain defaultAWSCredentialsProviderChain = new DefaultAWSCredentialsProviderChain();
        // create an EMR client using the credentials and region specified in order to create the cluster
        return AmazonElasticMapReduceClientBuilder.standard()
                .withCredentials(defaultAWSCredentialsProviderChain)
                .withRegion(region)
                .build();
    }
}