package org.datapunch.starfish;

import org.datapunch.starfish.health.TemplateHealthCheck;
import org.datapunch.starfish.resources.emr.ClusterResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.datapunch.starfish.resources.emr.ApplicationSubmissionResource;
import org.datapunch.starfish.resources.root.RootResource;

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

        final RootResource rootResource = new RootResource();
        final ClusterResource clusterResource = new ClusterResource(configuration.getEmrClusterConfiguration());
        final ApplicationSubmissionResource applicationSubmissionResource = new ApplicationSubmissionResource(configuration.getSparkConfiguration());

        environment.jersey().register(rootResource);
        environment.jersey().register(clusterResource);
        environment.jersey().register(applicationSubmissionResource);
    }

}
