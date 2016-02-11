package com.wheretoeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EmptyLocationException extends RuntimeException{
	private static final long serialVersionUID = -8574961696857449596L;
	
	
	public EmptyLocationException(String message) {
		super(message);		
	}
}
