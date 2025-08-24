package com.example.tickets.common.criteria;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que representa un conjunto de filtros utilizados en los criterios de búsqueda.
 */
public final class Filters {
    private final List<Filter> filters;

    /**
     * Constructor de Filters.
     *
     * @param filters la lista de filtros
     */
    public Filters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Crea una instancia de Filters a partir de los valores especificados.
     *
     * @param filters la lista de valores de filtros
     * @return una instancia de Filters con los filtros creados
     */
    public static Filters fromValues(List<HashMap<String, String>> filters) {
        return new Filters(filters.stream().map(Filter::fromValues).collect(Collectors.toList()));
    }

    /**
     * Crea una instancia de Filters vacía, sin filtros.
     *
     * @return una instancia de Filters vacía
     */
    public static Filters none() {
        return new Filters(Collections.emptyList());
    }

    /**
     * Obtiene la lista de filtros.
     *
     * @return la lista de filtros
     */
    public List<Filter> filters() {
        return filters;
    }

    /**
     * Serializa los filtros en una representación de cadena de texto.
     *
     * @return la representación serializada de los filtros
     */
    public String serialize() {
        return filters.stream().map(Filter::serialize).collect(Collectors.joining("^"));
    }
}
