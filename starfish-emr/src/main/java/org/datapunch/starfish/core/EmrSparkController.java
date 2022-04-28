package org.datapunch.starfish.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.api.spark.SparkApplicationState;
import org.datapunch.starfish.api.spark.SparkApplicationStatus;
import org.datapunch.starfish.api.spark.SubmitSparkApplicationRequest;

import java.util.List;

public class EmrSparkController {
    private final EmrSparkConfiguration config;

    public EmrSparkController(EmrSparkConfiguration config) {
        this.config = config == null ? new EmrSparkConfiguration() : config;
    }

    public SubmitSparkApplicationResponse submitSparkApplication(String clusterFqidStr, SubmitSparkApplicationRequest request) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        HadoopJarStepConfig sparkStepConf = new HadoopJarStepConfig()
                        .withJar("command-runner.jar")
                        .withArgs("spark-submit")
                        .withArgs("--master", "yarn")
                        .withArgs("--class", request.getMainClass())
                        .withArgs(request.getMainApplicationFile());
        StepConfig sparkStep = new StepConfig()
                .withName("Spark Step")
                .withActionOnFailure(ActionOnFailure.CONTINUE)
                .withHadoopJarStep(sparkStepConf);
        AddJobFlowStepsRequest addJobFlowStepsRequest = new AddJobFlowStepsRequest();
        addJobFlowStepsRequest.withJobFlowId(clusterFqid.getClusterId())
                .withSteps(sparkStep);
        AmazonElasticMapReduce emr = getEmr(clusterFqid.getRegion());
        try {
            AddJobFlowStepsResult addJobFlowStepsResult = emr.addJobFlowSteps(addJobFlowStepsRequest);
            List<String> stepIds = addJobFlowStepsResult.getStepIds();
            if (stepIds.size() != 1) {
                throw new RuntimeException(String.format(
                        "Failed to submit Spark application to cluster %s, expecting 1 step id in the result, but got %s",
                        clusterFqid, stepIds.size()));
            }
            SubmitSparkApplicationResponse response = new SubmitSparkApplicationResponse();
            response.setClusterFqid(clusterFqid.toString());
            response.setSubmissionId(stepIds.get(0));
            return response;
        } finally {
            emr.shutdown();
        }
    }

    public GetSparkApplicationResponse getSparkApplication(String clusterFqidStr, String submissionId) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        AmazonElasticMapReduce emr = getEmr(clusterFqid.getRegion());
        try {
            DescribeStepRequest describeStepRequest = new DescribeStepRequest();
            describeStepRequest.setClusterId(clusterFqid.getClusterId());
            describeStepRequest.setStepId(submissionId);
            DescribeStepResult describeStepResult = emr.describeStep(describeStepRequest);
            String state = describeStepResult.getStep().getStatus().getState();
            String errorMessage = null;
            if (describeStepResult.getStep().getStatus().getFailureDetails() != null) {
                errorMessage = describeStepResult.getStep().getStatus().getFailureDetails().toString();
            }
            SparkApplicationState sparkApplicationState = new SparkApplicationState();
            sparkApplicationState.setState(state);
            sparkApplicationState.setErrorMessage(errorMessage);
            SparkApplicationStatus sparkApplicationStatus = new SparkApplicationStatus();
            sparkApplicationStatus.setApplicationState(sparkApplicationState);
            GetSparkApplicationResponse response = new GetSparkApplicationResponse();
            response.setClusterFqid(clusterFqidStr);
            response.setSubmissionId(submissionId);
            response.setStatus(sparkApplicationStatus);
            return response;
        } finally {
            emr.shutdown();
        }
    }

    // TODO move getEmr to helper function
    private AmazonElasticMapReduce getEmr(String region) {
        DefaultAWSCredentialsProviderChain defaultAWSCredentialsProviderChain = new DefaultAWSCredentialsProviderChain();
        // create an EMR client using the credentials and region specified in order to create the cluster
        return AmazonElasticMapReduceClientBuilder.standard()
                .withCredentials(defaultAWSCredentialsProviderChain)
                .withRegion(region)
                .build();
    }
}
