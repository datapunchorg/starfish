package org.datapunch.starfish.api.spark;

public class PodSpec {
    private int cores;
    private String memory;

    public PodSpec() {
        this.cores = 1;
        this.memory = "512m";
    }
    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
}
