package org.datapunch.starfish.core;

import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.api.spark.SubmitSparkApplicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmrSparkControllerIT {
    private static final Logger logger = LoggerFactory.getLogger(EmrSparkControllerIT.class);

    @Test
    public void testController() {
        String clusterFqid = "us-west-1-j-XSKSY82EOVZQ"; // TODO use environment variable
        boolean deleteClusterAfterTest = false;

        EmrClusterConfiguration clusterConfiguration = new EmrClusterConfiguration();

        // TODO query AWS account and get subnet id
        clusterConfiguration.setSubnetIds(Arrays.asList("subnet-1147f875"));

        EmrClusterController clusterController = new EmrClusterController(clusterConfiguration);

        if (clusterFqid == null) {
            CreateClusterRequest createClusterRequest = new CreateClusterRequest();
            createClusterRequest.setClusterName(String.format("IntegrationTest-%s", EmrClusterControllerIT.class.getSimpleName()));
            createClusterRequest.setLogUri("s3://datapunch-public-writeable-us-west-1/upload"); // TODO modify this
            CreateClusterResponse createClusterResponse = clusterController.createCluster(createClusterRequest);

            GetClusterResponse getClusterResponse = clusterController.getCluster(createClusterResponse.getClusterFqid());
            long startTime = System.currentTimeMillis();
            Set<String> readyStates = new HashSet<>(Arrays.asList("Waiting".toLowerCase()));
            while (System.currentTimeMillis() - startTime < 30 * 60 * 1000) {
                logger.info("EMR cluster {} in state {}", createClusterResponse.getClusterFqid(), getClusterResponse.getStatus().getState());
                if (getClusterResponse.getStatus().getState() != null &&
                        (readyStates.contains(getClusterResponse.getStatus().getState().toLowerCase())) || getClusterResponse.getStatus().getState().toLowerCase().contains("terminated")) {
                    break;
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                getClusterResponse = clusterController.getCluster(createClusterResponse.getClusterFqid());
            }

            Assert.assertEquals(getClusterResponse.getClusterFqid(), createClusterResponse.getClusterFqid());
            Assert.assertNotNull(getClusterResponse.getStatus());

            clusterFqid = getClusterResponse.getClusterFqid();
        }

        EmrSparkConfiguration sparkConfiguration = new EmrSparkConfiguration();
        EmrSparkController sparkController = new EmrSparkController(sparkConfiguration);

        SubmitSparkApplicationRequest submitSparkApplicationRequest = new SubmitSparkApplicationRequest();
        submitSparkApplicationRequest.setMainClass("org.apache.spark.examples.SparkPi");
        submitSparkApplicationRequest.setMainApplicationFile("s3a://datapunch-public-01/jars/spark-examples_2.12-3.1.2.jar");
        SubmitSparkApplicationResponse submitSparkApplicationResponse = sparkController.submitSparkApplication(clusterFqid, submitSparkApplicationRequest);
        Assert.assertNotNull(submitSparkApplicationResponse.getClusterFqid());
        Assert.assertNotNull(submitSparkApplicationResponse.getSubmissionId());

        if (deleteClusterAfterTest) {
            DeleteClusterResponse deleteClusterResponse = clusterController.deleteCluster(clusterFqid);
            Assert.assertNotNull(deleteClusterResponse.getClusterFqid());
        }
    }
}
