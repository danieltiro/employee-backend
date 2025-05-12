package com.siscon.demo.employee.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ServiceException extends RuntimeException {
	
    private static final long serialVersionUID = -5642266379346466425L;

	public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, ExceptionUtils.getRootCause(cause));
    }
}