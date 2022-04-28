package datapunch.org;

import datapunch.org.health.TemplateHealthCheck;
import datapunch.org.resources.HelloWorldResource;
import datapunch.org.resources.emr.ClusterResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StarfishApplication extends Application<StarfishConfiguration> {

    public static void main(final String[] args) throws Exception {
        new StarfishApplication().run(args);
    }

    @Override
    public String getName() {
        return "starfish";
    }

    @Override
    public void initialize(final Bootstrap<StarfishConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final StarfishConfiguration configuration,
                    final Environment environment) {
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        final ClusterResource clusterResource = new ClusterResource();
        environment.jersey().register(clusterResource);
    }

}
