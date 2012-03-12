package com.rialms.assessment

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.qtitools.qti.node.item.AssessmentItem

class Exercise {

    String dataFile;
    static constraints = {
    }

    def getAssessmentItem = {

        String exercisePath =   CH.config.rialms.excercisePath;
		AssessmentItem assessmentItem = new AssessmentItem();
		assessmentItem.load(AH.application.parentContext.getResource("${exercisePath}" + dataFile).getFile());
		return assessmentItem;
	}
}
