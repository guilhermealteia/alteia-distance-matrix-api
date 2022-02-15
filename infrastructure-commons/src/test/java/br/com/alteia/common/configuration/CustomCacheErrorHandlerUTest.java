package br.com.alteia.common.configuration;

import br.com.alteia.common.adapters.handlers.CustomCacheErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class CustomCacheErrorHandlerUTest {

    @Mock
    Cache cache;

    CustomCacheErrorHandler customCacheErrorHandler = new CustomCacheErrorHandler();

    @Test
    void handleCacheGetError() {
        assertDoesNotThrow(() -> customCacheErrorHandler.handleCacheGetError(new RuntimeException(""), cache, "Object"));
    }

    @Test
    void handleCacheClearError() {
        assertDoesNotThrow(() -> customCacheErrorHandler.handleCacheClearError(new RuntimeException(""), cache));
    }

    @Test
    void handleCacheEvictError() {
        assertDoesNotThrow(() -> customCacheErrorHandler.handleCacheEvictError(new RuntimeException(""), cache, "Object"));
    }

    @Test
    void handleCachePutError() {
        assertDoesNotThrow(() -> customCacheErrorHandler.handleCachePutError(new RuntimeException(""), cache, "Object1", "Object2"));
    }
}
