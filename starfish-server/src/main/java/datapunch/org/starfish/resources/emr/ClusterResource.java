package datapunch.org.starfish.resources.emr;

import com.codahale.metrics.annotation.Timed;
import datapunch.org.starfish.api.emr.CreateClusterRequest;
import datapunch.org.starfish.api.emr.CreateClusterResponse;
import datapunch.org.starfish.api.emr.DeleteClusterResponse;
import datapunch.org.starfish.api.emr.GetClusterResponse;
import datapunch.org.starfish.core.EmrClusterConfiguration;
import datapunch.org.starfish.core.EmrClusterController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/emr/clusters")
public class ClusterResource {
    private final EmrClusterConfiguration clusterConfiguration;
    private final EmrClusterController controller;

    public ClusterResource(EmrClusterConfiguration clusterConfiguration) {
        this.clusterConfiguration = clusterConfiguration;
        this.controller = new EmrClusterController(clusterConfiguration);
    }

    @GET
    @Timed
    @Path("/test")
    public String test() {
        return "OK";
    }

    @POST
    @Timed
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public CreateClusterResponse createCluster(CreateClusterRequest request) {
        return controller.createCluster(request);
    }

    @GET
    @Timed
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public GetClusterResponse getCluster(@PathParam("id") String id) {
        return controller.getCluster(id);
    }

    @DELETE
    @Timed
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public DeleteClusterResponse deleteCluster(@PathParam("id") String id) {
        return controller.deleteCluster(id);
    }
}
