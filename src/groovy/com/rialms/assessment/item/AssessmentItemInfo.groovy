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
import org.qtitools.qti.value.NullValue
import org.qtitools.qti.node.item.Stylesheet
import org.qtitools.qti.node.item.interaction.Interaction
import org.apache.commons.collections.CollectionUtils

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

    public Set<HiddenElement> hiddenElements = [];

    private List<String> disableOnCompletionIds = [];

    public static final String controllerActionForProcessItem = 'process';

    public static final String onSuccessCallbackForProcessItem = 'updateRenderedItem(data)';

    private AssessmentItemRef assessmentItemRef;

    private AssessmentItemStatus status = NOT_PRESENTED;

    //Used to hold header title and hint/solution buttons
    private Map header = [:];

    //Used to share any dynamic parameters between interactions.
    private Map<String, String> params = [:];

    private Map endAttemptButtons = [:];

    private boolean isResponseValid = true;

    //Variable to track number of times multi hint  button is clicked.
    private int multiHintClickCount = 0;

    //Variable to track total number of multi hint steps defined in item xml
    private int multiHintStepCount = 0;

    //Variable to track remaining number of multi hint steps available to show to user.
    private int multiHintRemainingCount = 0;

    //To avoid conflict with getter, named with beginning underscore
    private List<InteractionHelp> _interactionHelps = new ArrayList<InteractionHelp>();

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
        initInteractionHelps();
    }

    public Map<String, List<String>> getResponseValues() {
        return QtiUtils.convertRespValuesToStringMap(assessmentItem.responseValues);
    }

    public Map<String, String> getOutcomeValues() {
        return QtiUtils.convertQTITypesToParams(assessmentItem.outcomeValues);
    }

    public Map<String, String> getTemplateValues() {
        return QtiUtils.convertQTITypesToParams(assessmentItem.templateValues);
    }

    private void setAndProcessResponses(Map params) {
        if (params.containsKey(Consts.MULTI_HINT_IDENTIFIER)) {
            multiHintClicked();
        }
        List identifiers = assessmentItem.responseDeclarations.collect {it -> it.identifier};

        Map<String, List<String>> responseValues = QtiUtils.convertToRespValues(params, identifiers);
        isResponseValid = !responseValues.isEmpty();
        log.info("Response Values ${this} ==> ${responseValues} , valid : ${isResponseValid}");

        if (isResponseValid) {
            log.debug("DEBUG Got valid response, updating assessmentItem")
            assessmentItem.setResponses(responseValues);
            assessmentItem.processResponses();
        }
        if (params.containsKey(Consts.submitClicked) && isResponseValid) {
            status = RESPONDED;
        } else {
            log.debug("DEBUG submit was not clicked, other endAttempt interaction like show hint/solution clicked, not setting status to RESPONDED");
        }
    }

    public boolean processResponses(Map params) {
        setAndProcessResponses(params);
        log.debug("DEBUG Response after processing ${responseValues}")
        log.info("OUTCOME ==> ${assessmentItem.outcomeValues}");
    }

    public void addEndAttemptButton(String buttonId, String buttonTitle) {
        endAttemptButtons = endAttemptButtons + [(buttonId): buttonTitle];
    }

    public Map<String, String> getEndAttemptButtons() {
        return endAttemptButtons
    }

    private void initInteractionHelps(){
        try{

            List<Interaction> interactions = assessmentItem.itemBody.interactions;

            Set<Tag> interactionTags = interactions.collect {Tag.valueOf(it.CLASS_TAG)};
            log.debug("DEBUG interactionTags ${interactionTags}");
            interactionTags.each { interactionTag ->
                InteractionHelp interactionHelp = InteractionHelp.forTag(interactionTag);

                if (interactionHelp) {
                    _interactionHelps << interactionHelp;
                }
            }

            log.info("interactionHelps ${interactionHelps}");
        } catch(Throwable t){
            t.printStackTrace();
        }

    }

    public void createHeader() {
        header = createHeader(title);
    }

    public static Map createHeader(String title) {
        return [(Consts.title): title];
    }

    public void addDisableOnCompletionId(String id) {
        disableOnCompletionIds << id;
    }

    public void setStatus(AssessmentItemStatus status) {
        this.status = status;
    }

    public void incrementMultiHintStepCount() {
        multiHintStepCount++;
        multiHintRemainingCount = multiHintStepCount - multiHintClickCount;
    }

    public void multiHintClicked() {
        multiHintClickCount++;
        multiHintRemainingCount = multiHintStepCount - multiHintClickCount;
    }

    public int getMultiHintClickCount() {
        return multiHintClickCount;
    }

    public int getMultiHintStepCount() {
        return multiHintStepCount;
    }

    public int getMultiHintRemainingCount() {
        return multiHintRemainingCount;
    }

    public Map getHeader() {
        return header;
    }

    public Map<String, Set<String>> getVisibleAndHiddenElementIds() {
        Set<String> visibleIds = [];
        Set<String> hiddenIds = [];
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
        return element.isVisible(outcomeValues, templateValues);
    }
    //TODO P4 This will work only for template & initial rendering
    public boolean checkForHiddenElement(Node node, Tag xmlTag) {
        HiddenElement hiddenElement = createHiddenElement(node, xmlTag, HiddenElement.ValueLookUpType.Template);
        hiddenElement ? !isVisible(hiddenElement) : false;
    }

    public HiddenElement addHiddenElement(Node node, Tag xmlTag) {

        HiddenElement.ValueLookUpType valueLookUpType = Tag.isFeedBackTag(xmlTag) ? HiddenElement.ValueLookUpType.Outcome : HiddenElement.ValueLookUpType.Template;
        HiddenElement hiddenElement = createHiddenElement(node, xmlTag, valueLookUpType);
        if (hiddenElement) {
            hiddenElements << hiddenElement;
        }
        return hiddenElement;
    }

    /**
     * This method will reset multiHintStepCount value so that re-rendering assessmentItem will not cause any problems.
     */
    public void resetMultiHintStepCount() {
        multiHintStepCount = 0;
    }

    /**
     * This method will reset multiHintClickCount value so that re-starting assessmentItem in a test work as expected
     */
    public void resetAllMultiHintCounts() {
        multiHintStepCount = 0;
        multiHintClickCount = 0;
        multiHintRemainingCount = 0;
    }

    private HiddenElement createHiddenElement(Node node, Tag xmlTag, HiddenElement.ValueLookUpType valueLookUpType) {
        String identifier = node.'@identifier';
        String valueLookUpKey = (valueLookUpType == HiddenElement.ValueLookUpType.Template) ? node.'@templateIdentifier' : node.'@outcomeIdentifier';
        String visibilityMode = node.'@showHide';
        String sourceElementId= node.'@id';
        if (valueLookUpType && visibilityMode) {
            return new HiddenElement(identifier, valueLookUpKey, xmlTag, visibilityMode, valueLookUpType,sourceElementId);
        }
        else {
            return null;
        }
    }

    public boolean isCorrect() {
        //Note: AssessmentItem.isCorrect() is patched to handle adaptive items correctly.
        return assessmentItem.isCorrect();
    }

    public double getScore() {
        Value score = assessmentItem.outcomeValues[Consts.ITEM_OUTCOME_SCORE];
        if (!(score instanceof NullValue)) {
            return score.toString().toDouble();
        } else {
            return 0;
        }
    }

    public void addParam(String key, String value) {
        if (params.containsKey(key)) {
            // throw new IllegalArgumentException('key ${key} already found in params.');
            log.warn("key ${key} already found in params.")
        } else {
            params.put(key, value);
        }

    }

    public String getParam(String key) {
        if (params.containsKey(key)) {
            return params[key];
        } else {
            throw new IllegalArgumentException('No param found for key ${key');
        }
    }

    public List<Map<String, String>> getItemStylesheets() {
        List<Map<String, String>> stylesheets = [];

        assessmentItem.stylesheets.each { stylesheet ->
            String fullPath = dataPath + stylesheet.getHref();

            Map attrs = [href:fullPath];
            String media = stylesheet.getMedia();
            String title = stylesheet.getTitle();
            String type = stylesheet.getTitle();
            attrs.media = (media) ? media : "screen,projector"
            attrs.type = (type) ? type : "text/css"
            attrs.title = (title) ? title : "";
            stylesheets << attrs;
        }
        log.debug("DEBUG Returning ItemStylesheets ${stylesheets}")
        return stylesheets;
    }

    public Map getRenderOutput() {
        Map<String, Set<String>> visibleAndHiddenElementIds = visibleAndHiddenElementIds;
        Map output = [(Consts.itemOutcomeValues): outcomeValues,
                (Consts.visibleElementIds): visibleAndHiddenElementIds[Consts.visibleElementIds],
                (Consts.hiddenElementIds): visibleAndHiddenElementIds[Consts.hiddenElementIds]];
        if (isResponseValid && isCorrect()) {
            output[(Consts.disableElementIds)] = disableOnCompletionIds.collect { "#${it}"};
        }
        Map angularData = [(Consts.assessmentHeader): header,
                (Consts.endAttemptButtons): endAttemptButtons,
                (Consts.isResponseValid): isResponseValid];

        output[Consts.angularData] = angularData;

        log.debug("DEBUG AssessmentItemInfo renderOutput ${output}")
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

    public boolean getIsResponseValid() {
        return isResponseValid
    }

    public ItemBody getItemBody() {
        assessmentItem.itemBody;
    }

    public AssessmentItem getAssessmentItem() {
        return assessmentItem;
    }

    public List<InteractionHelp> getInteractionHelps() {
        return _interactionHelps.asImmutable();
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

    public List<TemplateDeclaration> getTemplateDeclarations() {
        return assessmentItem.templateDeclarations;
    }

    public List<OutcomeDeclaration> getOutcomeDeclarations() {
        return assessmentItem.outcomeDeclarations;
    }

    @Override
    public String toString() {
        return "AssessmentItemInfo{" +
                "dataPath='" + dataPath + '\'' +
                ", assessmentItem=" + assessmentItem.title +
                '}';
    }
}
