package org.datapunch.starfish.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.util.ListUtil;
import org.datapunch.starfish.util.StringUtil;
import org.datapunch.starfish.api.emr.ClusterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EmrClusterController {
    private static final Logger logger = LoggerFactory.getLogger(EmrClusterController.class);

    private final EmrClusterConfiguration config;

    private static final Set<String> finishedStatesLowerCase = new HashSet<>(Arrays.asList("waiting", "terminated", "terminated with errors"));

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
        Integer instanceCount = request.getInstanceCount();
        if (instanceCount == null) {
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
        AmazonElasticMapReduce emr = EmrHelper.getEmr(region);
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
        response.setClusterFqid(String.format("%s-%s", region, runJobFlowResult.getJobFlowId()));
        return response;
    }

    public GetClusterResponse getCluster(String clusterFqidStr) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        AmazonElasticMapReduce emr = EmrHelper.getEmr(clusterFqid.getRegion());
        DescribeClusterRequest describeClusterRequest = new DescribeClusterRequest();
        describeClusterRequest.setClusterId(clusterFqid.getClusterId());
        DescribeClusterResult describeClusterResult = emr.describeCluster(describeClusterRequest);
        org.datapunch.starfish.api.emr.ClusterStatus status = new ClusterStatus();
        status.setState(describeClusterResult.getCluster().getStatus().getState());
        status.setCode(describeClusterResult.getCluster().getStatus().getStateChangeReason().getCode());
        status.setInformation(describeClusterResult.getCluster().getStatus().getStateChangeReason().getMessage());
        GetClusterResponse response = new GetClusterResponse();
        response.setClusterFqid(clusterFqidStr);
        response.setStatus(status);
        return response;
    }

    public DeleteClusterResponse deleteCluster(String clusterFqidStr) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        AmazonElasticMapReduce emr = EmrHelper.getEmr(clusterFqid.getRegion());
        TerminateJobFlowsRequest terminateJobFlowsRequest = new TerminateJobFlowsRequest();
        terminateJobFlowsRequest.setJobFlowIds(Arrays.asList(clusterFqid.getClusterId()));
        emr.terminateJobFlows(terminateJobFlowsRequest);
        DeleteClusterResponse response = new DeleteClusterResponse();
        response.setClusterFqid(clusterFqidStr);
        return response;
    }

    public void waitClusterReadyOrTerminated(String clusterFqidStr, long maxWaitMillis, long sleepIntervalMillis) {
        long startTime = System.currentTimeMillis();
        String state = null;
        while (System.currentTimeMillis() - startTime <= maxWaitMillis) {
            GetClusterResponse getClusterResponse = getCluster(clusterFqidStr);
            if (getClusterResponse.getStatus() != null) {
                state = getClusterResponse.getStatus().getState();
                if (state != null) {
                    if (finishedStatesLowerCase.contains(state.toLowerCase())) {
                        logger.info("Cluster {} ready or terminated (state {})", clusterFqidStr, state);
                        return;
                    } else {
                        logger.info("Cluster {} not ready or terminated (state {})", clusterFqidStr, state);
                    }
                }
            }
            try {
                if (sleepIntervalMillis > 0) {
                    Thread.sleep(sleepIntervalMillis);
                }
            } catch (InterruptedException e) {
                logger.warn("InterruptedException", e);
            }
        }
        throw new RuntimeException(String.format(
                "Cluster %s not ready or terminated (state: %s) after waiting %s milliseconds",
                clusterFqidStr, state, maxWaitMillis));
    }
}
