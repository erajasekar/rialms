package com.rialms.assessment.test

import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.value.Value
import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.consts.Constants

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/24/12
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
class TestRenderInfo {

    private AssessmentItemInfo assessmentItemInfo;
    private Map<String, Object> assessmentParams;

    public TestRenderInfo() {
    }

    public TestRenderInfo(AssessmentItemInfo assessmentItemInfo, Map<String, Object> assessmentParams) {

        if (!assessmentItemInfo) {
            throw new IllegalArgumentException("AssessmentItemInfo can't be null");
        }
        this.assessmentItemInfo = assessmentItemInfo;
        this.assessmentParams = assessmentParams;
    }

    public AssessmentItemInfo getAssessmentItemInfo() {
        return assessmentItemInfo
    }

    public Map<String, Object> getAssessmentParams() {
        return assessmentParams
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assessment Item => ${assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM) ? '_BLANK_' : assessmentItemInfo.getTitle()}\n ");
        sb.append("Assessment Params ------------------------- \n");
        assessmentParams.each {
            sb.append("${it.key} ==> ${it.value} \n");
        }
        sb.append("Assessment Params ------------------------- ")
        return sb.toString();
    }

    public Map<String, Object> toPropertiesMap() {
        return ['assessmentItemInfo': assessmentItemInfo, 'assessmentParams': assessmentParams]
    }

    public Map getRenderOutput() {

        Map output = ['testOutcomeValues': assessmentParams.outcomeValues];
        output[Constants.assessmentHeader] = assessmentItemInfo.header;
        output[Constants.testStatusModel] = assessmentParams[Constants.testStatusModel];
        output[Constants.navigationButtonStates] = assessmentParams[Constants.navigationButtonStates];
        output[Constants.endAttemptButtons] = assessmentItemInfo.endAttemptButtons
        return output;
    }
}
