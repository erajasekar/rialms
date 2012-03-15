package com.rialms.util

import org.qtitools.qti.node.item.AssessmentItem;

class UtilitiesService {

    static transactional = false
	static scope = "singleton"

	public Map<String, List<String>> convertToRespValues(Map params, List identifiers) {
		Map<String, List<String>> map = new HashMap<String, List<String>>()

		identifiers.each{i ->
			List<String> values = new ArrayList<String>()
			values << params[i];
			map.put(i, values)
		}

		return map
	}


    public Map<String,String> processAssessmentItem(AssessmentItem assessmentItem, Map params){

        List identifiers = assessmentItem.getResponseDeclarations().collect {it-> it.identifier};
        Map<String, List<String>> responses = convertToRespValues(params, identifiers)  ;
        assessmentItem.setResponses(responses);
        assessmentItem.processResponses();
        log.info ( "OUTCOME ==> ${assessmentItem.getOutcomeValues()}" );
        return  assessmentItem.getOutcomeValues()
    }
}
