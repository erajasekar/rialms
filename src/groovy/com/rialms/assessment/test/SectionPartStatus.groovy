package com.rialms.assessment.test

import groovy.transform.ToString
import com.rialms.consts.AssessmentItemStatus
import groovy.transform.EqualsAndHashCode
import com.rialms.consts.Constants

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

    public static final String PARENT_SECTION_DELIMITER = ' > ';
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
        //Always disable for current position.
        this.enabled = enabled && position != Position.CURRENT;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, Object> toPropertiesMap() {
        return [(Constants.identifier): identifier,
                (Constants.isCurrent): isCurrentItem(),
                (Constants.isPositionedAfterCurrent): isPositionedAfterCurrent(),
                (Constants.status): AssessmentItemStatus.format(status),
                (Constants.enabled): isEnabled(),
                (Constants.styleClass): status.statusClass
        ]
    }

    public boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        SectionPartStatus that = (SectionPartStatus) o

        if (enabled != that.enabled) return false
        if (identifier != that.identifier) return false
        if (parentSection != that.parentSection) return false
        if (position != that.position) return false
        if (status != that.status) return false

        return true
    }

    public int hashCode() {
        int result
        result = identifier.hashCode()
        result = 31 * result + parentSection.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + (enabled ? 1 : 0)
        return result
    }
}
