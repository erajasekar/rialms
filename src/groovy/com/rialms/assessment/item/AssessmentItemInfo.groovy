package com.rialms.assessment.item

import org.qtitools.qti.node.item.AssessmentItem
import com.rialms.util.QtiUtils
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import groovy.util.logging.*
import org.qtitools.qti.node.content.ItemBody
import org.qtitools.qti.value.Value
import org.qtitools.qti.validation.ValidationItem

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/19/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class AssessmentItemInfo {

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
        this.dataPath = dataPath;
        xmlRoot = new XmlParser().parse(assessmentItem.sourceFile);
    }

    public Map<String, String> getResponseValues() {
        return QtiUtils.convertRespValuesToStringMap(assessmentItem.responseValues);
    }

    public Map<String, String> getOutcomeValues() {
        return QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues);
    }

    public Map<String, String> getTemplateValues() {
        return QtiUtils.convertQTITypesToParams(assessmentItem.templateValues);
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

        Map<String,List<String>> responseValues = QtiUtils.convertToRespValues(params, identifiers);
        //TODO
        log.info("Response Values ${this} ==> ${responseValues}");
        assessmentItem.setResponses(responseValues);
    }

    public void processResponses(Map params) {
        setResponses(params);
        assessmentItem.processResponses();
        log.info("OUTCOME ==> ${assessmentItem.outcomeValues}");
        //TODO UPDATE LOG LEVEL
        log.info("IS COMPLETE ==> ${this.complete}");
    }

    public boolean isComplete(){
        boolean complete = false;
        if (assessmentItem.adaptive){
            Value completionStatus = assessmentItem.getOutcomeValue(AssessmentItem.VARIABLE_COMPLETION_STATUS)
            complete = (completionStatus && completionStatus.toString().equals(AssessmentItem.VALUE_ITEM_IS_COMPLETED))
        }else{
            complete = assessmentItem.isCorrect();
        }
        return complete;
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

    public List<ValidationItem> validate(){
        return assessmentItem.validate().allItems;
    }

}
