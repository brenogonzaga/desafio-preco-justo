package br.app.precojusto.exceptions.handler;

import br.app.precojusto.exceptions.BadRequest;
import br.app.precojusto.exceptions.ErrorResponse;
import br.app.precojusto.exceptions.ErrorType;
import br.app.precojusto.exceptions.Forbidden;
import br.app.precojusto.exceptions.NotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResponse> handlerBadRequest(BadRequest e, HttpServletRequest request) {
        var err = new ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            ErrorType.BAD_REQUEST.getErrorType(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ErrorResponse> handlerFobbiden(Forbidden e, HttpServletRequest request) {
        var err = new ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.FORBIDDEN.value(),
            ErrorType.FORBIDDEN.getErrorType(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorResponse> handlerNotFound(NotFound e, HttpServletRequest request) {
        var err = new ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.NOT_FOUND.value(),
            ErrorType.NOT_FOUND.getErrorType(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
}
