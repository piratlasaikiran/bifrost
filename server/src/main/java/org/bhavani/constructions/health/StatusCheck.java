package org.bhavani.constructions.health;

import com.codahale.metrics.health.HealthCheck;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck;

import javax.inject.Inject;

public class StatusCheck extends NamedHealthCheck {
    private final RotationManager manager;

    @Inject
    public StatusCheck(RotationManager manager) {
        this.manager = manager;
    }

    @Override
    public String getName() {
        return "rotation-status";
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        if (manager.status()) {
            return HealthCheck.Result.healthy();
        } else {
            return HealthCheck.Result.unhealthy("Taken out of rotation");
        }
    }
}
