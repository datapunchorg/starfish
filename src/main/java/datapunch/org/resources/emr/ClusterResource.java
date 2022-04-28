package datapunch.org.resources.emr;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import com.codahale.metrics.annotation.Timed;
import datapunch.org.core.EmrClusterConfiguration;
import datapunch.org.api.emr.ClusterStatus;
import datapunch.org.api.emr.CreateClusterRequest;
import datapunch.org.api.emr.CreateClusterResponse;
import datapunch.org.api.emr.DeleteClusterResponse;
import datapunch.org.api.emr.GetClusterResponse;
import datapunch.org.core.EmrClusterController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.UUID;

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
