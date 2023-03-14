package org.bhavani.constructions;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.inject.ServerModule;
import org.bhavani.constructions.resources.UserResource;
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
        environment.jersey().register(UserResource.class);
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
                .modules(new MetricsInstrumentationModule(bootstrap.getMetricRegistry(),
                        new AbstractMatcher<TypeLiteral<?>>() {
                            @Override
                            public boolean matches(TypeLiteral<?> typeLiteral) {
                                return !typeLiteral.getRawType().getPackage().getName().equals(RESOURCE_PACKAGE);
                            }
                        }))
                .enableAutoConfig(getClass().getPackage().getName())
                .build();

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(guiceBundle);

    }
}
