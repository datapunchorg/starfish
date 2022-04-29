package org.datapunch.starfish.api.spark;

public class GetApplicationSubmissionResponse {
    private ApplicationSubmissionStatus status;

    public ApplicationSubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationSubmissionStatus status) {
        this.status = status;
    }
}
