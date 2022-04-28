package datapunch.org.starfish.core;

import datapunch.org.starfish.api.emr.CreateClusterRequest;
import datapunch.org.starfish.api.emr.CreateClusterResponse;
import datapunch.org.starfish.api.emr.DeleteClusterResponse;
import datapunch.org.starfish.api.emr.GetClusterResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmrClusterControllerIT {
    @Test
    public void testController() {
        EmrClusterConfiguration clusterConfiguration = new EmrClusterConfiguration();
        EmrClusterController controller = new EmrClusterController(clusterConfiguration);

        CreateClusterRequest createClusterRequest = new CreateClusterRequest();
        createClusterRequest.setClusterName(String.format("IntegrationTest-%s", EmrClusterControllerIT.class.getSimpleName()));
        CreateClusterResponse createClusterResponse = controller.createCluster(createClusterRequest);

        GetClusterResponse getClusterResponse = controller.getCluster(createClusterResponse.getClusterId());
        Assert.assertEquals(getClusterResponse.getClusterId(), createClusterResponse.getClusterId());
        Assert.assertNotNull(getClusterResponse.getStatus());

        DeleteClusterResponse deleteClusterResponse = controller.deleteCluster(createClusterResponse.getClusterId());
        Assert.assertNotNull(deleteClusterResponse.getClusterId());
    }
}
