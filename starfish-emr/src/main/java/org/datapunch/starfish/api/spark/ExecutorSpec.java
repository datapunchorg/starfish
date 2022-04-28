package org.datapunch.starfish.api.spark;

public class ExecutorSpec extends PodSpec {
    private long instances;

    public ExecutorSpec() {
        this.instances = 1;
    }

    public long getInstances() {
        return instances;
    }

    public void setInstances(long instances) {
        this.instances = instances;
    }
}
