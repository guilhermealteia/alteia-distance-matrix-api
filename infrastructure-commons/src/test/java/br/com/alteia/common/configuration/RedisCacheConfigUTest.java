package br.com.alteia.common.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RedisCacheConfigUTest {

    @Test
    void init() {
        RedisCacheConfig redisCacheConfig = new RedisCacheConfig();
        assertNotNull(redisCacheConfig);
        assertNotNull(redisCacheConfig.errorHandler());
    }

}
