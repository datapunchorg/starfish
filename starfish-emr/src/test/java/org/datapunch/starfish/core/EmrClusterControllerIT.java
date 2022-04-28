package org.datapunch.starfish.core;

import org.datapunch.starfish.api.emr.CreateClusterRequest;
import org.datapunch.starfish.api.emr.CreateClusterResponse;
import org.datapunch.starfish.api.emr.DeleteClusterResponse;
import org.datapunch.starfish.api.emr.GetClusterResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class EmrClusterControllerIT {
    @Test
    public void testController() {
        EmrClusterConfiguration clusterConfiguration = new EmrClusterConfiguration();

        // TODO query AWS account and get subnet id
        clusterConfiguration.setSubnetIds(Arrays.asList("subnet-1147f875"));

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
