package com.rialms.util

import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.Value
import com.rialms.assessment.AssessmentItemInfo;

class UtilitiesService {

    static transactional = false
    static scope = "singleton"

    public AssessmentItemInfo processAssessmentItem(AssessmentItem assessmentItem, Map params) {

        List identifiers = assessmentItem.getResponseDeclarations().collect {it -> it.identifier};
        Map<String, List<String>> responses = QtiUtils.convertToRespValues(params, identifiers);
        //TODO
        log.info("Response Values ${responses}");
        assessmentItem.setResponses(responses);
        assessmentItem.processResponses();

        log.info("OUTCOME ==> ${assessmentItem.getOutcomeValues()}");
        return new AssessmentItemInfo(assessmentItem);

    }
}
