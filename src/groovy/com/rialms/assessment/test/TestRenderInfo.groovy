package com.rialms.assessment.test

import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.value.Value
import com.rialms.assessment.item.AssessmentItemInfo

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
    private Map<String, Object> pageParams;

    public TestRenderInfo() {
    }

    public TestRenderInfo(AssessmentItemInfo assessmentItemInfo, Map<String, Object> assessmentParams, Map<String, Object> pageParams) {

        if (!assessmentItemInfo) {
            throw new IllegalArgumentException("AssessmentItemInfo can't be null");
        }
        this.assessmentItemInfo = assessmentItemInfo;
        this.assessmentParams = assessmentParams;
        this.pageParams = pageParams;

    }

    public AssessmentItemInfo getAssessmentItemInfo() {
        return assessmentItemInfo
    }

    public Map<String, Object> getAssessmentParams() {
        return assessmentParams
    }

    public Map<String, Object> getPageParams() {
        return pageParams
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assessment Item => ${assessmentItemInfo.getTitle()}\n ");
        sb.append("Assessment Params ------------------------- \n");
        assessmentParams.each {
            sb.append("${it.key} ==> ${it.value} ==> ${it.value.getClass()} \n");
        }
        sb.append("Assessment Params ------------------------- ")
        sb.append("Page Params =>  ${pageParams} \n");
        return sb.toString();
    }

    public Map<String, Object> toPropertiesMap() {
        return ['assessmentItemInfo': assessmentItemInfo, 'assessmentParams': assessmentParams, 'pageParams': pageParams]
    }
}