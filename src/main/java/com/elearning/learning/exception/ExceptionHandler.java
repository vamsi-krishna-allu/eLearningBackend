package com.elearning.learning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHandle(Exception exception) {
		if(exception.getMessage()!=null)
			return new ResponseEntity<String>(exception.getMessage(),HttpStatus.NOT_FOUND);
		return new ResponseEntity<String>("There is some error, please contact to support@nobookslearning.com",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> usernameNotFound(UsernameNotFoundException exception) {
		return new ResponseEntity<String>("UserName or Password is incorrect",HttpStatus.NOT_FOUND);	
	}
	 
	@org.springframework.web.bind.annotation.ExceptionHandler(PaymentFailureException.class)
	public ResponseEntity<String> paymentFailure(PaymentFailureException exception) {
		return new ResponseEntity<String>("Payment got failed, if your money debited please contact to support@nobookslearning.com",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(EmailValidationException.class)
	public ResponseEntity<String> emailValidationFailure(EmailValidationException exception) {
		return new ResponseEntity<String>("Email validation failed, please enter a valid email",HttpStatus.NOT_FOUND);
	}
}
