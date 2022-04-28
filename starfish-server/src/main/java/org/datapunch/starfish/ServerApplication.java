package org.datapunch.starfish;

import org.datapunch.starfish.health.TemplateHealthCheck;
import org.datapunch.starfish.resources.emr.ClusterResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.datapunch.starfish.resources.emr.SparkResource;

public class ServerApplication extends Application<ServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "starfish";
    }

    @Override
    public void initialize(final Bootstrap<ServerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ServerConfiguration configuration,
                    final Environment environment) {
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck("TODO");
        environment.healthChecks().register("check01", healthCheck);

        final ClusterResource clusterResource = new ClusterResource(configuration.getEmrClusterConfiguration());
        final SparkResource sparkResource = new SparkResource(configuration.getSparkConfiguration());

        environment.jersey().register(clusterResource);
        environment.jersey().register(sparkResource);
    }

}
