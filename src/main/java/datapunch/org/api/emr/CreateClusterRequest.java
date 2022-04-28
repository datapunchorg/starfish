package datapunch.org.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClusterRequest {
    private String emrRelease;

    @JsonProperty
    public String getEmrRelease() {
        return emrRelease;
    }

    @JsonProperty
    public void setEmrRelease(String emrRelease) {
        this.emrRelease = emrRelease;
    }
}
