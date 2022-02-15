package br.com.alteia.common.adapters.handlers;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggableDispatcherServletUTest {

    @Mock
    private OpenTelemetry openTelemetry;

    @Mock
    private Tracer tracer;

    @Mock
    private SpanBuilder spanBuilder;

    @Mock
    private Span span;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ContentCachingRequestWrapper contentCachingRequestWrapper;

    @Mock
    private ContentCachingResponseWrapper contentCachingResponseWrapper;

    @Test
    void doDispatch() throws Exception {
        when(request.getRequestURI()).thenReturn("uri/exemplo");
        when(openTelemetry.getTracer(anyString())).thenReturn(tracer);
        when(tracer.spanBuilder(anyString())).thenReturn(spanBuilder);
        when(spanBuilder.startSpan()).thenReturn(span);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.singleton("header1")));

        LoggableDispatcherServlet loggableDispatcherServlet = new LoggableDispatcherServlet(openTelemetry);
        assertDoesNotThrow(() -> loggableDispatcherServlet.doDispatch(request, response));
    }

    @Test
    void doDispatchWithOtherServletRequestAndResponse() throws Exception {
        when(contentCachingRequestWrapper.getRequestURI()).thenReturn("uri/exemplo");
        when(openTelemetry.getTracer(anyString())).thenReturn(tracer);
        when(tracer.spanBuilder(anyString())).thenReturn(spanBuilder);
        when(spanBuilder.startSpan()).thenReturn(span);
        when(contentCachingRequestWrapper.getHeaderNames()).thenReturn(Collections.enumeration(Collections.singleton("header1")));

        when(contentCachingRequestWrapper.getContentAsByteArray()).thenReturn("Payload de request".getBytes());
        when(contentCachingResponseWrapper.getContentAsByteArray()).thenReturn("Payload de response".getBytes());

        when(contentCachingRequestWrapper.getCharacterEncoding()).thenReturn("UTF-8");
        when(contentCachingResponseWrapper.getCharacterEncoding()).thenReturn("UTF-8");

        LoggableDispatcherServlet loggableDispatcherServlet = new LoggableDispatcherServlet(openTelemetry);
        assertDoesNotThrow(() -> loggableDispatcherServlet.doDispatch(contentCachingRequestWrapper, contentCachingResponseWrapper));
    }
}
