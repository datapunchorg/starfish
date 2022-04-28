package org.datapunch.starfish;

import org.datapunch.starfish.core.EmrClusterConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.datapunch.starfish.core.EmrSparkConfiguration;

import javax.validation.constraints.NotEmpty;

public class ServerConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    private EmrClusterConfiguration emrClusterConfiguration;
    private EmrSparkConfiguration sparkConfiguration;

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

    @JsonProperty
    public EmrSparkConfiguration getSparkConfiguration() {
        return sparkConfiguration;
    }

    @JsonProperty
    public void setSparkConfiguration(EmrSparkConfiguration sparkConfiguration) {
        this.sparkConfiguration = sparkConfiguration;
    }
}
