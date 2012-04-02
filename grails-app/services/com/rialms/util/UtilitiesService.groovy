package com.rialms.util

import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.value.MultipleValue
import org.qtitools.qti.value.Value
import com.rialms.assessment.item.AssessmentItemInfo;

class UtilitiesService {
    
     def grailsApplication;
     static transactional = false
     static scope = "singleton"
    
     public boolean showInternalState(){
         grailsApplication.config.rialms.showInternalState;
     }
     
}
