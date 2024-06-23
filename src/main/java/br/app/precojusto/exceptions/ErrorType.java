package br.app.precojusto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    NOT_FOUND("NOT_FOUND", "Resource not found"),
    FORBIDDEN("FORBIDDEN", "Access denied"),
    UNAUTHORIZED_CLIENT("UNAUTHORIZED_CLIENT", "Unauthorized client"),
    BAD_REQUEST("BAD_REQUEST", "Bad request"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

    private String errorType;
    private String description;
}
