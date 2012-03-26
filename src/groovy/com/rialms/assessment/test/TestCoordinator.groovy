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


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.qtitools.qti.exception.QTIException;
import org.qtitools.qti.node.content.variable.RubricBlock;
import org.qtitools.qti.node.item.AssessmentItem;
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.ControlObject;
import org.qtitools.qti.node.test.SubmissionMode;
import org.qtitools.qti.node.test.TestFeedback;
import org.qtitools.qti.node.test.flow.ItemFlow;
import org.qtitools.qti.value.ListValue;
import org.qtitools.qti.value.OrderedValue;
import org.qtitools.qti.value.SingleValue;
import org.qtitools.qti.value.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import groovy.util.logging.*
import com.rialms.assessment.item.AssessmentItemInfo;

@Log4j
public class TestCoordinator implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
      * cached version of last rendered page, for quick reloads
      */
    private TestRenderInfo cachedTestRenderInfo = null;

    /*
      * Parameters for rendering the page (i.e. debug render, applet paths, etc)
      */
    private Map<String, Object> pageRenderParameters = null;

    /*
      * renderer view
      */
    protected String view;

    /*
      * controller object
      */
    public AssessmentTestController test;

    /*
      * rendering debug mode
      */
    private boolean debug = false;

    /*
      * rendering validation mode (if set to true, validation errors/warnings will be shown)
      */
    private boolean validate = false;

    /*
      * Storage for any messages that need to be displayed to the user in a one-off manner
      */
    private String flash = null;

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

    public void setPageRenderParameters(Map<String, Object> parameters) {
        pageRenderParameters = parameters;
    }

    /**
     * Enable or disable debugging
     * @param doDebug
     */
    public void setDebug(boolean doDebug) {
        if (debug != doDebug) {
            debug = doDebug;
            cachedTestRenderInfo = null;
        }
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

    //TODO: content is no more string
    public String flashMessage(String message) {
        flash = message;
        cachedTestRenderInfo = null;
        String content = getRenderableContent();
        flash = null;
        cachedTestRenderInfo = null;
        return content;
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

        if (test.isTestComplete()) {
//			ItemData id = new ItemData();
//			id.setImageBasePath(getImageBasePath());
//			id.setTestTitle(test.getTestTitle());
//			if (test.getAssessmentFeedback() != null)
//			id.getAssessmentFeedback().addAll(test.getAssessmentFeedback());
//			if (test.getTestPartFeedback() != null)
//			id.getTestPartFeedback().addAll(test.getTestPartFeedback());
//			cachedTestRenderInfo = RenderingEngine.renderItem(renderer, view, id);

            Map<String, Object> params = new HashMap<String, Object>();

            //set the test title from the cached version (to avoid lookups)
            params.put("title", test.getTestTitle());

            Node assessmentFeedback = convertFeedback(test.getAssessmentFeedback());
            if (assessmentFeedback != null) params.put("assessmentFeedback", assessmentFeedback);

            Node testPartFeedback = convertFeedback(test.getTestPartFeedback());
            if (testPartFeedback != null) params.put("testPartFeedback", testPartFeedback);

            Node outcomes = convertOutcomes(test.getTest().getOutcomeValues());
            if (outcomes != null) params.put("outcomeValues", outcomes);

            if (view != null) {
                params.put("view", view);
                pageRenderParameters.put("view", view);
            }
            //TODO find better way to handle blank
            renderContent(AssessmentItemInfo.BLANK_ITEM, params, pageRenderParameters, null);
        } else {
//			ItemSessionControl itemSessionControl = test.getCurrentItemSessionControl();

//			String identifier = test.getCurrentItemIdentifier();
//			byte [] render = null;
//			String href = test.getCurrentItemHREF();

            //this deals with content packages that have mutliple levels, and item images are relative to the item, not the test
//			String base = "";
//			if (href.lastIndexOf("/") != -1) {
//			base = href.substring(0, href.lastIndexOf("/"));
//			}
//			ai = new R2Q2AssessmentItem(new URI(href), testdir, itemSessionControl.getShowFeedback(), getImageBasePath()+base, test.getCurrentItemRef().getTemplateDefaults());
//			render = ai.render();

            //assemble and render (call service)
            renderContent(test.currentItemInfo, makeAssessmentParams(), pageRenderParameters, test.currentItemInfo.getResponseValues());
        }
    }

    private Map<String, Object> makeAssessmentParams() {
        Map<String, Object> params = new HashMap<String, Object>();

        //set the question id
        params.put("questionId", getCurrentQuestionId());

        //set the test title from the cached version (to avoid lookups)
        params.put("title", test.getTestTitle());

        //set the section titles
        Node sectionTitles = convertStringArray(test.getCurrentSectionTitles());
        if (sectionTitles != null) params.put("sectionTitles", sectionTitles);

        //set the rubric
        Node rubric = convertRubric(test.getRubricBlocks());
        if (rubric != null) params.put("rubric", rubric);

        Node assessmentFeedback = convertFeedback(test.getAssessmentFeedback());
        if (assessmentFeedback != null) params.put("assessmentFeedback", assessmentFeedback);

        Node testPartFeedback = convertFeedback(test.getTestPartFeedback());
        if (testPartFeedback != null) params.put("testPartFeedback", testPartFeedback);

        Node outcomes = convertOutcomes(test.getTest().getOutcomeValues());
        if (outcomes != null) params.put("outcomeValues", outcomes);

        Node outcomeDeclarations = convertOutcomeDeclarations(test.getTest().getOutcomeDeclarations());
        if (outcomeDeclarations != null) params.put("outcomeDeclarations", outcomeDeclarations);

        if (view != null) {
            params.put("view", view);
            pageRenderParameters.put("view", view);
        }

        //set the state for rendering controls
        params.put("previousEnabled", test.previousEnabled());
        params.put("backwardEnabled", test.backwardEnabled());
        params.put("nextEnabled", test.nextEnabled());
        params.put("forwardEnabled", test.forwardEnabled());
        params.put("submitEnabled", test.submitEnabled());
        params.put("skipEnabled", test.skipEnabled());

        params.put("numberSelected", test.getNumberSelected());
        params.put("numberRemaining", test.getNumberRemaining());
        params.put("timeSelected", test.getTimeSelected());
        params.put("timeRemaining", test.getTimeRemaining());

        if (test.getCurrentItemRef().getItemSessionControl().getAllowComment())
            params.put("allowCandidateComment", true);

        if (debug) params.put("debug", true);
        if (validate) {
            if (getTest().validate().getAllItems().size() != 0) {
                params.put("validation", getTest().validate().toString());
            }
        }
        if (flash != null) {
            params.put("flash", flash);
        }

        return params;
    }

    /*
      * convert an array of strings to a node (with children)
      */

    private Node convertStringArray(List<String> values) {
        if (values == null || values.size() == 0) return null;

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("root");
        doc.appendChild(root);

        for (String value: values) {
            Element child = doc.createElement("value");
            child.setTextContent(value.toString());
            root.appendChild(child);
        }

        return doc.getFirstChild();
    }

    /*
      * convert rubric to a node
      */

    private Node convertRubric(List<List<RubricBlock>> values) {
        if (values == null || values.size() == 0) return null;

        int count = 0;
        for (List<RubricBlock> v: values) count += v.size();
        if (count == 0) return null;

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("root");
        doc.appendChild(root);

        for (List<RubricBlock> section: values) {
            Element childsection = doc.createElement("section");

            for (RubricBlock block: section) {
                RubricBlock newblock = new RubricBlock(null);
                newblock.load(block.toXmlString());

                Node node = doc.adoptNode(newblock.getSourceNode());
                childsection.appendChild(node);
            }

            root.appendChild(childsection);
        }

        return doc.getFirstChild();
    }

    /*
      * convert a list of feedback to a node (with children)
      */

    private Node convertFeedback(List<TestFeedback> values) {
        if (values == null || values.size() == 0) return null;

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("root");
        doc.appendChild(root);

        for (TestFeedback value: values) {
            TestFeedback newValue = new TestFeedback(null);
            newValue.load(value.toXmlString());

            Node child = doc.adoptNode(newValue.getSourceNode());
            root.appendChild(child);
        }

        return doc.getFirstChild();
    }

    /*
      * convert a list of outcomeDeclarations to a node (with children)
      */

    private Node convertOutcomeDeclarations(List<OutcomeDeclaration> values) {
        if (values == null || values.size() == 0) return null;

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("root");
        doc.appendChild(root);

        for (OutcomeDeclaration value: values) {
            OutcomeDeclaration newValue = new OutcomeDeclaration(null);
            newValue.load(value.toXmlString());

            Node child = doc.adoptNode(newValue.getSourceNode());
            root.appendChild(child);
        }

        return doc.getFirstChild();
    }

    /*
      * convert outcomes to a node
      */

    private Node convertOutcomes(Map<String, Value> values) {
        if (values == null || values.size() == 0) return null;

        String elementName = "outcome";

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("root");
        doc.appendChild(root);

        for (String id: values.keySet()) {
            Element child = doc.createElement(elementName);
            child.setAttribute("identifier", id);

            Value value = values.get(id);
            if (value instanceof SingleValue) {
                Element v = doc.createElement("value");
                v.setTextContent(value.toString());
                child.appendChild(v);
            } else if (value instanceof ListValue) {
                for (int i = 0; i < ((ListValue) value).size(); i++) {
                    Element v = doc.createElement("value");
                    v.setTextContent(((ListValue) value).get(i).toString());
                    child.appendChild(v);
                }
            } else if (value instanceof OrderedValue) {
                for (int i = 0; i < ((OrderedValue) value).size(); i++) {
                    Element v = doc.createElement("value");
                    v.setTextContent(((ListValue) value).get(i).toString());
                    child.appendChild(v);
                }
            }

            root.appendChild(child);
        }

        return doc.getFirstChild();
    }

    private void renderContent(AssessmentItemInfo assessmentItemInfo, Map<String, Object> assessmentParams, Map<String, Object> pageParams, Map<String, Value> responses) {
        /*try {
              boolean isResponded;
              if (test.getCurrentItemRef() == null) {
                  isResponded = false;
              } else {
                  isResponded = test.getCurrentItemRef().isResponded();
              }
              cachedTestRenderInfo = renderer.renderPage(assessmentItem, isResponded, responses, assessmentParams, pageParams);
          } catch (Exception e) {
              e.printStackTrace();
          }
        System.out.println(assessmentItem.getTitle());
     //   System.out.println("Assess params " + assessmentParams);
        assessmentParams.each{
            println "${it.key} ==> ${it.value} ==> ${it.value.getClass()}";
        }
        System.out.println("paga params " + pageParams);
        System.out.println("resp " + responses);  */

        //TODO fix logging
        log.info("getCurrentItemRef ==> ${test.getCurrentItemRef()} , AssessmentItemInfo ==> ${assessmentItemInfo}");
        boolean isResponded;
        if (test.getCurrentItemRef() == null) {
            log.info("getCurrentItemRef() is null, returning NO_INFO");
            isResponded = false;
            cachedTestRenderInfo = TestRenderInfo.NO_INFO;
            return;
        } else {
            isResponded = test.getCurrentItemRef().isResponded();
        }

        if (!assessmentItemInfo) {
            log.info("assessmentItem is null, returning NO_INFO");
            cachedTestRenderInfo = TestRenderInfo.NO_INFO;
            return;
        }
        cachedTestRenderInfo = new TestRenderInfo(assessmentItemInfo, assessmentParams, pageParams, responses)

    }

    public String getCurrentQuestionId() {
        if (test.getCurrentItemRef() == null || isCompleted()) return null;
        return test.getCurrentItemRef().getIdentifier();
    }

    public void skipCurrentQuestion() throws QTIException {
        test.skipCurrentItem();
        getNextQuestion(false);
    }

    public void setCurrentResponse(Map params) throws FileNotFoundException, URISyntaxException, QTIException {
        test.setCurrentItemResponses(params);

        //TODO verify candidateComments work
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
                if (!test.getCurrentItemRef().isTimedOut())
                    test.getCurrentItemRef().timeOut();
            }

            if (!test.getItemFlow().hasNextItemRef(true)) {
                //write all vars

                for (AssessmentItemRef key: testPartItems.keySet()) {
                    key.setOutcomes(testPartItems.get(key));
                }
                test.getTest().processOutcome();
                testPartItems.clear();
            }
        }

        //set the render
        if ((test.getAssessmentFeedback() == null || test.getAssessmentFeedback().size() == 0) &&
                (test.getTestPartFeedback() == null || test.getTestPartFeedback().size() == 0) &&
                !test.currentItemInfo.itemBody.willShowFeedback() &&
                !test.currentItemInfo.adaptive &&
                test.getCurrentItemRef().isFinished()
        ) {
            //there is no feedback (at the test level), so we'll show the next item instead
            getNextQuestion(false);

            //TODO: this is broken if there is item feedback, as you won't see it!!! Also it will break adaptive items!!!
            //test.getCurrentItem().getItemBody().willShowFeedback()
            //test.getCurrentItem().getAdaptive()
        } else {
            renderContent(test.currentItemInfo, makeAssessmentParams(), pageRenderParameters, test.getCurrentItemResponses());
        }
    }

    /**
     * Get the test report xml string
     * @return report xml string
     */
    public String getReport() {
        return test.getReport();
    }
}
