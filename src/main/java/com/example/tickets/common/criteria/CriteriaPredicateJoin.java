package com.example.tickets.common.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Clase que representa un predicado de criterios para realizar consultas con joins en JPA.
 */
@Slf4j
public class CriteriaPredicateJoin {
    private final CriteriaBuilder builder;
    private final Map<FilterOperator, BiFunction<Filter, Join<?, ?>, Predicate>> predicateTransformers = new HashMap<>();

    /**
     * Constructor de la clase CriteriaPredicateJoin.
     *
     * @param builder el objeto CriteriaBuilder utilizado para construir predicados
     */
    public CriteriaPredicateJoin(CriteriaBuilder builder) {
        this.builder = builder;
        predicateTransformers.put(FilterOperator.EQUAL, CriteriaPredicateJoin.this::equalsPredicateTransformer);
        predicateTransformers.put(FilterOperator.NOT_EQUAL, CriteriaPredicateJoin.this::notEqualsPredicateTransformer);
        predicateTransformers.put(FilterOperator.GT, CriteriaPredicateJoin.this::greaterThanPredicateTransformer);
        predicateTransformers.put(FilterOperator.GT_OR_EQUAL, CriteriaPredicateJoin.this::greaterThanOrEqualPredicateTransformer);
        predicateTransformers.put(FilterOperator.LT, CriteriaPredicateJoin.this::lowerThanPredicateTransformer);
        predicateTransformers.put(FilterOperator.LT_OR_EQUAL, CriteriaPredicateJoin.this::lowerThanOrEqualPredicateTransformer);
        predicateTransformers.put(FilterOperator.CONTAINS, CriteriaPredicateJoin.this::containsPredicateTransformer);
        predicateTransformers.put(FilterOperator.NOT_CONTAINS, CriteriaPredicateJoin.this::notContainsPredicateTransformer);
        predicateTransformers.put(FilterOperator.IS_TRUE, CriteriaPredicateJoin.this::isTruePredicateTransformer);
        predicateTransformers.put(FilterOperator.IS_FALSE, CriteriaPredicateJoin.this::isFalsePredicateTransformer);
        predicateTransformers.put(FilterOperator.IN, CriteriaPredicateJoin.this::inPredicateTransformer);
    }

    /**
     * Formatea el filtro y realiza la transformación correspondiente para generar el predicado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado resultante después de aplicar la transformación
     */
    public Predicate format(Filter filter, Join<?, ?> join) {
        BiFunction<Filter, Join<?, ?>, Predicate> transformer = predicateTransformers.get(filter.operator());

        return transformer.apply(filter, join);
    }

    /**
     * Genera un predicado de igualdad para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de igualdad resultante
     */
    private Predicate equalsPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.equal(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de desigualdad para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de desigualdad resultante
     */
    private Predicate notEqualsPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.notEqual(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de mayor que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de mayor que resultante
     */
    private Predicate greaterThanPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.greaterThan(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de mayor o igual que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de mayor o igual que resultante
     */
    private Predicate greaterThanOrEqualPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.greaterThanOrEqualTo(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de menor que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de menor que resultante
     */
    private Predicate lowerThanPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.lessThan(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de menor o igual que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de menor o igual que resultante
     */
    private Predicate lowerThanOrEqualPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.lessThanOrEqualTo(join.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Genera un predicado de contención para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de contención resultante
     */
    private Predicate containsPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.like(join.get(filter.field().value()).as(String.class), String.format("%%%s%%", filter.value().value()));
    }

    /**
     * Genera un predicado de no contención para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de no contención resultante
     */
    private Predicate notContainsPredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.notLike(join.get(filter.field().value()).as(String.class), String.format("%%%s%%", filter.value().value()));
    }

    /**
     * Genera un predicado de verdadero para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de verdadero resultante
     */
    private Predicate isTruePredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.isTrue(join.get(filter.field().value()));
    }

    /**
     * Genera un predicado de falso para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de falso resultante
     */
    private Predicate isFalsePredicateTransformer(Filter filter, Join<?, ?> join) {
        return builder.isFalse(join.get(filter.field().value()));
    }

    /**
     * Genera un predicado de pertenencia para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param join   el objeto Join utilizado para el predicado
     * @return el predicado de pertenencia resultante
     */
    private Predicate inPredicateTransformer(Filter filter, Join<?, ?> join) {
        CriteriaBuilder.In<String> inPredicate = builder.in(join.get(filter.field().value()).as(String.class));
        String[] fieldSplit = filter.value().value().split(",");
        for (String field : fieldSplit) {
            inPredicate.value(field);
        }
        return inPredicate;
    }
}
