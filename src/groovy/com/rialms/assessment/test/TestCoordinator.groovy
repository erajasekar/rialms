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


import org.qtitools.qti.exception.QTIException;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.ControlObject;
import org.qtitools.qti.node.test.SubmissionMode;
import org.qtitools.qti.node.test.flow.ItemFlow;
import org.qtitools.qti.value.Value;
import groovy.util.logging.*
import com.rialms.assessment.item.AssessmentItemInfo
import com.rialms.util.QtiUtils;
import groovy.util.Node
import org.qtitools.qti.validation.ValidationItem
import com.rialms.consts.AssessmentItemStatus
import org.qtitools.qti.validation.ValidationResult
import groovy.xml.XmlUtil
import com.rialms.consts.NavButton;

@Log4j
public class TestCoordinator implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
      * cached version of last rendered page, for quick reloads
      */
    private TestRenderInfo cachedTestRenderInfo = null;

    /*
      * renderer view
      */
    protected String view;

    /*
      * controller object
      */
    public AssessmentTestController test;

    /*
    * rendering validation mode (if set to true, validation errors/warnings will be shown)
    *
    */
    private boolean validate = false;

    /*
      * Storage for responses in simultaneous mode
      */
    private Map<AssessmentItemRef, Map<String, Value>> testPartItems = new HashMap<AssessmentItemRef, Map<String, Value>>();

    /*
      * Maximum amount of time allowed for network delay
      */
    private static long magic_time = 15000;

    public TestCoordinator(File assessmentTestFile, String dataPath, String view) throws Exception {
        this.view = view;
        test = new AssessmentTestController(assessmentTestFile, dataPath);
    }

    /**
     * Enable or disable validation
     * @param doValidate true if validation is wanted, false otherwise
     */
    public void setValidate(boolean doValidate) {
        if (validate != doValidate) {
            validate = doValidate;
            cachedTestRenderInfo = null;
        }
    }

    /**
     * Determine if the test is completed
     * @return true if complete; false otherwise
     */
    public boolean isCompleted() {
        return test.isTestComplete();
    }

    /**
     * Get the underlying controller object
     * @return the test controller
     */
    public AssessmentTestController getTestController() {
        return test;
    }

    /**
     * Get the underlying AssessmentTest object
     * @return the assessmentTest
     */
    public AssessmentTest getTest() {
        return test.getTest();
    }

    /**
     * Get the underlying ItemFlow object
     * @return the itemFlow associated with the AssessmentTestController
     */
    public ItemFlow getFlow() {
        return test.getItemFlow();
    }

    public TestRenderInfo getTestRenderInfo() {
        if (cachedTestRenderInfo == null) {
            getCurrentQuestion();
        }
        return cachedTestRenderInfo;
    }

    public void getNextQuestion(boolean includeFinished) throws QTIException {
        test.getNextItemHREF(includeFinished);

        //invalidate renderedContent
        cachedTestRenderInfo = null;
    }

    public void getPreviousQuestion(boolean includeFinished) throws QTIException {
        test.getPrevItemHREF(includeFinished);

        //invalidate renderedContent
        cachedTestRenderInfo = null;
    }

    public void getCurrentQuestion() {
        log.info("getCurrentQuestion() - " + test.currentItemInfo);

        //if the renderedContent is still active, just return
        if (cachedTestRenderInfo != null) return;

        if (test.isTestComplete() || test.getCurrentItemRef().isTimedOut()) {
            //TODO doesn't work if no item is submitted
            if (testPartItems) {
                log.info("Finished simultaneous submission mode test, render pending submission content");
                // doSimultaneousSubmission();
                renderPendingSubmissionContent();
            } else {
                renderFeedbackContent();
            }
        } else {
            renderContent(test.currentItemInfo, makeAssessmentParams());
        }
    }

    private void renderPendingSubmissionContent() {
        Map<String, Object> params = new HashMap<String, Object>();

        //set the test title from the cached version (to avoid lookups)
        params.put("title", test.getTestTitle());
        params.put("outcomeValues", QtiUtils.convertQTITypesToParams(test.getTest().getOutcomeValues()));
        params.put("outcomeDeclarations", test.getTest().getOutcomeDeclarations());
        params.put("itemsPendingSubmission", test.itemsPendingSubmission);
        renderContent(AssessmentItemInfo.BLANK_ITEM, params);
    }

    private void renderFeedbackContent() {
        Map<String, Object> params = new HashMap<String, Object>();

        //set the test title from the cached version (to avoid lookups)
        params.put("title", test.getTestTitle());

        Node assessmentFeedback = QtiUtils.convertFeedbackToNode(test.getAssessmentFeedback());
        if (assessmentFeedback != null) params.put("assessmentFeedback", assessmentFeedback);

        Node testPartFeedback = QtiUtils.convertFeedbackToNode(test.getTestPartFeedback());
        if (testPartFeedback != null) params.put("testPartFeedback", testPartFeedback);

        params.put("outcomeValues", QtiUtils.convertQTITypesToParams(test.getTest().getOutcomeValues()));
        params.put("outcomeDeclarations", test.getTest().getOutcomeDeclarations());

        if (view != null) {
            params.put("view", view);
        }
        renderContent(AssessmentItemInfo.BLANK_ITEM, params);
    }

    private Map<String, Object> makeAssessmentParams() {
        Map<String, Object> params = new HashMap<String, Object>();

        //set the question id
        params.put("questionId", getCurrentQuestionId());

        //set the test title from the cached version (to avoid lookups)
        params.put("title", test.getTestTitle());

        //set the section titles
        Node sectionTitles = QtiUtils.convertStringArrayToNode(test.getCurrentSectionTitles());
        if (sectionTitles != null) params.put("sectionTitles", sectionTitles);

        //set the rubric
        Node rubric = QtiUtils.convertRubricToNode(test.getRubricBlocks());
        if (rubric != null) params.put("rubric", rubric);

        Node assessmentFeedback = QtiUtils.convertFeedbackToNode(test.getAssessmentFeedback());

        if (assessmentFeedback != null) params.put("assessmentFeedback", assessmentFeedback);

        Node testPartFeedback = QtiUtils.convertFeedbackToNode(test.getTestPartFeedback());
        if (testPartFeedback != null) params.put("testPartFeedback", testPartFeedback);

        params.put("outcomeValues", QtiUtils.convertQTITypesToParams(test.getTest().getOutcomeValues()));

        params.put("outcomeDeclarations", test.getTest().getOutcomeDeclarations());

        if (view != null) {
            params.put("view", view);
            pageRenderParameters.put("view", view);
        }
        //set the state for rendering controls
        params.put("submitEnabled", test.submitEnabled());
        NavigationControls controls = new NavigationControls();
        controls.addButtonState(NavButton.previous, test.previousEnabled());
        controls.addButtonState(NavButton.backward, test.backwardEnabled());
        controls.addButtonState(NavButton.next, test.nextEnabled());
        controls.addButtonState(NavButton.forward, test.forwardEnabled());
        controls.addButtonState(NavButton.skip, test.skipEnabled());
        params.put("navigationControls", controls);

        params.put("numberSelected", test.getNumberSelected());
        params.put("numberRemaining", test.getNumberRemaining());
        params.put("timeSelected", test.getTimeSelected());
        params.put("timeRemaining", test.getTimeRemaining());
        params.put("testStatus", test.testStatus)

        if (test.getCurrentItemRef().getItemSessionControl().getAllowComment())
            params.put("allowCandidateComment", true);

        if (validate) {
            params.put("validationResult", getTest().validate());
        } else {
            //Create empty Validation Result
            params.put("validationResult", new ValidationResult());
        }
        return params;
    }


    private void renderContent(AssessmentItemInfo assessmentItemInfo, Map<String, Object> assessmentParams) {
        //TODO fix logging
        log.info("IS BLANK ${assessmentItemInfo.is(AssessmentItemInfo.BLANK_ITEM)}");
        cachedTestRenderInfo = new TestRenderInfo(assessmentItemInfo, assessmentParams)

    }

    public String getCurrentQuestionId() {
        if (test.getCurrentItemRef() == null || isCompleted()) return null;
        return test.getCurrentItemRef().getIdentifier();
    }

    public void skipCurrentQuestion() throws QTIException {
        test.skipCurrentItem();
        getNextQuestion(false);
    }

    public boolean setCurrentResponse(Map params) throws FileNotFoundException, URISyntaxException, QTIException {

        test.setCurrentItemResponses(params);

        //TODO RENDER Input for canditate comments
        if (params.containsKey("candidateComment"))
            test.getCurrentItemRef().setCandidateComment(params.get("candidateComment").get(0));

        Map<String, Value> itemOutcomes = test.getCurrentItemRef().getItem().getOutcomeValues();

        boolean se = !test.getCurrentItemRef().isFinished();
        ControlObject co = TimeReport.getLowestRemainingTimeControlObject(test.getCurrentItemRef());
        if (co != null) {
            if (TimeReport.getRemainingTime(co) < (-1 * magic_time)) {
                se = false;
            }
        }

        if (test.getCurrentTestPart().getSubmissionMode() == SubmissionMode.INDIVIDUAL) {
            if (se) {
                test.setCurrentItemOutcomes(itemOutcomes);
            } else {
                if (!test.getCurrentItemRef().isTimedOut())
                    test.getCurrentItemRef().timeOut();
            }
        } else {
            if (se) {
                testPartItems.put(test.getCurrentItemRef(), itemOutcomes);
            } else {
                if (!test.getCurrentItemRef().isTimedOut()) {
                    test.getCurrentItemRef().timeOut();
                }
            }
            if (!test.getCurrentItemRef().passMaximumTimeLimit()) {
                println "RAJA 2"
                test.getCurrentItemRef().timeOut();
                log.info("Test timedout");
                cachedTestRenderInfo = null;
                getCurrentQuestion();
            }
            //TODO : remove commented code
            /*if (!test.getItemFlow().hasNextItemRef(true)) {
                doSimultaneousSubmission();
            } */
        }

        boolean shouldRenderNextItem = shouldRenderNextItem();
        if (shouldRenderNextItem) {
            //there is no feedback (at the test level), so we'll show the next item instead
            getNextQuestion(false);

            //TODO: this is broken if there is item feedback, as you won't see it!!! Also it will break adaptive items!!!
            //test.getCurrentItem().getItemBody().willShowFeedback()
            //test.getCurrentItem().getAdaptive()
        } else {
            renderContent(test.currentItemInfo, makeAssessmentParams());
        }
        return shouldRenderNextItem;
    }

    public void doSimultaneousSubmission() {
        //TODO Test it for multiple testPart
        log.info("Doing simultaneous submission");
        //write all vars
        for (AssessmentItemRef key: testPartItems.keySet()) {
            key.setOutcomes(testPartItems.get(key));
        }
        test.getTest().processOutcome();
        testPartItems.clear();
    }

    private boolean shouldRenderNextItem() {
        boolean hasNoFeedbackAndNotAdaptive = ((test.getAssessmentFeedback() == null || test.getAssessmentFeedback().size() == 0) &&
                (test.getTestPartFeedback() == null || test.getTestPartFeedback().size() == 0) &&
                !test.currentItemInfo.itemBody.willShowFeedback() &&
                !test.currentItemInfo.adaptive)
        log.info("hasNoFeedbackAndNotAdaptive = ${hasNoFeedbackAndNotAdaptive} <======> nextEnabled ${test.nextEnabled()}");
        return hasNoFeedbackAndNotAdaptive && test.nextEnabled();
    }
    /**
     * Get the test report xml string
     * @return report xml string
     */
    public String getReport() {
        return test.getReport();
    }
}