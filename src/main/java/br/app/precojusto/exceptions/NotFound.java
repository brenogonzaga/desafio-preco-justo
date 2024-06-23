package br.app.precojusto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFound extends Exception {

    private static final String DEFAULT_MSG = "Recurso não encontrado!";

    public NotFound() {
        super(DEFAULT_MSG);
    }

    public NotFound(String message) {
        super(message);
    }
}
