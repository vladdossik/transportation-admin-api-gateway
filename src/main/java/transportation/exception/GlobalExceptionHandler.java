package transportation.exception;

import exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientRequestException.class)
    ResponseEntity handleAllExceptions(WebClientRequestException exception) {
        ErrorResponse exceptionResponse = new ErrorResponse(LocalDate.now(), exception.getCause().toString(), exception.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(WebClientResponseException.class)
    ResponseEntity handleAllExceptions(WebClientResponseException exception) {
        ErrorResponse exceptionResponse = new ErrorResponse(LocalDate.now(), exception.getStatusText(), exception.getMessage());

        return new ResponseEntity<>(exceptionResponse, exception.getStatusCode());
    }
}
