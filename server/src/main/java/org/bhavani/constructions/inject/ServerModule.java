package org.bhavani.constructions.inject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import lombok.RequiredArgsConstructor;
import org.bhavani.constructions.config.ServerConfiguration;
import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

@RequiredArgsConstructor
public class ServerModule extends DropwizardAwareModule<ServerConfiguration> {

    private final HibernateBundle<ServerConfiguration> hibernateBundle;

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    protected SessionFactory providesSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
