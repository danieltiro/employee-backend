package com.siscon.demo.employee.exception;

import org.hibernate.service.spi.ServiceException;

public class ResourceNotFoundException extends ServiceException{

	private static final long serialVersionUID = -4599822963594731776L;

	public ResourceNotFoundException(String message){
        super(message);
    }
}
