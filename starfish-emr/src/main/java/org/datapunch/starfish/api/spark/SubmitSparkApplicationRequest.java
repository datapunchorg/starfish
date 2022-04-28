package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SubmitSparkApplicationRequest {
    private String mainClass;
    private String mainApplicationFile;

    private DriverSpec driver;
    private ExecutorSpec executor;

    private Map<String, String> sparkConf;

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getMainApplicationFile() {
        return mainApplicationFile;
    }

    public void setMainApplicationFile(String mainApplicationFile) {
        this.mainApplicationFile = mainApplicationFile;
    }

    public DriverSpec getDriver() {
        return driver;
    }

    public void setDriver(DriverSpec driver) {
        this.driver = driver;
    }

    public ExecutorSpec getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorSpec executor) {
        this.executor = executor;
    }

    public Map<String, String> getSparkConf() {
        return sparkConf;
    }

    public void setSparkConf(Map<String, String> sparkConf) {
        this.sparkConf = sparkConf;
    }
}
