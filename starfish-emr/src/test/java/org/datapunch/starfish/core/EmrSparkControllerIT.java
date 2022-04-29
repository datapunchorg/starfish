package org.datapunch.starfish.core;

import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.api.spark.DriverSpec;
import org.datapunch.starfish.api.spark.ExecutorSpec;
import org.datapunch.starfish.api.spark.SubmitSparkApplicationRequest;
import org.datapunch.starfish.awslib.Ec2Helper;
import org.datapunch.starfish.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

public class EmrSparkControllerIT {
    private static final Logger logger = LoggerFactory.getLogger(EmrSparkControllerIT.class);

    private static final String ENV_EMR_LOG_URI = "EMR_LOG_URI";

    private EmrClusterConfiguration clusterConfiguration;

    private String clusterFqid;
    private boolean deleteClusterAfterTest = true;

    @BeforeTest
    public void beforeTest() {
        clusterConfiguration = new EmrClusterConfiguration();

        List<String> subnetIds = Ec2Helper.getSubnetIds("us-east-1");
        clusterConfiguration.setSubnetIds(subnetIds);

        EmrClusterController clusterController = new EmrClusterController(clusterConfiguration);

        CreateClusterRequest createClusterRequest = new CreateClusterRequest();
        createClusterRequest.setClusterName(String.format("IntegrationTest-%s", EmrClusterControllerIT.class.getSimpleName()));
        String logUri = System.getenv("INTEGRATION_TEST_EMR_LOG_URI");
        if (!StringUtil.isNullOrEmpty(logUri)) {
            createClusterRequest.setLogUri(logUri);
        }
        CreateClusterResponse createClusterResponse = clusterController.createCluster(createClusterRequest);

        clusterController.waitClusterReadyOrTerminated(createClusterResponse.getClusterFqid(), 30*60*1000, 10000);

        GetClusterResponse getClusterResponse = clusterController.getCluster(createClusterResponse.getClusterFqid());
        Assert.assertEquals(getClusterResponse.getClusterFqid(), createClusterResponse.getClusterFqid());
        Assert.assertNotNull(getClusterResponse.getStatus());

        clusterFqid = getClusterResponse.getClusterFqid();
        logger.info("Created cluster {}", clusterFqid);
    }

    @AfterTest
    public void afterTest() {
        if (deleteClusterAfterTest) {
            logger.info("Deleting cluster {}", clusterFqid);

            EmrClusterController clusterController = new EmrClusterController(clusterConfiguration);

            DeleteClusterResponse deleteClusterResponse = clusterController.deleteCluster(clusterFqid);
            Assert.assertEquals(deleteClusterResponse.getClusterFqid(), clusterFqid);
        }
    }

    @Test
    public void testSparkController() {
        EmrSparkConfiguration sparkConfiguration = new EmrSparkConfiguration();
        EmrSparkController sparkController = new EmrSparkController(sparkConfiguration);

        {
            SubmitSparkApplicationRequest submitSparkApplicationRequest = new SubmitSparkApplicationRequest();
            submitSparkApplicationRequest.setMainClass("org.apache.spark.examples.SparkPi");
            submitSparkApplicationRequest.setMainApplicationFile("s3a://datapunch-public-01/jars/spark-examples_2.12-3.1.2.jar");
            SubmitSparkApplicationResponse submitSparkApplicationResponse = sparkController.submitSparkApplication(clusterFqid, submitSparkApplicationRequest);
            logger.info("Submitted Spark application to cluster {}, got submission id: {}", submitSparkApplicationResponse.getClusterFqid(), submitSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(submitSparkApplicationResponse.getClusterFqid());
            Assert.assertNotNull(submitSparkApplicationResponse.getSubmissionId());

            GetSparkApplicationResponse getSparkApplicationResponse = sparkController.getSparkApplication(clusterFqid, submitSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(getSparkApplicationResponse.getClusterFqid());
            Assert.assertNotNull(getSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(getSparkApplicationResponse.getStatus());

            sparkController.waitSparkApplicationFinished(clusterFqid, submitSparkApplicationResponse.getSubmissionId(), 10*60*1000, 10000);

            getSparkApplicationResponse = sparkController.getSparkApplication(clusterFqid, submitSparkApplicationResponse.getSubmissionId());
            Assert.assertEquals(getSparkApplicationResponse.getStatus().getApplicationState().getState(), "COMPLETED");
        }

        {
            SubmitSparkApplicationRequest submitSparkApplicationRequest = new SubmitSparkApplicationRequest();
            submitSparkApplicationRequest.setMainClass("org.apache.spark.examples.SparkPi");
            submitSparkApplicationRequest.setMainApplicationFile("s3a://datapunch-public-01/jars/spark-examples_2.12-3.1.2.jar");
            submitSparkApplicationRequest.setDriver(new DriverSpec());
            submitSparkApplicationRequest.setExecutor(new ExecutorSpec());
            submitSparkApplicationRequest.getDriver().setCores(2);
            submitSparkApplicationRequest.getDriver().setMemory("1g");
            submitSparkApplicationRequest.getExecutor().setCores(1);
            submitSparkApplicationRequest.getExecutor().setMemory("700m");
            submitSparkApplicationRequest.setSparkConf(new HashMap<>());
            submitSparkApplicationRequest.getSparkConf().put("spark.conf.example1", "value1");
            SubmitSparkApplicationResponse submitSparkApplicationResponse = sparkController.submitSparkApplication(clusterFqid, submitSparkApplicationRequest);
            logger.info("Submitted Spark application to cluster {}, got submission id: {}", submitSparkApplicationResponse.getClusterFqid(), submitSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(submitSparkApplicationResponse.getClusterFqid());
            Assert.assertNotNull(submitSparkApplicationResponse.getSubmissionId());

            GetSparkApplicationResponse getSparkApplicationResponse = sparkController.getSparkApplication(clusterFqid, submitSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(getSparkApplicationResponse.getClusterFqid());
            Assert.assertNotNull(getSparkApplicationResponse.getSubmissionId());
            Assert.assertNotNull(getSparkApplicationResponse.getStatus());

            sparkController.waitSparkApplicationFinished(clusterFqid, submitSparkApplicationResponse.getSubmissionId(), 10*60*1000, 10000);

            getSparkApplicationResponse = sparkController.getSparkApplication(clusterFqid, submitSparkApplicationResponse.getSubmissionId());
            Assert.assertEquals(getSparkApplicationResponse.getStatus().getApplicationState().getState(), "COMPLETED");
        }
    }
}
