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
import org.qtitools.qti.node.test.flow.DefaultItemFlow;
import org.qtitools.qti.node.test.flow.ItemFlow;
import org.qtitools.qti.value.Value
import com.rialms.assessment.item.AssessmentItemInfo;

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

    AssessmentItemInfo currentItemInfo = null;

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


    public AssessmentItemInfo getCurrentItemInfo() {
        AssessmentItem currentItem = flow.getCurrentItemRef()?.getItem();
        if (currentItem) {
            if (currentItemInfo == null || !currentItemInfo.assessmentItem.is(currentItem)) {
                currentItemInfo = new AssessmentItemInfo(currentItem, dataPath);
            }
        }
        return currentItemInfo;
    }

    private AssessmentItemRef getPreviousItem(boolean includeFinished) {
        return flow.getPrevItemRef(includeFinished);
    }

    private AssessmentItemRef getNextItem(boolean includeFinished) {
        return flow.getNextItemRef(includeFinished);
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

    public String getCurrentItemHREF() throws QTIException {
        AssessmentItemRef air = getCurrentItemRef();
        if (air == null) return null;
        return air.getHref().toString();
    }

    public String getNextItemHREF(boolean includeFinished) throws QTIException {
        AssessmentItemRef air = getNextItem(includeFinished);
        if (air == null) return null;
        return air.getHref().toString();
    }

    public String getPrevItemHREF(boolean includeFinished) throws QTIException {
        AssessmentItemRef air = getPreviousItem(includeFinished);
        if (air == null) return null;
        return air.getHref().toString();
    }

    public ItemSessionControl getCurrentItemSessionControl() {
        if (getCurrentItemRef() != null)
            return getCurrentItemRef().getItemSessionControl();
        return null;
    }

    public void setCurrentItemResponses(Map params) throws QTIException {
        println "${currentItemInfo} ==> setCurrentItemResponses ==> ${params}"
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
        getCurrentItemRef().skip();

        getTest().processOutcome();
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
            log.debug("submitEnabled is " + se + " (" + getCurrentItemRef().getIdentifier() + ")");
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


    public String getReport() {
        return getTest().getAssessmentResult().toXmlString();
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
}
