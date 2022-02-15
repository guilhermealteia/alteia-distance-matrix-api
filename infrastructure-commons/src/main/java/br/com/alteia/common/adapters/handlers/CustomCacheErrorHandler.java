package br.com.alteia.common.adapters.handlers;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    private static final Logger LOG = Logger.getLogger(CustomCacheErrorHandler.class.getName());

    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        LOG.log(Level.SEVERE, format("handlingCacheGetError for cacheName: %s, Object: %s", cache.getName(), o), e);
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        LOG.log(Level.SEVERE, format("handlingCachePutError for cacheName: %s, Object1: %s, Object2: %s", cache.getName(), o, o1), e);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        LOG.log(Level.SEVERE, format("handlingCacheEvictError for cacheName: %s, Object: %s", cache.getName(), o), e);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        LOG.log(Level.SEVERE, format("handlingCacheClearError for cacheName: %s", cache.getName()), e);
    }
}