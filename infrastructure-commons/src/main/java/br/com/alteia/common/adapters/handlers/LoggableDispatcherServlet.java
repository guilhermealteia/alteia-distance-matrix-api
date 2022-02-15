package br.com.alteia.common.adapters.handlers;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

public class LoggableDispatcherServlet extends DispatcherServlet {

    private static final Logger LOG = Logger.getLogger(LoggableDispatcherServlet.class.getName());
    private static final String PATH_FILE_LOGGING_CONFIGS = "/logging-config.properties";//NOSONAR
    private static final Map<String, String> loggingConfigurations = new HashMap<>();
    private static final List<String> loggableHeaders = new ArrayList<>();
    private static final List<String> notLoggableEndpoints = new ArrayList<>();
    public static final String MAX_LOG_LENGHT = "maxLogLenght";
    public static final String LOGGABLE_HEADERS = "loggableHeaders";
    public static final String NOT_LOGGABLE_ENDPOINTS = "notLoggableEndpoints";
    private static final String SPLIT_REGEX = ",";
    private static final String UUID_REGEX = "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";
    public static final String REQUEST_ID_LOG_FIELD = "requestId";
    public static final String EMPTY = "[]";

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(PATH_FILE_LOGGING_CONFIGS));
            loggingConfigurations.putAll(
                    properties.entrySet().stream().collect(Collectors.toMap(
                            i -> i.getKey().toString(),
                            i -> i.getValue().toString()))
            );

            loggableHeaders.addAll(Arrays.asList(loggingConfigurations.get(LOGGABLE_HEADERS).toLowerCase().split(SPLIT_REGEX)));
            notLoggableEndpoints.addAll(Arrays.asList(loggingConfigurations.get(NOT_LOGGABLE_ENDPOINTS).toLowerCase().split(SPLIT_REGEX)));

        } catch (IOException e) {
            LOG.log(SEVERE, String.format("Error when trying to load %s config file", PATH_FILE_LOGGING_CONFIGS), e);
        }
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestUuid = UUID.randomUUID().toString();
        ThreadContext.put(REQUEST_ID_LOG_FIELD, requestUuid);

        // Generate a span
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        try {
            super.doDispatch(request, response);
        } catch (IOException | ServletException ex) {
            LOG.log(SEVERE, "Error when processing the request", ex);
        } finally {
            String requestHeaders = getRequestHeaders(request);

            String requestUri = request.getRequestURI();
            if (notLoggableEndpoints.stream().noneMatch(requestUri::contains)) {
                String callLog = new StringBuilder(6)
                        .append("Request headers: ")
                        .append(requestHeaders)
                        .append("Request: ")
                        .append(getRequestPayload(request))
                        .append("Response: ")
                        .append(getResponsePayload(response)).toString();
                LOG.log(INFO, callLog);
            }
            updateResponse(response);
        }
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (responseWrapper != null) {
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getRequestHeaders(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            StringBuilder requestLog = new StringBuilder();

            requestLog.append("Headers [");
            wrapper.getHeaderNames().asIterator().forEachRemaining(f -> {
                if (loggableHeaders.contains(f.toLowerCase())) {
                    requestLog.append(f);
                    requestLog.append(":");
                    requestLog.append(wrapper.getHeader(f));
                    requestLog.append(", ");
                }
            });
            requestLog.append("] ");
            return requestLog.toString();
        }
        return "Headers [] ";
    }

    private String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            StringBuilder requestLog = new StringBuilder();
            requestLog.append("Payload ");
            byte[] buf = wrapper.getContentAsByteArray();
            requestLog.append(Optional.ofNullable(extractBuffer(wrapper.getCharacterEncoding(), buf)).orElse(EMPTY));
            requestLog.append(" ");
            return requestLog.toString();
        }
        return EMPTY;
    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            return Optional.ofNullable(extractBuffer(wrapper.getCharacterEncoding(), buf)).orElse(EMPTY);
        }
        return EMPTY;
    }

    private String extractBuffer(String characterEncoding, byte[] buf) {
        if (buf.length > 0) {
            int length = Math.min(buf.length, Integer.parseInt(loggingConfigurations.get(MAX_LOG_LENGHT)));
            try {
                return new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                LOG.log(SEVERE, "Error when trying to extract buffer from payload");
            }
        }
        return null;
    }
}