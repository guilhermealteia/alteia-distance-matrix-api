package br.com.alteia.common.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static java.lang.Boolean.TRUE;

@Configuration
public class RedisCacheCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return (context.getEnvironment().getProperty("redis.cache.enabled", Boolean.class, TRUE));
    }
}