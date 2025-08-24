package com.example.tickets.common.parse;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Esta clase nos permite parsear los filtros que exponemos a los clientes
 *
 * @author Angel P
 * @version 19/01/2023
 */

@Slf4j
public final class ParseFilters {

    private ParseFilters() {
    }

    /**
     * Método que parsea un valor serializable a Optional<Integer>
     *
     * @param value
     * @return Retorna un entero oprcional
     */
    public static Optional<Integer> serializableToOptionalInteger(Serializable value) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Integer.valueOf((String) value));
    }

    /**
     * Método que convierte un objeto de parametros Map en una lista de HashMap
     *
     * @param params
     * @return Retorna una lista d HashMap
     */
    public static List<HashMap<String, String>> parseFilters(Map<String, Serializable> params) {
        /*** --- Otenemos la cantidad de parametros --- ***/
        int maxParams = params.size();

        /*** --- Se declara una lista de HashMap --- ***/
        List<HashMap<String, String>> filters = new ArrayList<>();

        /*** --- Recorre los parametros --- ***/
        for (int possibleFilterKey = 0; possibleFilterKey < maxParams; possibleFilterKey++) {

            /*** --- Verifico si es un parametro de tipo filtro que viene el formato con llave filters[0][field] --- ***/
            if (params.containsKey(String.format("filters[%s][field]", possibleFilterKey))) {
                int key = possibleFilterKey;

                /*** --- Se declara un objeto  HashMap --- ***/
                HashMap<String, String> map = new HashMap<>();

                /*** --- Se asigna al objeto la llaves de lso filtros extraidas que son [field - operadaor - value] --- ***/
                map.put("field", (String) params.get(String.format("filters[%s][field]", key)));
                map.put("operator", (String) params.get(String.format("filters[%s][operator]", key)));
                map.put("value", (String) params.get(String.format("filters[%s][value]", key)));

                /*** --- Se agrega el objeto  a la lista de HashMap --- ***/
                filters.add(map);
            }
        }
        /*** --- Retornamos la lista --- ***/
        return filters;
    }

    /**
     * Convierte una lista de mapas de filtros en una lista de objetos de tipo Filter.
     *
     * @param filtersMap La lista de mapas de filtros.
     * @return La lista de objetos Filter.
     */
    public static List<com.example.tickets.common.criteria.Filter> getFilters(List<HashMap<String, String>> filtersMap) {
        return filtersMap.stream()
                .map(com.example.tickets.common.criteria.Filter::fromValues)
                .collect(Collectors.toList());
    }

}
