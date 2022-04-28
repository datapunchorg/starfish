package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitSparkApplicationRequest {
    private String mainClass;
    private String mainApplicationFile;

    private DriverSpec driver;
    private ExecutorSpec executor;

    @JsonProperty
    public String getMainClass() {
        return mainClass;
    }

    @JsonProperty
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @JsonProperty
    public String getMainApplicationFile() {
        return mainApplicationFile;
    }

    @JsonProperty
    public void setMainApplicationFile(String mainApplicationFile) {
        this.mainApplicationFile = mainApplicationFile;
    }

    @JsonProperty
    public DriverSpec getDriver() {
        return driver;
    }

    @JsonProperty
    public void setDriver(DriverSpec driver) {
        this.driver = driver;
    }

    @JsonProperty
    public ExecutorSpec getExecutor() {
        return executor;
    }

    @JsonProperty
    public void setExecutor(ExecutorSpec executor) {
        this.executor = executor;
    }
}
