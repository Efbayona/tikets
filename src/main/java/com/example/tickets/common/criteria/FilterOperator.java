package com.example.tickets.common.criteria;

/**
 * Enumeración que representa los operadores de filtro utilizados en los criterios de búsqueda.
 */
public enum FilterOperator {
    EQUAL("="),
    NOT_EQUAL("!="),
    GT(">"),
    GT_OR_EQUAL(">="),
    LT("<"),
    LT_OR_EQUAL("<="),
    CONTAINS("CONTAINS"),
    NOT_CONTAINS("NOT_CONTAINS"),
    IS_TRUE("TRUE"),
    IS_FALSE("FALSE"),
    IN("IN");

    private final String operator;

    /**
     * Constructor de FilterOperator.
     *
     * @param operator el operador del filtro
     */
    FilterOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Obtiene el operador de filtro a partir del valor especificado.
     *
     * @param value el valor del operador
     * @return el operador de filtro correspondiente
     * @throws IllegalArgumentException si el valor especificado no es compatible
     */
    public static FilterOperator fromValue(String value) {
        return switch (value) {
            case "=" -> FilterOperator.EQUAL;
            case "!=" -> FilterOperator.NOT_EQUAL;
            case ">" -> FilterOperator.GT;
            case ">=" -> FilterOperator.GT_OR_EQUAL;
            case "<" -> FilterOperator.LT;
            case "<=" -> FilterOperator.LT_OR_EQUAL;
            case "CONTAINS" -> FilterOperator.CONTAINS;
            case "NOT_CONTAINS" -> FilterOperator.NOT_CONTAINS;
            case "TRUE" -> FilterOperator.IS_TRUE;
            case "FALSE" -> FilterOperator.IS_FALSE;
            case "IN" -> FilterOperator.IN;
            default -> null;
        };
    }

    /**
     * Verifica si el operador de filtro es positivo.
     *
     * @return true si el operador es positivo, false de lo contrario
     */
    public boolean isPositive() {
        return this != NOT_EQUAL && this != NOT_CONTAINS;
    }

    /**
     * Obtiene el valor del operador de filtro.
     *
     * @return el valor del operador
     */
    public String value() {
        return operator;
    }
}
