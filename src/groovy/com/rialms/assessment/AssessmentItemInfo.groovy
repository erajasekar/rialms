package com.rialms.assessment

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.util.QtiUtils
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/19/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
class AssessmentItemInfo {

    private Map<String, String> outcomeValues;

    private Map<String, String> responseValues;

    private Map<String, String> templateValues;

    private AssessmentItem assessmentItem;


    public AssessmentItemInfo(AssessmentItem item) {
        this.assessmentItem = item;
        outcomeValues = QtiUtils.convertQTITypesToParams(item.outcomeValues);
        responseValues = QtiUtils.convertQTITypesToParams(item.responseValues);
        templateValues = QtiUtils.convertQTITypesToParams(item.templateValues)
    }

    public Map<String, String> getResponseValues() {
        return responseValues
    }

    public Map<String, String> getOutcomeValues() {
        return outcomeValues
    }


    public Map<String, String> getTemplateValues() {
        return templateValues
    }

    public OutcomeDeclaration getOutcomeDeclarationForIdentifier(String identifier) {
        return QtiUtils.findVariableDeclarationByIdentifier(assessmentItem.outcomeDeclarations, identifier);
    }

    public TemplateDeclaration getTemplateDeclarationForIdentifier(String identifier) {
        return QtiUtils.findVariableDeclarationByIdentifier(assessmentItem.templateDeclarations, identifier);
    }
}