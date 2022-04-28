package datapunch.org.starfish;

import datapunch.org.starfish.health.TemplateHealthCheck;
import datapunch.org.starfish.resources.HelloWorldResource;
import datapunch.org.starfish.resources.emr.ClusterResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        final ClusterResource clusterResource = new ClusterResource(configuration.getEmrClusterConfiguration());
        environment.jersey().register(clusterResource);
    }

}
