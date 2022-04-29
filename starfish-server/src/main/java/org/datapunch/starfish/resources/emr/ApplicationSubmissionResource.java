package org.datapunch.starfish.resources.emr;

import com.codahale.metrics.annotation.Timed;
import org.datapunch.starfish.api.emr.*;
import org.datapunch.starfish.api.spark.SubmitSparkApplicationRequest;
import org.datapunch.starfish.core.EmrApplicationSubmissionConfiguration;
import org.datapunch.starfish.core.EmrSparkController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/emr/clusters")
public class ApplicationSubmissionResource {
    private final EmrApplicationSubmissionConfiguration submissionConfiguration;
    private final EmrSparkController controller;

    public ApplicationSubmissionResource(EmrApplicationSubmissionConfiguration submissionConfiguration) {
        this.submissionConfiguration = submissionConfiguration;
        this.controller = new EmrSparkController(submissionConfiguration);
    }

    @POST
    @Timed
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clusterId}/submissions")
    public SubmitSparkApplicationResponse submitSparkApplication(@PathParam("clusterId") String clusterId, SubmitSparkApplicationRequest request) {
        return controller.submitSparkApplication(clusterId, request);
    }

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clusterId}/submissions/{submissionId}")
    public GetSparkApplicationResponse getSparkApplication(@PathParam("clusterId") String clusterId, @PathParam("submissionId") String submissionId) {
        return controller.getSparkApplication(clusterId, submissionId);
    }
}
