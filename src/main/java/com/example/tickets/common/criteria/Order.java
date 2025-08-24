package com.example.tickets.common.criteria;

import java.util.Optional;

/**
 * Clase que representa el ordenamiento en los criterios de búsqueda.
 */
public class Order {
    private final OrderBy orderBy;
    private final OrderType orderType;

    /**
     * Constructor de Order.
     *
     * @param orderBy    el campo por el cual se realiza el ordenamiento
     * @param orderType  el tipo de ordenamiento (ASC o DESC)
     */
    public Order(OrderBy orderBy, OrderType orderType) {
        this.orderBy = orderBy;
        this.orderType = orderType;
    }

    /**
     * Crea un objeto Order a partir de los valores proporcionados.
     *
     * @param orderBy    el campo por el cual se realiza el ordenamiento (opcional)
     * @param orderType  el tipo de ordenamiento (opcional)
     * @return un objeto Order
     */
    public static Order fromValues(Optional<String> orderBy, Optional<String> orderType) {
        return orderBy.map(order -> new Order(new OrderBy(order), OrderType.valueOf(orderType.orElse("ASC"))))
                .orElseGet(Order::none);
    }

    /**
     * Crea un objeto Order sin ningún ordenamiento.
     *
     * @return un objeto Order vacío
     */
    public static Order none() {
        return new Order(new OrderBy(""), OrderType.NONE);
    }

    /**
     * Crea un objeto Order con ordenamiento descendente.
     *
     * @param orderBy el campo por el cual se realiza el ordenamiento
     * @return un objeto Order con ordenamiento descendente
     */
    public static Order desc(String orderBy) {
        return new Order(new OrderBy(orderBy), OrderType.DESC);
    }

    /**
     * Crea un objeto Order con ordenamiento ascendente.
     *
     * @param orderBy el campo por el cual se realiza el ordenamiento
     * @return un objeto Order con ordenamiento ascendente
     */
    public static Order asc(String orderBy) {
        return new Order(new OrderBy(orderBy), OrderType.ASC);
    }

    /**
     * Devuelve el campo por el cual se realiza el ordenamiento.
     *
     * @return el campo de ordenamiento
     */
    public OrderBy orderBy() {
        return orderBy;
    }

    /**
     * Devuelve el tipo de ordenamiento.
     *
     * @return el tipo de ordenamiento (ASC o DESC)
     */
    public OrderType orderType() {
        return orderType;
    }

    /**
     * Verifica si se ha especificado un ordenamiento.
     *
     * @return true si se ha especificado un ordenamiento, false de lo contrario
     */
    public boolean hasOrder() {
        return !orderType.isNone();
    }

    /**
     * Devuelve una representación en cadena del objeto Order.
     *
     * @return la representación en cadena del objeto Order
     */
    public String serialize() {
        return String.format("%s.%s", orderBy.value(), orderType.value());
    }
}
