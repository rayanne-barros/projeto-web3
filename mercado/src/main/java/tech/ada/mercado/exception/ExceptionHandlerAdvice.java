package tech.ada.mercado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    ResponseEntity<NotFoundException> exceptionHandler(NotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundException(error.getMessage()));
    }

}
