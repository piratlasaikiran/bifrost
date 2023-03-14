package org.bhavani.constructions.inject;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import lombok.RequiredArgsConstructor;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.dao.api.UserEntityDao;
import org.bhavani.constructions.dao.impl.UserEntityDaoImpl;
import org.bhavani.constructions.serviceImpls.DefaultUserService;
import org.bhavani.constructions.services.UserService;
import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

@RequiredArgsConstructor
public class ServerModule extends DropwizardAwareModule<ServerConfiguration> {

    private final HibernateBundle<ServerConfiguration> hibernateBundle;

    @Override
    protected void configure() {
        bind(UserService.class).to(DefaultUserService.class).in(Singleton.class);
        bind(UserEntityDao.class).to(UserEntityDaoImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    protected SessionFactory providesSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
