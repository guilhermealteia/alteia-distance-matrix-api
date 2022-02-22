package br.com.alteia.common.adapters.handlers;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    private static final Logger LOG = Logger.getLogger(CustomCacheErrorHandler.class.getName());

    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        String message = format("handlingCacheGetError for cacheName: %s, Object hashCode: %s. Message: %s", cache.getName(), o.hashCode(), e.getMessage());
        LOG.log(SEVERE, message);
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        var o1Opt = Optional.ofNullable(o1);
        Integer o1HashCode = o1Opt.isPresent() ? o1Opt.get().hashCode() : null;
        String message = format("handlingCachePutError for cacheName: %s, Object1 hashCode: %s, Object2 hashCode: %s. Message: %s", cache.getName(), o.hashCode(), o1HashCode, e.getMessage());
        LOG.log(SEVERE, message);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        String message = format("handlingCacheEvictError for cacheName: %s, Object hashCode: %s. Message: %s", cache.getName(), o.hashCode(), e.getMessage());
        LOG.log(SEVERE, message);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        String message = format("handlingCacheClearError for cacheName: %s. Message: %s", cache.getName(), e.getMessage());
        LOG.log(SEVERE, message);
    }
}