/*
<LICENCE>

Copyright (c) 2008, University of Southampton
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this
	list of conditions and the following disclaimer.

  *	Redistributions in binary form must reproduce the above copyright notice,
	this list of conditions and the following disclaimer in the documentation
	and/or other materials provided with the distribution.

  *	Neither the name of the University of Southampton nor the names of its
	contributors may be used to endorse or promote products derived from this
	software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

</LICENCE>
*/

package com.rialms.assessment.test;


import groovy.util.logging.*;
import org.qtitools.qti.exception.QTIException;
import org.qtitools.qti.node.content.variable.RubricBlock;
import org.qtitools.qti.node.item.AssessmentItem;
import org.qtitools.qti.node.item.response.declaration.ResponseDeclaration;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentSection;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.ItemSessionControl;
import org.qtitools.qti.node.test.SubmissionMode;
import org.qtitools.qti.node.test.TestFeedback;
import org.qtitools.qti.node.test.TestFeedbackAccess;
import org.qtitools.qti.node.test.TestPart;
import com.rialms.qti.node.test.flow.DefaultItemFlow;
import com.rialms.qti.node.test.flow.ItemFlow;
import org.qtitools.qti.value.Value
import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.consts.AssessmentItemStatus
import org.qtitools.qti.node.test.SectionPart;
import com.rialms.consts.Constants as Consts
import org.qtitools.qti.node.result.AssessmentResult;

/**
 * This class wraps JQTI to provide a nice interface for building
 * tools (like delivery engines) on top of...
 *
 * @author Jonathon Hare
 * @author Jiri Kajaba
 */
@Log4j
public class AssessmentTestController implements Serializable {
    private static final long serialVersionUID = 1L;

    private ItemFlow flow;

    private String dataPath;

    private AssessmentItemInfo currentItemInfo = null;

    private Map<String, Map<String, AssessmentItemInfo>> processedItems = [:];

    /**
     * Create an AssessmentTestController
     */
    public AssessmentTestController() {
        log.info("Creating new AssessmentTestController");
    }

    /**
     * Create an AssessmentTestController and load the given file using the
     * default flow model.
     * This method is functionally equivalent to creating an AssessmentTestController
     * with an empty constructor, and then calling the load with method with the file.
     *
     * @param file File to load.
     */
    public AssessmentTestController(File file, String dataPath) {
        this();
        this.dataPath = dataPath;
        load(file);
    }

    /**
     * Create an AssessmentTestController with an ItemFlow. The ItemFlow is assumed
     * to have been initialized with a assessmentTest already.
     *
     * @param flow Flow to use.
     */
    public AssessmentTestController(ItemFlow flow) {
        this();
        this.flow = flow;
    }

    /**
     * Get the ItemFlow associated with the controller
     * @return the ItemFlow
     */
    public ItemFlow getItemFlow() {
        return flow;
    }

    /**
     * Get the assessmentTest associated with the controller
     * @return the assessmentTest
     */
    public AssessmentTest getTest() {
        return flow.getTest();
    }

    public TestPart getCurrentTestPart() {
        return flow.getCurrentTestPart();
    }

    public AssessmentItemRef getCurrentItemRef() {
        return flow.getCurrentItemRef();
    }


    public boolean isCurrentTestPartSubmissionModeIndividual() {
        return getCurrentTestPart()?.submissionMode == SubmissionMode.INDIVIDUAL;
    }

    public boolean isCurrentTestPartSubmissionModeSimultaneous() {
        return getCurrentTestPart()?.submissionMode == SubmissionMode.SIMULTANEOUS;
    }

    public boolean hasNoMoreItemsInCurrentTestPart() {
        return !getItemFlow().hasNextItemRef(true);
    }

    public AssessmentItemInfo getCurrentItemInfo() {
        log.debug("Executing getCurrentItemInfo() Processed Items==> ${processedItems}");
        if (currentTestPart && !processedItems[currentTestPart.identifier]) {
            processedItems[currentTestPart.identifier] = [:];
        }
        AssessmentItemRef currentItemRef = flow.getCurrentItemRef();
        AssessmentItem currentItem = currentItemRef?.getItem();
        if (currentItem) {
            if (currentItemInfo == null || !currentItemInfo.assessmentItem.is(currentItem)) {
                if (processedItems[currentTestPart.identifier].containsKey(currentItemRef.identifier)) {
                    currentItemInfo = processedItems[currentTestPart.identifier][currentItemRef.identifier];
                    log.debug("Found exisiting itemInfo for identifier ${currentItemRef.identifier} ==>  ${currentItemInfo} ");
                    return currentItemInfo;
                } else {
                    currentItemInfo = new AssessmentItemInfo(currentItem, dataPath);
                    currentItemInfo.setAssessmentItemRef(currentItemRef);
                    processedItems[currentTestPart.identifier] << [(currentItemRef.identifier): currentItemInfo];
                }
            }
        }
        return currentItemInfo;
    }

    public AssessmentItemRef getPreviousItem(boolean includeFinished) {
        return flow.getPrevItemRef(includeFinished);
    }

    public AssessmentItemRef getNextItem(boolean includeFinished) {
        return flow.getNextItemRef(includeFinished);
    }

    public AssessmentItemRef getItemByIdentifier(String identifier, boolean forward) {
        return flow.getItemRefByIdentifier(identifier, forward);
    }

    /**
     * Load and initialize an assessmentTest using the default flow model.
     * @param testFile File containing assessmentTest
     */
    public void load(File testFile) {
        log.info("Creating new AssessmentTest from " + testFile);
        AssessmentTest test = new AssessmentTest();
        test.load(testFile);
        test.initialize();

        flow = new DefaultItemFlow(test);
    }

    public boolean isTestComplete() {
        return getTest().isFinished();
    }

    public String getCurrentItemIdentifier() {
        AssessmentItemRef air = getCurrentItemRef();
        if (air == null) return null;
        return air.getIdentifier();
    }

    public ItemSessionControl getCurrentItemSessionControl() {
        if (getCurrentItemRef() != null)
            return getCurrentItemRef().getItemSessionControl();
        return null;
    }

    public void setCurrentItemResponses(Map params) throws QTIException {
        //set and process responses at the item level
        currentItemInfo.processResponses(params);
    }

    public Map<String, Value> getCurrentItemResponses() {
        List<ResponseDeclaration> responseDeclarations = getCurrentItemRef().getItem().getResponseDeclarations();
        Map<String, Value> responseMap = new HashMap<String, Value>();

        for (ResponseDeclaration declaration: responseDeclarations) {
            responseMap.put(declaration.getIdentifier(), declaration.getValue());
        }
        return responseMap;
    }

    public void setCurrentItemOutcomes(Map<String, Value> responses) {
        getCurrentItemRef().setOutcomes(responses);
        getTest().processOutcome();
    }

    public void skipCurrentItem() {
        currentItemInfo.skip();
        getCurrentItemRef().skip();

        getTest().processOutcome();
    }

    public void timeOut() {
        currentItemInfo.timeOut();
        AssessmentItemRef air = getCurrentItemRef();
        if (air?.isTimedOut()) {
            air.timeOut();
        }
    }

    public boolean isTestTimedOut() {
        AssessmentItemRef air = getCurrentItemRef();
        return (air && !air.passMaximumTimeLimit());
    }

    public String getTestTitle() {
        return getTest().getTitle();
    }

    /**
     * Get a list of section titles for the current item in top-down order
     * @return List of section titles
     */
    public List<String> getCurrentSectionTitles() {
        if (getCurrentItemRef() == null) return null;
        return getParentTitles();
    }

    public boolean previousEnabled() {
        return flow.hasPrevItemRef(false);
    }

    public boolean backwardEnabled() {
        return flow.hasPrevItemRef(true);
    }

    public boolean nextEnabled() {
        TestPart testPart = getCurrentTestPart();
        if (testPart != null && testPart.areJumpsEnabled() && !getCurrentItemRef().isFinished())
            return false;

        return true;
    }

    public boolean forwardEnabled() {
        TestPart testPart = getCurrentTestPart();
        if (testPart != null && testPart.areJumpsEnabled() && !getCurrentItemRef().isFinished())
            return false;

        return flow.hasNextItemRef(true);
    }

    public boolean submitEnabled() {
        log.debug("checking submitEnabled");
        if (getCurrentItemRef() != null) {
            boolean se = !getCurrentItemRef().isFinished() && getCurrentItemRef().passMaximumTimeLimit();
            log.info("submitEnabled is " + se + " (" + getCurrentItemRef().getIdentifier() + ")");
            return se;
        }
        log.debug("no item in submitEnabled");
        return false;
    }

    public boolean skipEnabled() {
        //in simultaneous mode, skipping makes no sense
        if (getCurrentTestPart().getSubmissionMode() == SubmissionMode.SIMULTANEOUS) return false;

        if (getCurrentItemRef() != null) {
            if (!getCurrentItemRef().isFinished())
                return getCurrentItemRef().getItemSessionControl().getAllowSkipping();
        }
        return false;
    }

    public List<TestFeedback> getAssessmentFeedback() {
        log.debug("Getting AssessmentFeedback");

        //handle assessment-level feedback:
        List<TestFeedback> assessmentFeedback = null;
        if (getTest().isFinished()) {
            log.debug("Getting AT_END feedback for the AssessmentTest");
            assessmentFeedback = getTest().getTestFeedbacks(TestFeedbackAccess.AT_END);
        } else {
            log.debug("Getting DURING feedback for the AssessmentTest");
            assessmentFeedback = getTest().getTestFeedbacks(TestFeedbackAccess.DURING);
        }

        return assessmentFeedback;
    }

    public List<TestFeedback> getTestPartFeedback() {
        log.debug("Getting TestPartFeedback");

        //handle testpart-level feedback:
        TestPart tp = getCurrentTestPart();
        List<TestFeedback> partFeedback = null;
        if (tp != null) {
            AssessmentItemRef air = getCurrentItemRef();
            if (air != null && air.isFinished()) {
                if (!flow.hasNextItemRef(false)) {
                    log.debug("Getting AT_END feedback for the current TestPart");
                    partFeedback = tp.getTestFeedbacks(TestFeedbackAccess.AT_END);
                } else {
                    log.debug("Getting DURING feedback for the current TestPart");
                    partFeedback = tp.getTestFeedbacks(TestFeedbackAccess.DURING);
                }
            }
        }

        return partFeedback;
    }


    public long getTimeSelected() {
        if (getCurrentItemRef() == null) return -1;

        TimeReport tr = TimeReport.getLowestRemainingTimeReport(getCurrentItemRef());
        if (tr != null)
            return tr.getMaximumTimeLimit();
        return -1;
    }


    public long getTimeRemaining() {
        if (getCurrentItemRef() == null) return -1;
        TimeReport tr = TimeReport.getLowestRemainingTimeReport(getCurrentItemRef());
        if (tr != null)
            return tr.getRemainingMaximumTime();
        return -1;
    }

    public int getNumberSelected() {
        if (getCurrentItemRef() == null) return -1;
        TimeReport tr = TimeReport.getLowestRemainingTimeReport(getCurrentItemRef());
        if (tr != null)
            return tr.getSelectedItemsCount();
        return -1;
    }

    public int getNumberRemaining() {
        if (getCurrentItemRef() == null) return -1;
        TimeReport tr = TimeReport.getLowestRemainingTimeReport(getCurrentItemRef());
        if (tr != null)
            return tr.getRemainingItemsCount();
        return -1;
    }

    public Map<String, AssessmentItemStatus> getTestStatus() {
        processedItems.values().collectEntries {it.collectEntries {k, v -> [k, v.itemStatus]}}
    }

    public AssessmentItemStatus getAssessmentItemStatus(String itemIdentifier) {
        AssessmentItemInfo itemInfo = processedItems[currentTestPart.identifier][itemIdentifier];
        AssessmentItemStatus itemStatus = (itemInfo ? itemInfo.itemStatus : AssessmentItemStatus.NOT_PRESENTED);
        return itemStatus;
    }

    public String getReport() {
        return getAssessmentResult().toXmlString();
    }

    /**
     * Returns current result of whole assessment (test and all its items).
     * This is modified version based on  org.qtitools.qti.node.test.AssessmentTest.getAssessmentResult().
     *
     * @return current result of whole assessment (test and all its items)
     */
    public AssessmentResult getAssessmentResult()
    {

        AssessmentTest test = getTest();
        AssessmentResult result = new AssessmentResult();

        result.setTestResult(test.getTestResult(result));

        List<AssessmentItemRef> itemRefs = test.lookupItemRefs(null);
        int sequenceIndex = 1;

        List<String> processedItemIds = processedItems.values().collect {it.collect {k, v -> [k]}}.flatten();

        log.info("DEBUG processedItemIds => ${processedItemIds}");
        for (AssessmentItemRef itemRef : itemRefs){
            //This check is avoid showing NOT_PRESENTED items for Linear Individual items which can use branching rules.
            //So for Linear Individual test part don't show any non processed items.
            if (processedItemIds.contains(itemRef.identifier) || !itemRef.getParentTestPart().areJumpsEnabled()){
                result.getItemResults().addAll(itemRef.getItemResult(result, sequenceIndex++, null));
            }

        }

        return result;
      /* AssessmentResult result = new AssessmentResult();

        result.setTestResult(test.getTestResult(result));

        List<AssessmentItemRef> itemRefs = test.lookupItemRefs(null);
        int sequenceIndex = 1;
        for (AssessmentItemRef itemRef : itemRefs)
            result.getItemResults().addAll(itemRef.getItemResult(result, sequenceIndex++, null));

        return result;*/
    }

    /*
      * Gets an array of titles of all the visible containing sections of this item
      * (in reverse order, so that the top-most section is first)
      */

    protected List<String> getParentTitles() {
        return getParentTitles(getCurrentItemRef().getParentSection());
    }

    /*
      * Gets an array of all the visible titles of sections above and including this (in reverse order)
      */

    private List<String> getParentTitles(AssessmentSection thiz) {
        List<String> titles = new ArrayList<String>();

        AssessmentSection section = thiz;
        while (section != null) {
            if (section.getVisible())
                titles.add(0, section.getTitle());

            section = section.getParentSection();
        }

        return titles;
    }

    /**
     * Gets a list of all rubric blocks at each level (in reverse order)
     *
     * @return assessment rubric for the current item
     */
    public List<List<RubricBlock>> getRubricBlocks() {
        List<List<RubricBlock>> blocks = new ArrayList<List<RubricBlock>>();

        AssessmentSection section = getCurrentItemRef().getParentSection();
        while (section != null) {
            blocks.add(0, section.getRubricBlocks());

            section = section.getParentSection();
        }

        return blocks;
    }

    private List<SectionPartStatus> getSectionPartsStatusInCurrentTestPart(AssessmentItemStatus filterByStatus) {
        List<AssessmentSection> sections = currentTestPart.getAssessmentSections();
        List<SectionPartStatus> sectionPartStatusList = [];
        SectionPartStatus.Position currentPosition = SectionPartStatus.Position.BEFORE;
        sections.each { section ->
            List<SectionPartStatus> childPartStatusList = getSectionPartsStatus(section, null, currentPosition).flatten();
            sectionPartStatusList << childPartStatusList;
            if (!childPartStatusList.isEmpty() && !childPartStatusList[childPartStatusList.size() - 1].isPositionedBeforeCurrent()) {
                currentPosition = SectionPartStatus.Position.AFTER;
            }
        }
        sectionPartStatusList = sectionPartStatusList.flatten();
        if (filterByStatus != AssessmentItemStatus.ALL) {
            sectionPartStatusList = sectionPartStatusList.findAll {SectionPartStatus it -> it.status == filterByStatus};
        }
        return sectionPartStatusList;
    }

    /* This method is used by test cases only to test currentTestPartStatus, the actual application uses getTestStatusModel() */
    public Map<String, List<SectionPartStatus>> getCurrentTestPartStatus(AssessmentItemStatus filterByStatus = AssessmentItemStatus.ALL) {
        Map<String, List<SectionPartStatus>> testPartStatus = getSectionPartsStatusInCurrentTestPart(filterByStatus)
                .groupBy {it.parentSection}
                .collectEntries {k, v ->
            [k, v.flatten()]
        };
        return testPartStatus;
    }

    public List<Map<String, Object>> getTestStatusModel() {
        List<Map<String, Object>> testStatusModel = [];//[['identifier':getCurrentItemIdentifier(),'isHeader':true],['identifier':1,'isHeader':false],['identifier':'2','isHeader':false]];
        getSectionPartsStatusInCurrentTestPart(AssessmentItemStatus.ALL).
                groupBy {it.parentSection}.each {k, v ->

            testStatusModel << [(Consts.identifier): k, (Consts.isSectionTitle): true];
            testStatusModel << v.flatten().collect {it.toPropertiesMap() + [(Consts.isSectionTitle): false]};
        }
        return testStatusModel.flatten();
    }

    private List<SectionPartStatus> getSectionPartsStatus(SectionPart section, String parentSection, SectionPartStatus.Position position) {
        List<SectionPartStatus> sectionPartStatusList = [];
        String identifier = section.identifier;
        SectionPartStatus.Position currentPosition = position;
        if (section.classTag.equalsIgnoreCase('assessmentSection')) {
            section.children.each {
                String sectionTitle = section.attributes.get('title').valueToString();
                List<SectionPartStatus> childPartStatusList = getSectionPartsStatus(it, SectionPartStatus.formatParentSection(parentSection, sectionTitle), currentPosition).flatten();
                sectionPartStatusList << childPartStatusList;
                if (!childPartStatusList.isEmpty() && !childPartStatusList[childPartStatusList.size() - 1].isPositionedBeforeCurrent()) {
                    currentPosition = SectionPartStatus.Position.AFTER;
                }
            }
        } else {
            currentPosition = currentItemIdentifier == identifier ? SectionPartStatus.Position.CURRENT : position;
            AssessmentItemStatus itemStatus = getAssessmentItemStatus(identifier);
            if (shouldAddSectionPartStatus(currentPosition, itemStatus)) {
                boolean isSectionPartStatusEnabled = isSectionPartStatusEnabled(currentPosition, itemStatus)
                if (!isSectionPartStatusEnabled) {
                    //disable all previously added items
                    sectionPartStatusList.each {
                        it.enabled = false;
                    }
                }
                sectionPartStatusList << new SectionPartStatus(identifier, parentSection, itemStatus, currentPosition, isSectionPartStatusEnabled);
            }
        }
        return sectionPartStatusList;
    }

    private boolean shouldAddSectionPartStatus(SectionPartStatus.Position position, AssessmentItemStatus itemStatus) {
        boolean shouldAddSectionPartStatus = true;
        if (position == SectionPartStatus.Position.AFTER) {
            shouldAddSectionPartStatus = nextEnabled();
        }
        if (shouldAddSectionPartStatus && canJumpWithoutPresenting()) {
            //don't add any not presented items if canJumpWithoutPresenting is true
            shouldAddSectionPartStatus = (itemStatus != AssessmentItemStatus.NOT_PRESENTED)
        }

        return shouldAddSectionPartStatus;
    }

    private boolean isSectionPartStatusEnabled(SectionPartStatus.Position position, AssessmentItemStatus itemStatus) {
        boolean enabled = true;
        if (itemStatus == AssessmentItemStatus.TIMED_OUT) {
            enabled = false;
        }
        //TODO P1 IF REVIEW IS DISABLED, USER CAN'T CLICK ON UN RESPONDED ITEMS, ALTHOUGTH HE CAN NAVIGATE VIA BACK.
        if (position == SectionPartStatus.Position.BEFORE) {
            enabled = enabled && backwardEnabled() && currentItemRef.itemSessionControl.allowReview;
        }
        return enabled;
    }

    private boolean canJumpWithoutPresenting() {
        TestPart testPart = currentTestPart;
        return testPart && testPart.areJumpsEnabled();
    }
}
