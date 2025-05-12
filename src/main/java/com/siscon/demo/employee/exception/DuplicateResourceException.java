package com.siscon.demo.employee.exception;

import org.hibernate.service.spi.ServiceException;

public class DuplicateResourceException extends ServiceException {
	
    private static final long serialVersionUID = 4223173110610785940L;

	public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
