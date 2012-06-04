package com.rialms.assessment.item

import com.rialms.consts.Constants as Consts

import com.rialms.assessment.render.HiddenElement
import com.rialms.consts.AssessmentItemStatus
import com.rialms.consts.Tag
import com.rialms.util.QtiUtils
import groovy.util.logging.Log4j
import org.qtitools.qti.node.content.ItemBody
import org.qtitools.qti.node.item.AssessmentItem
import org.qtitools.qti.node.item.template.declaration.TemplateDeclaration
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration
import org.qtitools.qti.node.test.AssessmentItemRef
import org.qtitools.qti.validation.ValidationResult
import org.qtitools.qti.value.Value
import static com.rialms.consts.AssessmentItemStatus.*

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

    public List<HiddenElement> hiddenElements = [];

    private List<String> disableOnCompletionIds = [];

    public static final String controllerActionForProcessItem = 'process';

    public static final String onSuccessCallbackForProcessItem = 'updateRenderedItem(data)';

    private AssessmentItemRef assessmentItemRef;

    private AssessmentItemStatus status = NOT_PRESENTED;

    //Used to hold header title and hint/solution buttons
    private Map header = [:];

    public AssessmentItemInfo() {
    }

    public AssessmentItemInfo(AssessmentItem item, String dataPath) {
        if (!item) {
            throw new IllegalArgumentException("AssessmentItem can't be null");
        }
        this.assessmentItem = item;
        this.dataPath = dataPath;
        xmlRoot = new XmlParser().parse(assessmentItem.sourceFile);
        status = PRESENTED;
        createHeader();
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
        if (params.containsKey('submitClicked')) {
            status = RESPONDED;
        } else {
            log.debug("DEBUG submit was not clicked, other endAttempt interaction like show hint/solution clicked, not setting status to RESPONDED");
        }

        List identifiers = assessmentItem.responseDeclarations.collect {it -> it.identifier};

        Map<String, List<String>> responseValues = QtiUtils.convertToRespValues(params, identifiers);
        log.info("Response Values ${this} ==> ${responseValues}");
        assessmentItem.setResponses(responseValues);
    }

    public void processResponses(Map params) {
        setResponses(params);
        assessmentItem.processResponses();
        log.info("OUTCOME ==> ${assessmentItem.outcomeValues}");

    }

    public HiddenElement addHiddenElement(HiddenElement e) {
        hiddenElements << e;
        return e;
    }

    public void addHeaderButton(String buttonId, String buttonTitle){
        //TODO remove comments
       /* if (!header){
            throw IllegalStateException("Header is null, should invoke createHeader first");
        }*/
        header[Consts.buttons] = [(buttonId), buttonTitle];
    }

    public void createHeader(){
        header = [(Consts.title):title, (Consts.id) : null,  (Consts.buttons):[:]];
    }

    public void addDisableOnCompletionId(String id) {
        disableOnCompletionIds << id;
    }

    private void skip() {
        status = SKIPPED;
    }

    private void timeOut() {
        status = TIMED_OUT;
    }


    public Map getHeader(){
        return header;
    }

    public Map<String, List<String>> getVisibleAndHiddenElementIds() {
        List<String> visibleIds = [];
        List<String> hiddenIds = [];
        hiddenElements.each { element ->
            if (isVisible(element)) {
                visibleIds << "#${element.elementId}";
            } else {
                hiddenIds << "#${element.elementId}";
            }
        }
        return [(Consts.visibleElementIds): visibleIds, (Consts.hiddenElementIds): hiddenIds]
    }

    public boolean isVisible(HiddenElement element) {
        Tag tag = element.tag;
        if (Tag.isFeedBackTag(tag)) {
            if (element.isVisible(outcomeValues)) {
                return true;
            }
        } else if (Tag.isTemplateTag(tag)) {
            if (element.isVisible(templateValues)) {
                return true;
            }
        } else {
            log.error("${element} can't belog to Tag ${tag}");
        }
        return false;
    }

    public boolean isComplete() {
        boolean complete = false;
        if (assessmentItem.adaptive) {
            complete = isAdaptiveItemComplete();
        } else {
            complete = assessmentItem.isCorrect();
        }
        return complete;
    }

    public boolean isAdaptiveItemComplete() {
        Value completionStatus = assessmentItem.getOutcomeValue(AssessmentItem.VARIABLE_COMPLETION_STATUS)
        return (completionStatus && completionStatus.toString().equals(AssessmentItem.VALUE_ITEM_IS_COMPLETED))
    }

    public Map getRenderOutput() {
        Map<String, List<String>> visibleAndHiddenElementIds = visibleAndHiddenElementIds;
        Map output = [(Consts.itemOutcomeValues): outcomeValues,
                (Consts.responseValues): responseValues,
                (Consts.visibleElementIds): visibleAndHiddenElementIds[Consts.visibleElementIds],
                (Consts.hiddenElementIds): visibleAndHiddenElementIds[Consts.hiddenElementIds]];
        if (isComplete()) {
            output[(Consts.disableElementIds)] = disableOnCompletionIds.collect { "#${it}"};
        }
        output[Consts.assessmentHeader] = header;
        println "RAJA RENDERED OUTPUT  ===> ${output}";
        return output;
    }

    public String getTitle() {
        return assessmentItem?.getTitle();
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

    public ValidationResult validate() {
        return assessmentItem.validate();
    }

    public AssessmentItemStatus getItemStatus() {
        return status;
    }

    public void setAssessmentItemRef(AssessmentItemRef assessmentItemRef) {
        this.assessmentItemRef = assessmentItemRef;
    }

    @Override
    public String toString() {
        return "AssessmentItemInfo{" +
                "dataPath='" + dataPath + '\'' +
                ", assessmentItem=" + assessmentItem.title +
                '}';
    }
}
