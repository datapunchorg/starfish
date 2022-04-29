package org.datapunch.starfish;

import org.datapunch.starfish.core.EmrClusterConfiguration;
import io.dropwizard.Configuration;
import org.datapunch.starfish.core.EmrApplicationSubmissionConfiguration;

public class ServerConfiguration extends Configuration {

    private String dbConnectionString;
    private String dbUser;
    private String dbPassword;

    private EmrClusterConfiguration emrClusterConfiguration;
    private EmrApplicationSubmissionConfiguration sparkConfiguration;

    public String getDbConnectionString() {
        return dbConnectionString;
    }

    public void setDbConnectionString(String dbConnectionString) {
        this.dbConnectionString = dbConnectionString;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public EmrClusterConfiguration getEmrClusterConfiguration() {
        return emrClusterConfiguration;
    }

    public void setEmrClusterConfiguration(EmrClusterConfiguration emrClusterConfiguration) {
        this.emrClusterConfiguration = emrClusterConfiguration;
    }

    public EmrApplicationSubmissionConfiguration getSparkConfiguration() {
        return sparkConfiguration;
    }

    public void setSparkConfiguration(EmrApplicationSubmissionConfiguration sparkConfiguration) {
        this.sparkConfiguration = sparkConfiguration;
    }
}
