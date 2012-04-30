package com.rialms.qti.node.test.flow;

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/30/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.Serializable;

import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.TestPart;

/**
 * Modified version of Item flow interface from qtitools.
 *
 * @author Rajasekar Elango
 */
public interface ItemFlow extends Serializable {
    /**
     * Returns true if there are no more item references to be presented; false otherwise.
     * <p/>
     * Convenient method for {@code getTest().isFinished()}.
     *
     * @return true if there are no more item references to be presented; false otherwise
     */
    public boolean isFinished();

    /**
     * Gets assessment test of this item flow.
     *
     * @return assessment test of this item flow
     */
    public AssessmentTest getTest();

    /**
     * Gets parent test part of current item reference (can be null).
     * <p/>
     * Result is null before test starts and after test finishes.
     *
     * @return parent test part of current item reference (can be null)
     */
    public TestPart getCurrentTestPart();

    /**
     * Gets current item reference (can be null).
     * <p/>
     * Result is null before test starts and after test finishes.
     *
     * @return current item reference (can be null)
     */
    public AssessmentItemRef getCurrentItemRef();

    /**
     * Returns true if there is any previous item reference in current test part; false otherwise.
     * <p/>
     * Previous item reference means any item reference which was presented before current item reference.
     * <p/>
     * Returns false before test starts and after test finishes (current test part is null).
     *
     * @param includeFinished whether consider already finished item references
     * @return true if there is any previous item reference in current test part; false otherwise
     */
    public boolean hasPrevItemRef(boolean includeFinished);

    /**
     * Gets first previous item reference in current test part (can be null).
     * <p/>
     * First previous item reference means item reference with the highest lower presented time than current item reference.
     * (First left item reference from current item reference on time axis.)
     *
     * @param includeFinished whether consider already finished item references
     * @return first previous item reference in current test part (can be null)
     */
    public AssessmentItemRef getPrevItemRef(boolean includeFinished);

    /**
     * Returns true if there is any next item reference in current test part; false otherwise.
     * <p/>
     * Next item reference means any item reference which was (or will be) presented after current item reference.
     * <p/>
     * Returns false before test starts and after test finishes (current test part is null).
     * <p/>
     * In linear individual mode this method can be called only when current item is finished!
     *
     * @param includeFinished whether consider already finished item references
     * @return true if there is any next item reference in current test part; false otherwise
     */
    public boolean hasNextItemRef(boolean includeFinished);

    /**
     * Gets first next item reference in test (can be null).
     * <p/>
     * First next item reference means item reference with the lowest higher presented time than current item reference.
     * (First right item reference from current item reference on time axis.)
     * <p/>
     * This is the only one method which can cross boundary of current test part.
     * Once boundary is crossed, there is no way how to go back!
     * <p/>
     * In linear individual mode this method can be called only when current item is finished!
     *
     * @param includeFinished whether consider already finished item references
     * @return first next item reference in test (can be null)
     */
    public AssessmentItemRef getNextItemRef(boolean includeFinished);

    /**
     * Gets item reference matching given identifier. if forward is true it will search in forward direction,
     * otherwise will look in backward direction.
     *
     * @param identifier
     * @param forward
     * @return
     */
    public AssessmentItemRef getItemRefByIdentifier(String identifier, boolean forward);
}

