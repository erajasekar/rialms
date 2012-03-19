package com.rialms.util

import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.Value;

class UtilitiesService {

    static transactional = false
    static scope = "singleton"

    public Map<String, List<String>> convertToRespValues(Map params, List identifiers) {
        Map<String, List<String>> map = new HashMap<String, List<String>>()

        identifiers.each {i ->
            List<String> values = new ArrayList<String>()
            def respValue = params[i];
            if (respValue instanceof String) {
                values << respValue;
            } else {
                respValue.each {values.add(it)}
            }

            map.put(i, values)
        }

        return map
    }

    public Map<String, String> convertQTITypesToParams(Map<String, Value> outcome) {

        Map<String, String> params = [:];

        outcome.each { k, v ->
            String value;
            if (!(v instanceof org.qtitools.qti.value.NullValue)) {
                if (v instanceof MultipleValue) {
                    value = v.getAll().join(',');
                }
                else {
                    value = v.toString();
                }
                params[k] = value;
            }
        }
        return params;
    }


    public Map<String, String> processAssessmentItem(AssessmentItem assessmentItem, Map params) {


        List identifiers = assessmentItem.getResponseDeclarations().collect {it -> it.identifier};
        Map<String, List<String>> responses = convertToRespValues(params, identifiers);
        //TODO
        log.info("Response Values ${responses}");
        assessmentItem.setResponses(responses);
        assessmentItem.processResponses();

        log.info("OUTCOME ==> ${assessmentItem.getOutcomeValues()}");
        return convertQTITypesToParams(assessmentItem.getOutcomeValues());

    }
}
