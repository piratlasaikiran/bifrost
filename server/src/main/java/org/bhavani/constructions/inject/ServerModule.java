package org.bhavani.constructions.inject;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import lombok.RequiredArgsConstructor;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.dao.api.DriverEntityDao;
import org.bhavani.constructions.dao.api.SupervisorEntityDao;
import org.bhavani.constructions.dao.impl.DriverEntityDaoImpl;
import org.bhavani.constructions.dao.impl.SupervisorEntityDaoImpl;
import org.bhavani.constructions.serviceImpls.DefaultDriverService;
import org.bhavani.constructions.serviceImpls.DefaultSupervisorService;
import org.bhavani.constructions.services.DriverService;
import org.bhavani.constructions.services.SupervisorService;
import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

@RequiredArgsConstructor
public class ServerModule extends DropwizardAwareModule<ServerConfiguration> {

    private final HibernateBundle<ServerConfiguration> hibernateBundle;

    @Override
    protected void configure() {
        bind(SupervisorService.class).to(DefaultSupervisorService.class).in(Singleton.class);
        bind(DriverService.class).to(DefaultDriverService.class).in(Singleton.class);

        bind(SupervisorEntityDao.class).to(SupervisorEntityDaoImpl.class).in(Singleton.class);
        bind(DriverEntityDao.class).to(DriverEntityDaoImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    protected SessionFactory providesSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
