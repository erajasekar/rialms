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

package com.rialms.qti.node.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qtitools.qti.JQTI;
import org.qtitools.qti.attribute.value.StringAttribute;
import org.qtitools.qti.group.outcome.declaration.OutcomeDeclarationGroup;
import org.qtitools.qti.group.outcome.processing.OutcomeProcessingGroup;
import org.qtitools.qti.group.test.TestFeedbackGroup;
import org.qtitools.qti.group.test.TestPartGroup;
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration;
import org.qtitools.qti.node.outcome.processing.OutcomeProcessing;
import org.qtitools.qti.node.result.AssessmentResult;
import org.qtitools.qti.node.result.OutcomeVariable;
import org.qtitools.qti.node.result.TestResult;
import org.qtitools.qti.value.DurationValue;
import org.qtitools.qti.value.Value;

/**
 * A test is A group of assessmentItems with an associated set of rules that determine which of the items the candidate sees,
 * in what order, and in what way the candidate interacts with them. The rules describe the valid paths through the test,
 * when responses are submitted for response processing and when (if at all) feedback is to be given.
 * 
 * @author Jiri Kajaba
 * @author Jonathon Hare
 */
public class AssessmentTest extends ControlObject
{
	private static final long serialVersionUID = 1L;
	
	/** Name of this class in xml schema. */
	public static final String CLASS_TAG = "assessmentTest";

	/** Header of xml file. */
	public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/** Name of xmlns attribute in xml schema. */
	public static final String ATTR_DEFAULT_NAME_SPACE_NAME = "xmlns";
	/** Value of xmlns attribute. */
	public static final String ATTR_DEFAULT_NAME_SPACE_VALUE = "http://www.imsglobal.org/xsd/imsqti_v2p1";

	/** Name of xmlns:xsi attribute in xml schema. */
	public static final String ATTR_XSI_NAME_SPACE_NAME = "xmlns:xsi";
	/** Value of xmlns:xsi attribute. */
	public static final String ATTR_XSI_NAME_SPACE_VALUE = "http://www.w3.org/2001/XMLSchema-instance";

	/** Name of xsi:schemaLocation attribute in xml schema. */
	public static final String ATTR_XSI_SCHEMA_LOCATION_NAME = "xsi:schemaLocation";
	/** Value of xsi:schemaLocation attribute. */
	public static final String ATTR_XSI_SCHEMA_LOCATION_VALUE = "http://www.imsglobal.org/xsd/imsqti_v2p1 imsqti_v2p1.xsd";

	/** Name of title attribute in xml schema. */
	public static final String ATTR_TITLE_NAME = "title";

	/** Name of toolName attribute in xml schema. */
	public static final String ATTR_TOOL_NAME_NAME = "toolName";
	/** Default value of toolName attribute. */
	public static final String ATTR_TOOL_NAME_DEFAULT_VALUE = null;

	/** Name of toolVersion attribute in xml schema. */
	public static final String ATTR_TOOL_VERSION_NAME = "toolVersion";
	/** Default value of toolVersion attribute. */
	public static final String ATTR_TOOL_VERSION_DEFAULT_VALUE = null;

	/**
	 * Provides current time.
	 * This approach is because of testing. Timer for automated testing purposes can return discrete values
	 * instead of real time. You do not need to modify timer in real test.
	 */
	private Timer timer;

	/**
	 * Constructs assessmentTest.
	 */
	public AssessmentTest()
	{
		super(null); // Test doesn't have any parent.

		getAttributes().add(0, new StringAttribute(this, ATTR_DEFAULT_NAME_SPACE_NAME, ATTR_DEFAULT_NAME_SPACE_VALUE, null, true));
		getAttributes().add(1, new StringAttribute(this, ATTR_XSI_NAME_SPACE_NAME, ATTR_XSI_NAME_SPACE_VALUE, null, true));
		getAttributes().add(2, new StringAttribute(this, ATTR_XSI_SCHEMA_LOCATION_NAME, ATTR_XSI_SCHEMA_LOCATION_VALUE, null, true));

		getAttributes().add(new StringAttribute(this, ATTR_TITLE_NAME));
		getAttributes().add(new StringAttribute(this, ATTR_TOOL_NAME_NAME, ATTR_TOOL_NAME_DEFAULT_VALUE));
		getAttributes().add(new StringAttribute(this, ATTR_TOOL_VERSION_NAME, ATTR_TOOL_VERSION_DEFAULT_VALUE));

		getNodeGroups().add(0, new OutcomeDeclarationGroup(this));
		getNodeGroups().add(new TestPartGroup(this));
		getNodeGroups().add(new OutcomeProcessingGroup(this));
		getNodeGroups().add(new TestFeedbackGroup(this));

		timer = new Timer();
	}

	/**
	 * Convenience constructor for assessmentTest.
	 * 
	 * Sets the JQTI toolName and toolVersion automatically
	 * 
	 * @param identifier Value of identifier attribute 
	 * @param title Value of title attribute
	 */
	public AssessmentTest(String identifier, String title) {
		this();
		
		setIdentifier(identifier);
		setTitle(title);
		
		setToolName(JQTI.TOOL_NAME);
		setToolVersion(JQTI.TOOL_VERSION);
	}
	
	@Override
	public String getClassTag()
	{
		return CLASS_TAG;
	}

	@Override
	public List<TestPart> getChildren()
	{
		return getNodeGroups().getTestPartGroup().getTestParts();
	}

	/**
	 * Gets value of title attribute.
	 *
	 * @return value of title attribute
	 * @see #setTitle
	 */
	public String getTitle()
	{
		return getAttributes().getStringAttribute(ATTR_TITLE_NAME).getValue();
	}

	/**
	 * Sets new value of title attribute.
	 *
	 * @param title new value of title attribute
	 * @see #getTitle
	 */
	public void setTitle(String title)
	{
		getAttributes().getStringAttribute(ATTR_TITLE_NAME).setValue(title);
	}

	/**
	 * Gets value of toolName attribute.
	 *
	 * @return value of toolName attribute
	 * @see #setToolName
	 */
	public String getToolName()
	{
		return getAttributes().getStringAttribute(ATTR_TOOL_NAME_NAME).getValue();
	}

	/**
	 * Sets new value of toolName attribute.
	 *
	 * @param toolName new value of toolName attribute
	 * @see #getToolName
	 */
	public void setToolName(String toolName)
	{
		getAttributes().getStringAttribute(ATTR_TOOL_NAME_NAME).setValue(toolName);
	}

	/**
	 * Gets value of toolVersion attribute.
	 *
	 * @return value of toolVersion attribute
	 * @see #setToolVersion
	 */
	public String getToolVersion()
	{
		return getAttributes().getStringAttribute(ATTR_TOOL_VERSION_NAME).getValue();
	}

	/**
	 * Sets new value of toolVersion attribute.
	 *
	 * @param toolVersion new value of toolVersion attribute
	 * @see #getToolVersion
	 */
	public void setToolVersion(String toolVersion)
	{
		getAttributes().getStringAttribute(ATTR_TOOL_VERSION_NAME).setValue(toolVersion);
	}

	/**
	 * Gets outcomeDeclaration children.
	 *
	 * @return outcomeDeclaration children
	 */
	public List<OutcomeDeclaration> getOutcomeDeclarations()
	{
		return getNodeGroups().getOutcomeDeclarationGroup().getOutcomeDeclarations();
	}

	/**
	 * Gets outcomeDeclaration with given identifier or null.
	 *
	 * @param identifier given identifier
	 * @return outcomeDeclaration with given identifier or null
	 */
	public OutcomeDeclaration getOutcomeDeclaration(String identifier)
	{
		for (OutcomeDeclaration declaration : getOutcomeDeclarations())
			if (declaration.getIdentifier() != null && declaration.getIdentifier().equals(identifier))
				return declaration;

		return null;
	}

	/**
	 * Gets value of outcomeDeclaration with given identifier or null.
	 *
	 * @param identifier given identifier
	 * @return value of outcomeDeclaration with given identifier or null
	 */
	public Value getOutcomeValue(String identifier)
	{
		OutcomeDeclaration declaration = getOutcomeDeclaration(identifier);

		return (declaration != null) ? declaration.getValue() : null;
	}
	
	/**
	 * Returns an unmodifiable map of outcome identifiers to outcome values.
	 * 
	 * @return Unmodifiable map of outcome values. 
	 */
	public Map<String, Value> getOutcomeValues() {
		List<OutcomeDeclaration> declarations = getOutcomeDeclarations();
		Map<String, Value> values = new HashMap<String, Value>();
		
		for (OutcomeDeclaration declaration : declarations) {
			values.put(declaration.getIdentifier(), declaration.getValue());
		}
		
		return Collections.unmodifiableMap(values);
	}

	/**
	 * Gets testPart children.
	 *
	 * @return testPart children
	 */
	public List<TestPart> getTestParts()
	{
		return getNodeGroups().getTestPartGroup().getTestParts();
	}

	/**
	 * Gets outcomeProcessing child.
	 *
	 * @return outcomeProcessing child
	 * @see #setOutcomeProcessing
	 */
	public OutcomeProcessing getOutcomeProcessing()
	{
		return getNodeGroups().getOutcomeProcessingGroup().getOutcomeProcessing();
	}

	/**
	 * Sets new outcomeProcessing child.
	 *
	 * @param outcomeProcessing new outcomeProcessing child
	 * @see #getOutcomeProcessing
	 */
	public void setOutcomeProcessing(OutcomeProcessing outcomeProcessing)
	{
		getNodeGroups().getOutcomeProcessingGroup().setOutcomeProcessing(outcomeProcessing);
	}

	/**
	 * Evaluates outcome processing.
	 * <ol>
	 * <li>Resets outcomes to default values.</li>
	 * <li>Evaluates outcome processing.</li>
	 * </ol>
	 */
	public void processOutcome()
	{
		for (OutcomeDeclaration outcomeDeclaration : getOutcomeDeclarations())
			outcomeDeclaration.resetValue();

		OutcomeProcessing outcomeProcessing = getOutcomeProcessing();
		if (outcomeProcessing != null)
			outcomeProcessing.evaluate();
	}

	/**
	 * Gets testFeedback children.
	 *
	 * @return testFeedback children
	 */
	public List<TestFeedback> getTestFeedbacks()
	{
		return getNodeGroups().getTestFeedbackGroup().getTestFeedbacks();
	}

	/**
	 * Gets all viewable testFeedbacks with given access.
	 * Tests if feedbacks can be displayed.
	 *
	 * @param requestedAccess given access
	 * @return all testFeedbacks with given access
	 */
	public List<TestFeedback> getTestFeedbacks(TestFeedbackAccess requestedAccess)
	{
		List<TestFeedback> result = new ArrayList<TestFeedback>();

		for (TestFeedback feedback : getTestFeedbacks())
			if (feedback.isVisible(requestedAccess))
				result.add(feedback);

		return result;
	}

	/**
	 * Gets current timer.
	 *
	 * @return current timer
	 * @see #setTimer
	 */
	public Timer getTimer()
	{
		return timer;
	}

	/**
	 * Sets new timer.
	 *
	 * @param timer new timer
	 * @see #getTimer
	 */
	public void setTimer(Timer timer)
	{
		this.timer = timer;
	}

	@Override
	public String toXmlString(int depth, boolean printDefaultAttributes)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(XML);
		builder.append(NEW_LINE);
		builder.append(super.toXmlString(depth, printDefaultAttributes));

		return builder.toString();
	}

	@Override
	public void initialize()
	{
		super.initialize();

		// Initialises outcomeDeclaration's values.
		for (OutcomeDeclaration outcomeDeclaration : getOutcomeDeclarations())
			outcomeDeclaration.resetValue();
	}

	/**
	 * Returns current result of this test (only test itself, no items).
	 *
	 * @param parent parent of created result
	 * @return current result of this test (only test itself, no items)
	 */
	public TestResult getTestResult(AssessmentResult parent)
	{
		TestResult result = new TestResult(parent);

		result.setIdentifier(getIdentifier());
		result.setDateStamp(new Date());

		for (OutcomeDeclaration declaration : getOutcomeDeclarations())
		{
			OutcomeVariable variable = new OutcomeVariable(result, declaration, null);
			result.getItemVariables().add(variable);
		}

		result.getItemVariables().add(new OutcomeVariable(result, VARIABLE_DURATION_NAME, new DurationValue(getDuration() / 1000.0)));

		for (TestPart testPart : getTestParts())
			processDuration(result, testPart);

		return result;
	}

	private void processDuration(TestResult result, AbstractPart parent)
	{
		if (!(parent instanceof AssessmentItemRef))
		{
			String identifier = parent.getIdentifier() + "." + VARIABLE_DURATION_NAME;
			DurationValue duration = new DurationValue(parent.getDuration() / 1000.0);

			result.getItemVariables().add(new OutcomeVariable(result, identifier, duration));
		}

		for (AbstractPart child : parent.getChildren())
			processDuration(result, child);
	}

	/**
	 * Returns current result of whole assessment (test and all its items).
	 *
	 * @return current result of whole assessment (test and all its items)
	 */
	public AssessmentResult getAssessmentResult()
	{
		AssessmentResult result = new AssessmentResult();

		result.setTestResult(getTestResult(result));

		List<AssessmentItemRef> itemRefs = lookupItemRefs(null);
		int sequenceIndex = 1;
		for (AssessmentItemRef itemRef : itemRefs)
			result.getItemResults().addAll(itemRef.getItemResult(result, sequenceIndex++, null));

		return result;
	}
}
