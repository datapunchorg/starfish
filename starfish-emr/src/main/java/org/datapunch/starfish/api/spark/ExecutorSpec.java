package org.datapunch.starfish.api.spark;

public class ExecutorSpec extends PodSpec {
    private int instances;

    public ExecutorSpec() {
        this.instances = 1;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }
}
