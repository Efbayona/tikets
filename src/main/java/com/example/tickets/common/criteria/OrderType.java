package com.example.tickets.common.criteria;

/**
 * Enumeración que representa el tipo de orden en los criterios de búsqueda.
 */
public enum OrderType {
    ASC("asc"),
    DESC("desc"),
    NONE("none");

    private final String type;

    /**
     * Constructor de OrderType.
     *
     * @param type el tipo de orden
     */
    OrderType(String type) {
        this.type = type;
    }

    /**
     * Verifica si el tipo de orden es NONE.
     *
     * @return true si el tipo de orden es NONE, false de lo contrario
     */
    public boolean isNone() {
        return this == NONE;
    }

    /**
     * Verifica si el tipo de orden es ASC.
     *
     * @return true si el tipo de orden es ASC, false de lo contrario
     */
    public boolean isAsc() {
        return this == ASC;
    }

    /**
     * Devuelve el valor del tipo de orden.
     *
     * @return el valor del tipo de orden
     */
    public String value() {
        return type;
    }
}
