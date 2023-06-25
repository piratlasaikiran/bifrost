package org.bhavani.constructions;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.inject.ServerModule;
import org.bhavani.constructions.resources.*;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import ru.vyarus.dropwizard.guice.GuiceBundle;


public class Server extends Application<ServerConfiguration> {

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
        environment.jersey().register(DriverResource.class);
        environment.jersey().register(VehicleResource.class);
        environment.jersey().register(SiteResource.class);
        environment.jersey().register(AssetLocationResource.class);
        environment.jersey().register(UserResource.class);
        environment.jersey().register(VendorResource.class);
        environment.jersey().register(BankAccountResource.class);
        environment.jersey().register(TransactionResource.class);
        environment.jersey().register(VendorAttendanceResource.class);
        environment.jersey().register(EmployeeAttendanceResource.class);

        environment.jersey().register(MultiPartFeature.class);
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

        final SwaggerBundle<ServerConfiguration> swaggerBundle = new SwaggerBundle<ServerConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ServerConfiguration configuration) {
                SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();
                swaggerBundleConfiguration.setResourcePackage(DriverResource.class.getPackage().getName());
                swaggerBundleConfiguration.setResourcePackage(SupervisorResource.class.getPackage().getName());
                return swaggerBundleConfiguration;
            }
        };

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(swaggerBundle);
        bootstrap.addBundle(new MultiPartBundle());
    }
}
