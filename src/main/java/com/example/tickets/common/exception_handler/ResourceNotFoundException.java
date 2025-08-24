package com.example.tickets.common.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando no se encuentra un recurso.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)  // 404
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor de la excepción ResourceNotFoundException.
     *
     * @param message El mensaje de error.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
