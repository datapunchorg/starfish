package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datapunch.starfish.api.spark.SparkApplicationStatus;

public class GetSparkApplicationResponse {
    private SparkApplicationStatus status;

    public SparkApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(SparkApplicationStatus status) {
        this.status = status;
    }
}
