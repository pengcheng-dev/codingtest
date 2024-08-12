package com.bgl.backend.exception;

/**
 * @author Pengcheng Xiao
 *
 * simple business exception
 */
public class BusinessException extends RuntimeException{

    public BusinessException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public BusinessException(final Throwable cause) {
        super(cause);
    }

}
