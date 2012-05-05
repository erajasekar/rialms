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

    private static final String PARENT_SECTION_DELIMITER = ' > ';
    private String identifier;
    private String parentSection;
    private AssessmentItemStatus status;
    private Position position;
    private boolean enabled;

    public SectionPartStatus(String identifier, String parentSection, AssessmentItemStatus status = AssessmentItemStatus.NOT_PRESENTED, Position position = Position.BEFORE, boolean enabled = true) {
        this.identifier = identifier;
        this.parentSection = parentSection;
        this.status = (status ?: AssessmentItemStatus.NOT_PRESENTED);
        this.position = position;
        this.enabled = enabled;
    }

    public String getIdentifier() {
        return identifier
    }

    public String getParentSection() {
        return parentSection;
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

    public boolean isEnabled() {
        return enabled;
    }

    public Position getPosition() {
        return position;
    }

    public AssessmentItemStatus getStatus() {
        return status
    }

    public static String formatParentSection(String parentSection, String currentSection) {
        return (parentSection) ? "${parentSection}${PARENT_SECTION_DELIMITER}${currentSection}" : currentSection;
    }
}
