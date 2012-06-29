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

    //Used to share any dynamic parameters between interactions.
    private Map<String,String> params = [:];

    private Map endAttemptButtons = [:];

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

    public void addEndAttemptButton(String buttonId, String buttonTitle){
        endAttemptButtons = endAttemptButtons + [(buttonId): buttonTitle];
    }

    public Map<String,String> getEndAttemptButtons(){
        return endAttemptButtons
    }
    public void createHeader(){
        header = createHeader(title);
    }

    public static Map createHeader(String title){
        return [(Consts.title):title];
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
        return element.isVisible(outcomeValues,templateValues);
    }
    //TODO P4 This will work only for template & initial rendering
    public boolean checkForHiddenElement(Node node, Tag xmlTag){
        HiddenElement hiddenElement = createHiddenElement(node,xmlTag,HiddenElement.ValueLookUpType.Template);
        hiddenElement ? !isVisible(hiddenElement):false;
    }

    public HiddenElement addHiddenElement(Node node, Tag xmlTag){

        HiddenElement.ValueLookUpType valueLookUpType = Tag.isFeedBackTag(xmlTag)? HiddenElement.ValueLookUpType.Outcome:HiddenElement.ValueLookUpType.Template;
        HiddenElement hiddenElement = createHiddenElement(node,xmlTag,valueLookUpType);
        if (hiddenElement){
            hiddenElements << hiddenElement;
        }
        return hiddenElement;
    }

    private HiddenElement createHiddenElement(Node node, Tag xmlTag, HiddenElement.ValueLookUpType valueLookUpType){
        String identifier = node.'@identifier';
        String valueLookUpKey = (valueLookUpType == HiddenElement.ValueLookUpType.Template) ? node.'@templateIdentifier' : node.'@outcomeIdentifier';
        String visibilityMode = node.'@showHide';
        if (valueLookUpType && visibilityMode){
            return new HiddenElement(identifier,valueLookUpKey,xmlTag,visibilityMode,valueLookUpType);
        }
        else{
            return null;
        }
    }

    public boolean isComplete() {
        boolean complete;
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


    public void addParam(String key, String value){
        if (params.containsKey(key)){
           // throw new IllegalArgumentException('key ${key} already found in params.');
            log.warn("key ${key} already found in params.")
        }else{
            params.put(key,value);
        }

    }

    public String getParam(String key){
        if (params.containsKey(key)) {
            return params[key];
        }else{
            throw new IllegalArgumentException('No param found for key ${key');
        }
    }
    public Map getRenderOutput() {
        Map<String, List<String>> visibleAndHiddenElementIds = visibleAndHiddenElementIds;
        Map output = [(Consts.itemOutcomeValues): outcomeValues,
                (Consts.visibleElementIds): visibleAndHiddenElementIds[Consts.visibleElementIds],
                (Consts.hiddenElementIds): visibleAndHiddenElementIds[Consts.hiddenElementIds]];
        if (isComplete()) {
            output[(Consts.disableElementIds)] = disableOnCompletionIds.collect { "#${it}"};
        }
        Map angularData = [(Consts.assessmentHeader):header, (Consts.endAttemptButtons):endAttemptButtons];
        output[Consts.angularData] = angularData;
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

   /* @Override
    public String toString() {
        return "AssessmentItemInfo{" +
                "dataPath='" + dataPath + '\'' +
                ", assessmentItem=" + assessmentItem.title +
                '}';
    }*/
}
