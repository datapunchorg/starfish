package datapunch.org.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import datapunch.org.api.emr.*;
import datapunch.org.api.emr.ClusterStatus;

import javax.ws.rs.PathParam;
import java.util.Arrays;
import java.util.UUID;

public class EmrClusterController {
    private final EmrClusterConfiguration clusterConfiguration;

    public EmrClusterController(EmrClusterConfiguration clusterConfiguration) {
        this.clusterConfiguration = clusterConfiguration;
    }

    public CreateClusterResponse createCluster(CreateClusterRequest request) {
        AmazonElasticMapReduce emr = getEmr();

        // create a step to enable debugging in the AWS Management Console
        StepFactory stepFactory = new StepFactory();
        StepConfig enabledebugging = new StepConfig()
                .withName("Enable debugging")
                .withActionOnFailure("TERMINATE_JOB_FLOW")
                .withHadoopJarStep(stepFactory.newEnableDebuggingStep());

        // specify applications to be installed and configured when EMR creates the cluster
        Application spark = new Application().withName("Spark");

        String clusterName = request.getClusterName();
        if (clusterName == null || clusterName.isEmpty()) {
            clusterName = UUID.randomUUID().toString();
        }
        String releaseLabel = request.getEmrRelease();
        if (releaseLabel == null || releaseLabel.isEmpty()) {
            releaseLabel = "emr-5.20.0";
        }

        // create the cluster
        RunJobFlowRequest runJobFlowRequest = new RunJobFlowRequest()
                .withName(clusterName)
                .withReleaseLabel(releaseLabel)
                .withSteps(enabledebugging)
                .withApplications(spark)
                .withLogUri("s3://path/to/my/emr/logs")
                .withServiceRole("EMR_DefaultRole") // replace the default with a custom IAM service role if one is used
                .withJobFlowRole("EMR_EC2_DefaultRole") // replace the default with a custom EMR role for the EC2 instance profile if one is used
                .withInstances(new JobFlowInstancesConfig()
                        .withEc2SubnetId("subnet-12ab34c56")
                        // .withEc2KeyName("myEc2Key")
                        .withInstanceCount(3)
                        .withKeepJobFlowAliveWhenNoSteps(true)
                        .withMasterInstanceType("m4.large")
                        .withSlaveInstanceType("m4.large"));

        RunJobFlowResult runJobFlowResult = emr.runJobFlow(runJobFlowRequest);
        CreateClusterResponse response = new CreateClusterResponse();
        response.setClusterId(runJobFlowResult.getJobFlowId());
        return response;
    }

    public GetClusterResponse getCluster(String id) {
        AmazonElasticMapReduce emr = getEmr();
        DescribeClusterRequest describeClusterRequest = new DescribeClusterRequest();
        describeClusterRequest.setClusterId(id);
        DescribeClusterResult describeClusterResult = emr.describeCluster(describeClusterRequest);
        datapunch.org.api.emr.ClusterStatus status = new ClusterStatus();
        status.setState(describeClusterResult.getCluster().getStatus().getState());
        status.setCode(describeClusterResult.getCluster().getStatus().getStateChangeReason().getCode());
        status.setInformation(describeClusterResult.getCluster().getStatus().getStateChangeReason().getMessage());
        GetClusterResponse response = new GetClusterResponse();
        response.setClusterId(id);
        response.setStatus(status);
        return response;
    }

    public DeleteClusterResponse deleteCluster(String id) {
        AmazonElasticMapReduce emr = getEmr();
        TerminateJobFlowsRequest terminateJobFlowsRequest = new TerminateJobFlowsRequest();
        terminateJobFlowsRequest.setJobFlowIds(Arrays.asList(id));
        emr.terminateJobFlows(terminateJobFlowsRequest);
        DeleteClusterResponse response = new DeleteClusterResponse();
        response.setClusterId(id);
        return response;
    }

    private AmazonElasticMapReduce getEmr() {
        DefaultAWSCredentialsProviderChain defaultAWSCredentialsProviderChain = new DefaultAWSCredentialsProviderChain();
        // create an EMR client using the credentials and region specified in order to create the cluster
        return AmazonElasticMapReduceClientBuilder.standard()
                .withCredentials(defaultAWSCredentialsProviderChain)
                .withRegion(Regions.US_WEST_1)
                .build();
    }
}
