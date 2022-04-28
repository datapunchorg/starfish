package org.datapunch.starfish.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.datapunch.starfish.api.emr.ClusterStatus;
import org.datapunch.starfish.api.emr.*;

import java.util.Arrays;

public class EmrSparkController {
    private final EmrClusterConfiguration config;

    public EmrSparkController(EmrClusterConfiguration config) {
        this.config = config == null ? new EmrClusterConfiguration() : config;
    }

    public CreateClusterResponse submitSparkApplication(SubmitSparkApplicationRequest request) {
        EmrClusterFqid clusterFqid = new EmrClusterFqid(request.getClusterFqid());
        HadoopJarStepConfig sparkStepConf = new HadoopJarStepConfig()
                        .withJar("command-runner.jar")
                        .withArgs("spark-submit")
                        .withArgs("--master", "yarn")
                        .withArgs("--class", "org.apache.spark.examples.SparkPi")
                        .withArgs("s3a://datapunch-public-01/jars/spark-examples_2.12-3.1.2.jar");
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
            return null;
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
