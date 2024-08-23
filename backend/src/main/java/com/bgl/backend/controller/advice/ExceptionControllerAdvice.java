package com.bgl.backend.controller.advice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;


import com.bgl.backend.common.exception.SystemException;
import com.bgl.backend.common.logging.LoggerWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    @Autowired
    private transient LoggerWrapper logger;

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    @ExceptionHandler(ResponseStatusException.class)
    ProblemDetail handleResponseStatusExceptionException(final ResponseStatusException e) {
        final ProblemDetail problemDetail = createProblemDetail(e, e.getStatusCode());

        logger.logStandardProblemDetail(LOG, problemDetail, e);

        return problemDetail;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgumentExceptionException(final IllegalArgumentException e) {
        final ProblemDetail problemDetail = createProblemDetail(e, HttpStatus.BAD_REQUEST);

        logger.logStandardProblemDetail(LOG, problemDetail, e);

        return problemDetail;
    }

    /**
     * Global handel exception type of SystemException.
     *
     * @param e SystemException
     * @return ProblemDetail
     */
    @ExceptionHandler(SystemException.class)
    ProblemDetail handleSystemException(final SystemException e) {
        //Add Handling for SystemException

        // Log the exception
        logger.logErrorWithException(LOG, "A system error occurred: " + e.getMessage(), e);

        // Customize the ProblemDetail response
        final HttpStatusCode status = getHttpStatusCodeFromSystemException(e);
        final ProblemDetail problemDetail = createProblemDetail(e, status);

        problemDetail.setTitle("System Error");
        problemDetail.setDetail(e.getDetail());
        return problemDetail;
    }

    /**
     * Global handel exception type of Exception.
     *
     * @param e Exception
     * @return ProblemDetail
     */
    @ExceptionHandler(Exception.class)
    ProblemDetail handleMainException(final Exception e) {
        // Log the exception
        logger.logErrorWithException(LOG, "An unexpected error occurred: " + e.getMessage(), e);

        final HttpStatusCode status = getHttpStatusCodeFromException(e);
        final ProblemDetail problemDetail = createProblemDetail(e, status);

        problemDetail.setTitle("An unexpected error occurred");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }


    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            logger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
}