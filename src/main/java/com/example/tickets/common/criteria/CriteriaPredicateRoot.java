package com.example.tickets.common.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class CriteriaPredicateRoot<T> {
    private final CriteriaBuilder builder;
    private final Map<FilterOperator, BiFunction<Filter, Root<T>, Predicate>> predicateTransformers = new HashMap<>();

    /**
     * Crea una instancia de CriteriaPredicateRoot con el CriteriaBuilder especificado.
     *
     * @param builder el CriteriaBuilder utilizado para generar los predicados
     */
    public CriteriaPredicateRoot(CriteriaBuilder builder) {
        this.builder = builder;
        predicateTransformers.put(FilterOperator.EQUAL, CriteriaPredicateRoot.this::equalsPredicateTransformer);
        predicateTransformers.put(FilterOperator.NOT_EQUAL, CriteriaPredicateRoot.this::notEqualsPredicateTransformer);
        predicateTransformers.put(FilterOperator.GT, CriteriaPredicateRoot.this::greaterThanPredicateTransformer);
        predicateTransformers.put(FilterOperator.GT_OR_EQUAL, CriteriaPredicateRoot.this::greaterThanOrEqualPredicateTransformer);
        predicateTransformers.put(FilterOperator.LT, CriteriaPredicateRoot.this::lowerThanPredicateTransformer);
        predicateTransformers.put(FilterOperator.LT_OR_EQUAL, CriteriaPredicateRoot.this::lowerThanOrEqualPredicateTransformer);
        predicateTransformers.put(FilterOperator.CONTAINS, CriteriaPredicateRoot.this::containsPredicateTransformer);
        predicateTransformers.put(FilterOperator.NOT_CONTAINS, CriteriaPredicateRoot.this::notContainsPredicateTransformer);
        predicateTransformers.put(FilterOperator.IS_TRUE, CriteriaPredicateRoot.this::isTruePredicateTransformer);
        predicateTransformers.put(FilterOperator.IS_FALSE, CriteriaPredicateRoot.this::isFalsePredicateTransformer);
        predicateTransformers.put(FilterOperator.IN, CriteriaPredicateRoot.this::inPredicateTransformer);
    }

    /**
     * Formatea la lista de filtros en una lista de predicados correspondientes.
     *
     * @param filters los filtros a aplicar
     * @param root    el objeto Root utilizado para los predicados
     * @return la lista de predicados resultante
     */
    public List<Predicate> format(List<Filter> filters, Root<T> root) {
        return filters.stream()
                .map(filter -> formatPredicate(filter, root))
                .collect(Collectors.toList());
    }

    /**
     * Aplica el formato a un filtro y lo convierte en un predicado correspondiente.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado resultante
     */
    private Predicate formatPredicate(Filter filter, Root<T> root) {
        BiFunction<Filter, Root<T>, Predicate> transformer = predicateTransformers.get(filter.operator());

        return transformer.apply(filter, root);
    }

    /**
     * Crea un predicado de igualdad para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de igualdad generado
     */
    private Predicate equalsPredicateTransformer(Filter filter, Root<T> root) {
        return builder.equal(root.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Crea un predicado de desigualdad para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de desigualdad generado
     */
    private Predicate notEqualsPredicateTransformer(Filter filter, Root<T> root) {
        return builder.notEqual(root.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Crea un predicado de mayor que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de mayor que generado
     */
    private Predicate greaterThanPredicateTransformer(Filter filter, Root<T> root) {
        return builder.greaterThan(root.get(filter.field().value()).as(String.class), filter.value().value());
    }


    /**
     * Crea un predicado de mayor o igual que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de mayor o igual que generado
     */
    private Predicate greaterThanOrEqualPredicateTransformer(Filter filter, Root<T> root) {
        return builder.greaterThanOrEqualTo(root.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Crea un predicado de menor que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de menor que generado
     */
    private Predicate lowerThanPredicateTransformer(Filter filter, Root<T> root) {
        return builder.lessThan(root.get(filter.field().value()).as(String.class), filter.value().value());
    }

    /**
     * Crea un predicado de menor o igual que para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de menor o igual que generado
     */
    private Predicate lowerThanOrEqualPredicateTransformer(Filter filter, Root<T> root) {
        return builder.lessThanOrEqualTo(root.get(filter.field().value()).as(String.class), filter.value().value());
    }


    /**
     * Crea un predicado de "contiene" para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de "contiene" generado
     */
    private Predicate containsPredicateTransformer(Filter filter, Root<T> root) {
        return builder.like(
                builder.lower(root.get(filter.field().value())),
                builder.lower(builder.literal(String.format("%%%s%%", filter.value().value())))
        );
    }

    /**
     * Crea un predicado de "no contiene" para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de "no contiene" generado
     */
    private Predicate notContainsPredicateTransformer(Filter filter, Root<T> root) {
        return builder.notLike(root.get(filter.field().value()).as(String.class), String.format("%%%s%%", filter.value().value()));
    }

    /**
     * Crea un predicado de "es verdadero" para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de "es verdadero" generado
     */
    private Predicate isTruePredicateTransformer(Filter filter, Root<T> root) {
        return builder.isTrue(root.get(filter.field().value()));
    }


    /**
     * Crea un predicado de "es falso" para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de "es falso" generado
     */
    private Predicate isFalsePredicateTransformer(Filter filter, Root<T> root) {
        return builder.isFalse(root.get(filter.field().value()));
    }

    /**
     * Crea un predicado de "en" para el filtro especificado.
     *
     * @param filter el filtro a aplicar
     * @param root   el objeto Root utilizado para el predicado
     * @return el predicado de "en" generado
     */
    private Predicate inPredicateTransformer(Filter filter, Root<T> root) {
        CriteriaBuilder.In<String> inPredicate = builder.in(root.get(filter.field().value()).as(String.class));
        String[] fieldSplit = filter.value().value().split(",");
        for (String field : fieldSplit) {
            inPredicate.value(field);
        }
        return inPredicate;
    }

}
