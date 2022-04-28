package datapunch.org.api.emr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClusterStatus {
    private String state;
    private String code;
    private String information;

    @JsonProperty
    public String getState() {
        return state;
    }

    @JsonProperty
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty
    public String getCode() {
        return code;
    }

    @JsonProperty
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty
    public String getInformation() {
        return information;
    }

    @JsonProperty
    public void setInformation(String information) {
        this.information = information;
    }
}
