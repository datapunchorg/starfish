package org.datapunch.starfish.api.spark;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class SubmitSparkApplicationRequest {
    private String mainClass;
    private String mainApplicationFile;
    private List<String> arguments;

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

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
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
