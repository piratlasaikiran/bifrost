package org.bhavani.constructions;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.inject.ServerModule;
import org.bhavani.constructions.resources.SupervisorResource;
import ru.vyarus.dropwizard.guice.GuiceBundle;


public class Server extends Application<ServerConfiguration> {

    private static final String RESOURCE_PACKAGE = "org.bhavani.constructions.resources";

    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public String getName() {
        return "Bhavani Constructions Portal";
    }

    @Override
    public void run(ServerConfiguration serverConfiguration, Environment environment) throws Exception {
        environment.jersey().register(SupervisorResource.class);
    }

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {
        String ORG_BHAVANI_HIBERNATE_ENTITIES = "org.bhavani.constructions.dao.entities";
        String[] entityPackageArrays = new String[]{ORG_BHAVANI_HIBERNATE_ENTITIES};

        final HibernateBundle<ServerConfiguration> hibernateBundle = new
                ScanningHibernateBundle<ServerConfiguration>(entityPackageArrays, new SessionFactoryFactory()) {
                    @Override
                    public PooledDataSourceFactory getDataSourceFactory(ServerConfiguration serverConfiguration) {
                        return serverConfiguration.getDataSourceFactory();
                    }
                };

        final GuiceBundle<ServerConfiguration> guiceBundle = GuiceBundle.<ServerConfiguration>builder()
                .modules(new ServerModule(hibernateBundle))
                .build();

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(guiceBundle);

    }
}
