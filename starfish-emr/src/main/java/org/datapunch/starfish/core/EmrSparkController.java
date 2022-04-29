package org.datapunch.starfish.core;

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.datapunch.starfish.api.spark.*;
import org.datapunch.starfish.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EmrSparkController {
    private static final Logger logger = LoggerFactory.getLogger(EmrSparkController.class);

    private static final Set<String> finishedStatesLowerCase = new HashSet<>(Arrays.asList("completed", "failed"));

    private final EmrApplicationSubmissionConfiguration config;

    public EmrSparkController(EmrApplicationSubmissionConfiguration config) {
        this.config = config == null ? new EmrApplicationSubmissionConfiguration() : config;
    }

    public SubmitSparkApplicationResponse submitSparkApplication(String clusterFqidStr, SubmitSparkApplicationRequest request) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        HadoopJarStepConfig sparkStepConf = new HadoopJarStepConfig()
                        .withJar("command-runner.jar")
                        .withArgs("spark-submit")
                        .withArgs("--master", "yarn");
        if (!StringUtil.isNullOrEmpty(request.getMainClass())) {
            sparkStepConf = sparkStepConf.withArgs("--class", request.getMainClass());
        }
        Map<String, String> sparkConf = new HashMap<>();
        if (request.getDriver() != null) {
            if (request.getDriver().getCores() > 0) {
                sparkConf.put("spark.driver.cores", String.valueOf(request.getDriver().getCores()));
            }
            if (!StringUtil.isNullOrEmpty(request.getDriver().getMemory())) {
                sparkConf.put("spark.driver.memory", request.getDriver().getMemory());
            }
        }
        if (request.getExecutor() != null) {
            if (request.getExecutor().getCores() > 0) {
                sparkConf.put("spark.executor.cores", String.valueOf(request.getExecutor().getCores()));
            }
            if (!StringUtil.isNullOrEmpty(request.getExecutor().getMemory())) {
                sparkConf.put("spark.executor.memory", request.getExecutor().getMemory());
            }
            if (request.getExecutor().getInstances() > 0) {
                sparkConf.put("spark.executor.instances", String.valueOf(request.getExecutor().getInstances()));
            }
        }
        if (request.getSparkConf() != null) {
            sparkConf.putAll(request.getSparkConf());
        }
        if (sparkConf != null) {
            for (Map.Entry<String, String> entry: sparkConf.entrySet()) {
                sparkStepConf = sparkStepConf.withArgs("--conf", String.format("%s=%s", entry.getKey(), entry.getValue()));
            }
        }

        sparkStepConf = sparkStepConf.withArgs(request.getMainApplicationFile());
        if (request.getArguments() != null && !request.getArguments().isEmpty()) {
            sparkStepConf.withArgs(request.getArguments());
        }

        StepConfig sparkStep = new StepConfig()
                .withName("Spark Step")
                .withActionOnFailure(ActionOnFailure.CONTINUE)
                .withHadoopJarStep(sparkStepConf);
        AddJobFlowStepsRequest addJobFlowStepsRequest = new AddJobFlowStepsRequest();
        addJobFlowStepsRequest.withJobFlowId(clusterFqid.getClusterId())
                .withSteps(sparkStep);
        AmazonElasticMapReduce emr = EmrHelper.getEmr(clusterFqid.getRegion());
        AddJobFlowStepsResult addJobFlowStepsResult = emr.addJobFlowSteps(addJobFlowStepsRequest);
        List<String> stepIds = addJobFlowStepsResult.getStepIds();
        if (stepIds.size() != 1) {
            throw new RuntimeException(String.format(
                    "Failed to submit Spark application to cluster %s, expecting 1 step id in the result, but got %s",
                    clusterFqid, stepIds.size()));
        }
        SubmitSparkApplicationResponse response = new SubmitSparkApplicationResponse();
        response.setSubmissionId(stepIds.get(0));
        return response;
    }

    public GetApplicationSubmissionResponse getSparkApplication(String clusterFqidStr, String submissionId) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(clusterFqidStr);
        AmazonElasticMapReduce emr = EmrHelper.getEmr(clusterFqid.getRegion());
        DescribeStepRequest describeStepRequest = new DescribeStepRequest();
        describeStepRequest.setClusterId(clusterFqid.getClusterId());
        describeStepRequest.setStepId(submissionId);
        DescribeStepResult describeStepResult = emr.describeStep(describeStepRequest);
        String state = describeStepResult.getStep().getStatus().getState();
        String errorMessage = null;
        if (describeStepResult.getStep().getStatus().getFailureDetails() != null) {
            errorMessage = describeStepResult.getStep().getStatus().getFailureDetails().toString();
        }
        ApplicationSubmissionStatus applicationSubmissionStatus = new ApplicationSubmissionStatus();
        applicationSubmissionStatus.setSubmissionId(submissionId);
        applicationSubmissionStatus.setState(state);
        applicationSubmissionStatus.setApplicationMessage(errorMessage);
        GetApplicationSubmissionResponse response = new GetApplicationSubmissionResponse();
        response.setStatus(applicationSubmissionStatus);
        return response;
    }

    public void waitSparkApplicationFinished(String clusterFqidStr, String submissionId, long maxWaitMillis, long sleepIntervalMillis) {
        long startTime = System.currentTimeMillis();
        String state = null;
        while (System.currentTimeMillis() - startTime <= maxWaitMillis) {
            GetApplicationSubmissionResponse getApplicationSubmissionResponse = getSparkApplication(clusterFqidStr, submissionId);
            if (getApplicationSubmissionResponse.getStatus() != null) {
                state = getApplicationSubmissionResponse.getStatus().getState();
                if (state != null) {
                    if (finishedStatesLowerCase.contains(state.toLowerCase())) {
                        logger.info("Spark application {} (cluster: {}) finished (state {})", submissionId, clusterFqidStr, state);
                        return;
                    } else {
                        logger.info("Spark application {} (cluster: {}) not finished (state {})", submissionId, clusterFqidStr, state);
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
                "Spark application %s (cluster: %s) not finished (state: %s) after waiting %s milliseconds",
                submissionId, clusterFqidStr, state, maxWaitMillis));
    }
}
