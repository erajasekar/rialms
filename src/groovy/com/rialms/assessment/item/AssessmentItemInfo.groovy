package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.util.QtiUtils
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import groovy.util.logging.*

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/19/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class AssessmentItemInfo {

    private Map<String, String> outcomeValues = [:];

    private Map<String, List<String>> responseValues = [:];

    private Map<String, String> templateValues;

    private String dataPath;

    private AssessmentItem assessmentItem;

    private Node xmlRoot;

    public AssessmentItemInfo(AssessmentItem item, String dataPath) {

        if (!item) {
            throw new IllegalArgumentException("AssessmentItem can't be null");
        }
        this.assessmentItem = item;
        this.outcomeValues = QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues);
        this.templateValues = QtiUtils.convertQTITypesToParams(assessmentItem.templateValues);
        this.dataPath = dataPath;
        println "assessmentItem ==> ${assessmentItem.title}";
        println "assessmentItem == > ${assessmentItem.sourceFile}";
        xmlRoot = new XmlParser().parse(assessmentItem.sourceFile);
    }

    public Map<String, String> getResponseValues() {
        return responseValues
    }

    public Map<String, String> getOutcomeValues() {
        return outcomeValues;
    }


    public Map<String, String> getTemplateValues() {
        return templateValues;
    }

    public OutcomeDeclaration getOutcomeDeclarationForIdentifier(String identifier) {
        return QtiUtils.findVariableDeclarationByIdentifier(assessmentItem.outcomeDeclarations, identifier);
    }

    public TemplateDeclaration getTemplateDeclarationForIdentifier(String identifier) {
        return QtiUtils.findVariableDeclarationByIdentifier(assessmentItem.templateDeclarations, identifier);
    }

    private void setResponses(Map params) {
        List identifiers = assessmentItem.responseDeclarations.collect {it -> it.identifier};
        responseValues = QtiUtils.convertToRespValues(params, identifiers);
        //TODO
        com.rialms.assessment.item.AssessmentItemInfo.log.info("Response Values ${responseValues}");
        assessmentItem.setResponses(responseValues);
    }

    public void processResponses(Map params) {
        setResponses(params);
        assessmentItem.processResponses();
        outcomeValues = QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues)
        com.rialms.assessment.item.AssessmentItemInfo.log.info("OUTCOME ==> ${outcomeValues}");
    }

    public String getTitle() {
        return assessmentItem.getTitle();
    }

    public String getDataPath() {
        return dataPath;
    }

    public Node getXmlRoot() {
        return xmlRoot;
    }
}
