package datapunch.org;

import datapunch.org.core.EmrClusterConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class StarfishConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    private EmrClusterConfiguration emrClusterConfiguration;

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    @JsonProperty
    public EmrClusterConfiguration getEmrClusterConfiguration() {
        return emrClusterConfiguration;
    }

    @JsonProperty
    public void setEmrClusterConfiguration(EmrClusterConfiguration emrClusterConfiguration) {
        this.emrClusterConfiguration = emrClusterConfiguration;
    }
}
