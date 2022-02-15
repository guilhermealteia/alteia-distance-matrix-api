package br.com.alteia.common.configuration;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ValidationConfig {

    @Inject
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}
