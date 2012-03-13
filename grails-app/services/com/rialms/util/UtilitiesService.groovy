package com.rialms.util

import org.qtitools.qti.node.item.AssessmentItem;

class UtilitiesService {

    static transactional = false
	static scope = "singleton"

	public Map<String, List<String>> convertToRespIdentifiers(Map params) {
		Map<String, List<String>> map = new HashMap<String, List<String>>()

		params.each{k,v ->
			List<String> values = new ArrayList<String>()
			values << v;
			map.put(k, values)
		}

		return map
	}


    public Map<String,String> processAssessmentItem(AssessmentItem assessmentItem, Map params){

        Map<String, List<String>> responses = convertToRespIdentifiers(params)  ;
        println "resp iden ${responses}"
        assessmentItem.setResponses(responses);
        assessmentItem.processResponses();
        println "score" + assessmentItem.getOutcomeValue("SCORE");
        println "${assessmentItem.getOutcomeValues()}"
        return  assessmentItem.getOutcomeValues()
    }
}
