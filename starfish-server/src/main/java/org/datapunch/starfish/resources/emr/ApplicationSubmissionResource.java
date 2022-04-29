package org.datapunch.starfish.resources.emr;

import com.codahale.metrics.annotation.Timed;
import org.datapunch.starfish.api.spark.*;
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
    public GetApplicationSubmissionResponse getSparkApplication(@PathParam("clusterId") String clusterId, @PathParam("submissionId") String submissionId) {
        return controller.getSparkApplication(clusterId, submissionId);
    }

    @GET
    @Timed
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clusterId}/submissions/{submissionId}/status")
    public GetApplicationSubmissionStatusResponse getSparkApplicationStatus(@PathParam("clusterId") String clusterId, @PathParam("submissionId") String submissionId) {
        ApplicationSubmissionStatus status = controller.getSparkApplication(clusterId, submissionId).getStatus();
        if (status == null) {
            throw new WebApplicationException(
                    String.format("Failed to get submission status for %s (cluster: %s)", submissionId, clusterId)
            );
        }
        GetApplicationSubmissionStatusResponse response = new GetApplicationSubmissionStatusResponse();
        response.setSubmissionId(status.getSubmissionId());
        response.setState(status.getState());
        response.setApplicationMessage(status.getApplicationMessage());
        return response;
    }
}
