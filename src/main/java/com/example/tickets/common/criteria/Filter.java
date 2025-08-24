package com.example.tickets.common.criteria;

import java.util.HashMap;

/**
 * Clase que representa un filtro utilizado en los criterios de búsqueda.
 */
public final class Filter {
    private final FilterField field;
    private final FilterOperator operator;
    private final FilterValue value;

    /**
     * Constructor de la clase Filter.
     *
     * @param field    el campo del filtro
     * @param operator el operador del filtro
     * @param value    el valor del filtro
     */
    public Filter(FilterField field, FilterOperator operator, FilterValue value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Crea un objeto Filter a partir de los valores especificados.
     *
     * @param field    el campo del filtro
     * @param operator el operador del filtro
     * @param value    el valor del filtro
     * @return el objeto Filter creado
     */
    public static Filter create(String field, String operator, String value) {
        return new Filter(
                new FilterField(field),
                FilterOperator.fromValue(operator.toUpperCase()),
                new FilterValue(value)
        );
    }

    /**
     * Crea un objeto Filter a partir de un mapa de valores.
     *
     * @param values el mapa de valores que contiene el campo, el operador y el valor del filtro
     * @return el objeto Filter creado
     */
    public static Filter fromValues(HashMap<String, String> values) {
        return new Filter(
                new FilterField(values.get("field")),
                FilterOperator.fromValue(values.get("operator")),
                new FilterValue(values.get("value"))
        );
    }

    /**
     * Devuelve el campo del filtro.
     *
     * @return el campo del filtro
     */
    public FilterField field() {
        return field;
    }

    /**
     * Devuelve el operador del filtro.
     *
     * @return el operador del filtro
     */
    public FilterOperator operator() {
        return operator;
    }

    /**
     * Devuelve el valor del filtro.
     *
     * @return el valor del filtro
     */
    public FilterValue value() {
        return value;
    }

    /**
     * Serializa el filtro en un formato de cadena.
     *
     * @return la representación en cadena del filtro
     */
    public String serialize() {
        return String.format("%s.%s.%s", field.value(), operator.value(), value.value());
    }
}
