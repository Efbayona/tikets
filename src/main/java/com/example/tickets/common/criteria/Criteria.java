package com.example.tickets.common.criteria;

import java.util.Optional;

/**
 * Clase que representa los criterios de búsqueda.
 */
public final class Criteria {

    private final Filters filters;
    private final Order order;
    private final Optional<Integer> limit;
    private final Optional<Integer> offset;

    /**
     * Constructor de Criteria.
     *
     * @param filters Los filtros de búsqueda.
     * @param order   El orden de los resultados.
     * @param limit   El límite de resultados por página.
     * @param offset  El desplazamiento de resultados.
     */
    public Criteria(Filters filters, Order order, Optional<Integer> limit, Optional<Integer> offset) {
        this.filters = filters;
        this.order = order;
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * Constructor de Criteria.
     *
     * @param filters Los filtros de búsqueda.
     * @param order   El orden de los resultados.
     */
    public Criteria(Filters filters, Order order) {
        this.filters = filters;
        this.order = order;
        this.limit = Optional.empty();
        this.offset = Optional.empty();
    }

    /**
     * Obtiene los filtros de búsqueda.
     *
     * @return Los filtros de búsqueda.
     */
    public Filters filters() {
        return filters;
    }

    /**
     * Obtiene el orden de los resultados.
     *
     * @return El orden de los resultados.
     */
    public Order order() {
        return order;
    }

    /**
     * Obtiene el límite de resultados por página.
     *
     * @return El límite de resultados por página.
     */
    public Optional<Integer> limit() {
        return limit;
    }

    /**
     * Obtiene el desplazamiento de resultados.
     *
     * @return El desplazamiento de resultados.
     */
    public Optional<Integer> offset() {
        return offset;
    }

    /**
     * Verifica si existen filtros de búsqueda.
     *
     * @return true si existen filtros, false de lo contrario.
     */
    public boolean hasFilters() {
        return filters.filters().size() > 0;
    }

    /**
     * Serializa los criterios en una cadena de texto.
     *
     * @return La cadena de texto que representa los criterios.
     */
    public String serialize() {
        return String.format(
                "%s~~%s~~%s~~%s",
                filters.serialize(),
                order.serialize(),
                offset.orElse(0),
                limit.orElse(0)
        );
    }
}
