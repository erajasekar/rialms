package com.rialms.assessment.test

import groovy.transform.ToString
import com.rialms.consts.AssessmentItemStatus

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 5/1/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
@ToString(includeFields = true)
class SectionPartStatus {

    public enum Position {
        BEFORE, CURRENT, AFTER
    };

    private String identifier;
    private boolean isItemRef;
    private AssessmentItemStatus status;
    private Position position;

    public SectionPartStatus(String identifier, isItemRef = false, AssessmentItemStatus status = AssessmentItemStatus.NOT_PRESENTED, Position position = Position.BEFORE) {
        this.identifier = identifier;
        this.isItemRef = isItemRef;
        this.status = (status ?: AssessmentItemStatus.NOT_PRESENTED);
        this.position = position;
    }

    public String getIdentifier() {
        return identifier
    }

    public boolean isItemRef() {
        return isItemRef
    }

    public boolean isCurrentItem() {
        return position == Position.CURRENT;
    }

    public boolean isPositionedBeforeCurrent() {
        return position == Position.BEFORE;
    }

    public boolean isPositionedAfterCurrent() {
        return position == Position.AFTER
    }

    public Position getPosition() {
        return position;
    }

    public AssessmentItemStatus getStatus() {
        return status
    }
}
