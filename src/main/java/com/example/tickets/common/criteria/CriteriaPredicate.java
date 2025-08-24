package com.example.tickets.common.criteria;

import jakarta.persistence.criteria.*;
import jakarta.persistence.criteria.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Clase utilizada para convertir los criterios de búsqueda en predicados de CriteriaQuery.
 *
 * @param <T> El tipo de entidad.
 * @param <X> El tipo de resultado.
 */
@Slf4j
public class CriteriaPredicate<T, X> {

    private final CriteriaBuilder builder;
    private Map<String, Join<?, ?>> joinMap = new HashMap<>();
    private CriteriaPredicateJoin criteriaPredicateJoin;

    /**
     * Constructor de CriteriaPredicate.
     *
     * @param builder El CriteriaBuilder utilizado para construir los predicados.
     */
    public CriteriaPredicate(CriteriaBuilder builder) {
        this.builder = builder;
    }

    /**
     * Convierte los criterios de búsqueda en predicados de CriteriaQuery.
     *
     * @param cq       El CriteriaQuery a convertir.
     * @param criteria Los criterios de búsqueda.
     * @param root     El Root de la entidad.
     * @return El CriteriaQuery con los predicados aplicados.
     */
    public CriteriaQuery<X> convert(CriteriaQuery<X> cq, Criteria criteria, Root<T> root) {

        this.getJoins(root.getJoins());

        List<Filter> filtersRoot = criteria.filters().filters().stream()
                .filter(filter -> filter.field().value().split("--").length == 1)
                .map(filter -> new Filter(new FilterField(fieldConvertName(filter.field().value())), filter.operator(), filter.value()))
                .toList();

        CriteriaPredicateRoot<T> criteriaPredicateRoot = new CriteriaPredicateRoot<>(this.builder);

        List<Predicate> predicates = criteriaPredicateRoot.format(filtersRoot, root);

        for (Filter filter : criteria.filters().filters()) {
            String[] key = filter.field().value().split("--");
            if (key.length == 2) {
                if (this.joinMap.containsKey(fieldConvertName(key[0]))){
                    criteriaPredicateJoin = new CriteriaPredicateJoin(this.builder);
                    predicates.add(criteriaPredicateJoin.format(new Filter(new FilterField(fieldConvertName(key[1])), filter.operator(), filter.value()), this.joinMap.get(fieldConvertName(key[0]))));
                }
            }
        }

        if (criteria.order().hasOrder()) {
            String[] key = criteria.order().orderBy().value().split("--");
            Path<Object> orderBy = null;
            if (key.length == 2) {
                if (this.joinMap.containsKey(fieldConvertName(key[0]))){
                    orderBy = this.joinMap.get(fieldConvertName(key[0])).get(fieldConvertName(key[1]));
                }
            }else {
                orderBy = root.get(fieldConvertName(criteria.order().orderBy().value()));
            }

            Order order = criteria.order().orderType().isAsc() ? this.builder.asc(orderBy) : this.builder.desc(orderBy);

            cq.orderBy(order);
        }

        return cq.where(predicates.toArray(new Predicate[0]));
    }

    /**
     * Convierte los criterios de búsqueda y búsqueda adicional en predicados de CriteriaQuery.
     *
     * @param cq       El CriteriaQuery a convertir.
     * @param criteria Los criterios de búsqueda.
     * @param root     El Root de la entidad.
     * @param search   El predicado adicional de búsqueda.
     * @return El CriteriaQuery con los predicados aplicados.
     */

    public CriteriaQuery<X> convertSearchAndFilters(CriteriaQuery<X> cq, Criteria criteria, Root<T> root, Predicate search) {

        this.getJoins(root.getJoins());

        List<Filter> filtersRoot = criteria.filters().filters().stream()
                .filter(filter -> filter.field().value().split("--").length == 1)
                .map(filter -> new Filter(new FilterField(fieldConvertName(filter.field().value())), filter.operator(), filter.value()))
                .toList();

        CriteriaPredicateRoot<T> criteriaPredicateRoot = new CriteriaPredicateRoot<>(this.builder);

        List<Predicate> predicates = criteriaPredicateRoot.format(filtersRoot, root);

        for (Filter filter : criteria.filters().filters()) {
            String[] key = filter.field().value().split("--");
            if (key.length == 2) {
                if (this.joinMap.containsKey(fieldConvertName(key[0]))) {
                    criteriaPredicateJoin = new CriteriaPredicateJoin(this.builder);
                    predicates.add(criteriaPredicateJoin.format(new Filter(new FilterField(fieldConvertName(key[1])), filter.operator(), filter.value()), this.joinMap.get(fieldConvertName(key[0]))));
                }
            }
        }

        if (criteria.order().hasOrder()) {
            String[] key = criteria.order().orderBy().value().split("--");
            Path<Object> orderBy = null;
            if (key.length == 2) {
                if (this.joinMap.containsKey(fieldConvertName(key[0]))) {
                    orderBy = this.joinMap.get(fieldConvertName(key[0])).get(fieldConvertName(key[1]));
                }
            } else {
                orderBy = root.get(fieldConvertName(criteria.order().orderBy().value()));
            }

            Order order = criteria.order().orderType().isAsc() ? this.builder.asc(orderBy) : this.builder.desc(orderBy);

            cq.orderBy(order);
        }

        if (search != null) predicates.add(search);

        return cq.where(predicates.toArray(new Predicate[0]));
    }

    /**
     * Obtiene los joins de la consulta y los almacena en un mapa.
     *
     * @param joins el conjunto de joins de la consulta
     */
    private void getJoins(Set<? extends Join<?, ?>> joins) {
        joins.forEach(join -> {
            this.joinMap.put(join.getAttribute().getName(), join);
            if (!join.getJoins().isEmpty()) {
                this.getJoins(join.getJoins());
            }
        });
    }

    /**
     * Convierte el nombre de un campo en un formato camelCase.
     *
     * @param field el nombre del campo a convertir
     * @return el nombre del campo convertido en formato camelCase
     */
    private static String fieldConvertName(String field) {
        String[] fieldSplit = field.split("_");
        StringBuilder fieldConverted = new StringBuilder();
        for (int i = 0; i < fieldSplit.length; i++) {
            String key;
            if (i != 0) {
                key = fieldSplit[i].toUpperCase(Locale.ROOT).charAt(0) + fieldSplit[i].substring(1).toLowerCase(Locale.ROOT);
            } else {
                key = fieldSplit[i];
            }
            fieldConverted.append(key);
        }
        return fieldConverted.toString();
    }

}
