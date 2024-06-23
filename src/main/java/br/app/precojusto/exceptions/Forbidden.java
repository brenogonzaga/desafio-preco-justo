package br.app.precojusto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Forbidden extends Exception {

    public Forbidden(String message) {
        super(message);
    }
}
