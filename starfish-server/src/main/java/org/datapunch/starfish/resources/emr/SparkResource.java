package org.datapunch.starfish.resources.emr;

import com.codahale.metrics.annotation.Timed;
import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.api.spark.SubmitSparkApplicationRequest;
import org.datapunch.starfish.core.EmrClusterConfiguration;
import org.datapunch.starfish.core.EmrClusterController;
import org.datapunch.starfish.core.EmrSparkConfiguration;
import org.datapunch.starfish.core.EmrSparkController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/emr/clusters")
public class SparkResource {
    private final EmrSparkConfiguration sparkConfiguration;
    private final EmrSparkController controller;

    public SparkResource(EmrSparkConfiguration sparkConfiguration) {
        this.sparkConfiguration = sparkConfiguration;
        this.controller = new EmrSparkController(sparkConfiguration);
    }

    @POST
    @Timed
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clusterId}/spark")
    public SubmitSparkApplicationResponse submitSparkApplication(@PathParam("clusterId") String clusterId, SubmitSparkApplicationRequest request) {
        return controller.submitSparkApplication(clusterId, request);
    }

}
