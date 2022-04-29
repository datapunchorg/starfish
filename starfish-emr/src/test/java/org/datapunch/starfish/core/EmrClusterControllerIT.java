package org.datapunch.starfish.core;

import org.datapunch.starfish.api.emr.CreateClusterRequest;
import org.datapunch.starfish.api.emr.CreateClusterResponse;
import org.datapunch.starfish.api.emr.DeleteClusterResponse;
import org.datapunch.starfish.api.emr.GetClusterResponse;
import org.datapunch.starfish.awslib.Ec2Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class EmrClusterControllerIT {
    @Test
    public void testController() {
        EmrClusterConfiguration clusterConfiguration = new EmrClusterConfiguration();

        List<String> subnetIds = Ec2Helper.getSubnetIds("us-west-1");
        clusterConfiguration.setSubnetIds(subnetIds);

        EmrClusterController controller = new EmrClusterController(clusterConfiguration);

        CreateClusterRequest createClusterRequest = new CreateClusterRequest();
        createClusterRequest.setClusterName(String.format("IntegrationTest-%s", EmrClusterControllerIT.class.getSimpleName()));
        CreateClusterResponse createClusterResponse = controller.createCluster(createClusterRequest);

        GetClusterResponse getClusterResponse = controller.getCluster(createClusterResponse.getClusterFqid());
        Assert.assertEquals(getClusterResponse.getClusterFqid(), createClusterResponse.getClusterFqid());
        Assert.assertNotNull(getClusterResponse.getStatus());

        DeleteClusterResponse deleteClusterResponse = controller.deleteCluster(createClusterResponse.getClusterFqid());
        Assert.assertNotNull(deleteClusterResponse.getClusterFqid());
    }
}
