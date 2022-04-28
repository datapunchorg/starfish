package datapunch.org.resources.emr;

import com.codahale.metrics.annotation.Timed;
import datapunch.org.api.Saying;
import datapunch.org.api.emr.CreateClusterRequest;
import datapunch.org.api.emr.CreateClusterResponse;
import datapunch.org.api.emr.DeleteClusterResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/emr/clusters")
public class ClusterResource {

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
        return new CreateClusterResponse();
    }

    @DELETE
    @Timed
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public DeleteClusterResponse deleteCluster(@PathParam("id") String id) {
        return new DeleteClusterResponse();
    }
}
