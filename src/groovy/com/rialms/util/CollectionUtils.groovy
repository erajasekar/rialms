package com.rialms.util

import com.rialms.consts.Constants

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/16/12
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
class CollectionUtils {

    /**
     * Returns values in input map as list ordered by positions in key
     * @param input
     * @return
     */
    public static List orderValuesByPosition(Map input) {
        List result = [];
        for (i in 0..input.size()) {
            def value = input[i];
            if (value) {
                result << input[i];
            }
        }
        return result;
    }

    /**
     * shuffles listToShuffle and inserts fixedItems to appropriate position.
     * @param listToShuffle
     * @param fixedItems
     */
    public static List shuffleWithFixedPositions(List listToShuffle, Map<Integer, Object> fixedItems) {
        LinkedList result = new LinkedList(listToShuffle);
        Collections.shuffle(result);
        fixedItems.each {k, v ->
            result.add(k, v);
        }
        return result;
    }

    public static Map mergeMapsByKeyAsList(Map map1, Map map2) {
        def result = map1 + map2
        result.each { key, value ->
            if (map1.containsKey(key) && map2.containsKey(key)) {
                result[key] = [map1[key], map2[key]].flatten();
            }
        }
        return result;
    }

    public static String convertMapToAttributes(Map input){
        StringBuilder sb = new StringBuilder();
        input.each{k,v->
            sb.append("${k}='${v}' ")
        }
        return sb.toString();
    }

    public static String convertMapToDataAttributes(Map input){
        StringBuilder sb = new StringBuilder();
        input.each{k,v->
            sb.append("${Constants.data}-${k.toString().toLowerCase()}='${v.toString()}' ")
        }
        return sb.toString();
    }
}
