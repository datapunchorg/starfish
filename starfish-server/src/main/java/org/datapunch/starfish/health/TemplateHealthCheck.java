package org.datapunch.starfish.health;

import com.codahale.metrics.health.HealthCheck;

public class TemplateHealthCheck extends HealthCheck {
    private final String value;

    public TemplateHealthCheck(String value) {
        this.value = value;
    }

    @Override
    protected Result check() {
        if (value == null) {
            return Result.unhealthy("value is null");
        }
        return Result.healthy();
    }
}
