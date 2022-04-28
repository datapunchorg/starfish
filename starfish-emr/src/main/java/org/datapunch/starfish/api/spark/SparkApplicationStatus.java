package org.datapunch.starfish.api.spark;

public class SparkApplicationStatus {
    private SparkApplicationState applicationState;

    public SparkApplicationState getApplicationState() {
        return applicationState;
    }

    public void setApplicationState(SparkApplicationState applicationState) {
        this.applicationState = applicationState;
    }
}
