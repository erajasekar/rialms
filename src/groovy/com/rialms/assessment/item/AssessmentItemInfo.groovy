package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.util.QtiUtils
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import groovy.util.logging.*
import org.qtitools.qti.node.content.ItemBody

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

    public static final BLANK_ITEM = new AssessmentItemInfo();

    public AssessmentItemInfo() {

    }

    public AssessmentItemInfo(AssessmentItem item, String dataPath) {

        if (!item) {
            throw new IllegalArgumentException("AssessmentItem can't be null");
        }
        this.assessmentItem = item;
        this.outcomeValues = QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues);
        this.templateValues = QtiUtils.convertQTITypesToParams(assessmentItem.templateValues);
        this.dataPath = dataPath;
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

    public List<TemplateDeclaration> getTemplateDeclarations() {
        return assessmentItem.templateDeclarations;
    }

    public List<OutcomeDeclaration> getOutcomeDeclarations() {
        return assessmentItem.outcomeDeclarations;
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
        log.info("Response Values ${this} ==> ${responseValues}");
        assessmentItem.setResponses(responseValues);
    }

    public void processResponses(Map params) {
        setResponses(params);
        assessmentItem.processResponses();
        outcomeValues = QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues)
        log.info("OUTCOME ==> ${outcomeValues}");
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

    public boolean getAdaptive() {
        assessmentItem.adaptive;
    }

    public ItemBody getItemBody() {
        assessmentItem.itemBody;
    }

    public AssessmentItem getAssessmentItem() {
        return assessmentItem;
    }

}
