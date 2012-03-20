package com.rialms.util

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
}
