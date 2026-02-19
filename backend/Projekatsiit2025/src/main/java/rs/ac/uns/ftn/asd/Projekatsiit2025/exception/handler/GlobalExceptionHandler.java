package rs.ac.uns.ftn.asd.Projekatsiit2025.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ErrorResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.EmailAlreadyExistsException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.NoEligibleDriverException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.RouteAlreadyAddedException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.UserNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.LinkedPassengerNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(NoEligibleDriverException.class)
	public ResponseEntity<ErrorResponse> handleNoEligibleDriver(NoEligibleDriverException ex){
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.CONFLICT.value(),
				LocalDateTime.now()
			);
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(RouteAlreadyAddedException.class)
	public ResponseEntity<ErrorResponse> handleRouteAlreadyAdded(RouteAlreadyAddedException ex){
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.CONFLICT.value(),
				LocalDateTime.now()
			);
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(LinkedPassengerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLinkedPassengerNotFound(LinkedPassengerNotFoundException ex){
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				HttpStatus.NOT_FOUND.value(),
				LocalDateTime.now()
			);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleException(ResponseStatusException ex){
		ErrorResponse error = new ErrorResponse(
				ex.getMessage(),
				ex.getStatusCode().value(),
				LocalDateTime.now()
			);
		return new ResponseEntity<>(error, ex.getStatusCode());
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
    	  ex.printStackTrace();
        ErrorResponse error = new ErrorResponse(
                "Unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
