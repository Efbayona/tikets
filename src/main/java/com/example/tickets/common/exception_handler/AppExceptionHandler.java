package com.example.tickets.common.exception_handler;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
/**
 * Clase que maneja las excepciones de la aplicación y proporciona respuestas de error adecuadas.
 */
public class AppExceptionHandler {

    private final HttpSession httpSession;

    public AppExceptionHandler(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    /**
     * Maneja la excepción de recurso no encontrado.
     *
     * @param exception la excepción ResourceNotFoundException
     * @return un mapa con el mensaje de error y el mensaje de la excepción
     */
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("error", "Not found");
        map.put("message", exception.getMessage());
        return map;
    }

}
