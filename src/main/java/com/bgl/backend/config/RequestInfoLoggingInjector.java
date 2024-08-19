package com.bgl.backend.config;

import com.bgl.backend.common.logging.LoggerWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Log request and response info for each http request.
 */
@Component
public class RequestInfoLoggingInjector implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestInfoLoggingInjector.class);

    @Autowired
    private transient LoggerWrapper logger;

    /**
     * Log request info.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler handler
     * @return true
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        // Log request details

        final StringBuilder logMessage = new StringBuilder(256);

        logMessage.append("Request URI: ").append(request.getRequestURI())
            .append("\nRequest Method: ").append(request.getMethod())
            .append("\nRequest Query String: ").append(request.getQueryString())
            .append("\nRequest Remote User: ").append(request.getRemoteUser());

        logger.info(LOG, logMessage.toString());

        return true;
    }

    /**
     * Log response info.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler handler
     * @param ex exception
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final Exception ex) {
        // Log response status and any exceptions
        logger.info(LOG, "Response Status: {}", response.getStatus());
        if (ex != null) {
            logger.logErrorWithException(LOG, "Request to " + request.getRequestURI() + " resulted in an exception",
                ex);
        }
    }

}
