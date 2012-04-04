package com.rialms.assessment.render

import com.rialms.consts.VisibilityMode
import com.rialms.consts.Tag

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/4/12
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
class HiddenElement {
    private Tag tag;
    private String identifier;
    private String valueLookUpKey;
    private VisibilityMode visibilityMode;
    private String elementId;

    public HiddenElement(String identifier, String valueLookUpKey, Tag tag, String visibilityMode) {
        this.identifier = identifier;
        this.valueLookUpKey = valueLookUpKey
        this.tag = tag;
        this.visibilityMode = com.rialms.consts.VisibilityMode.valueOfString(visibilityMode)
        this.elementId = "${tag}-${identifier}-${this.visibilityMode.name()}"
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValueLookUpKey() {
        return valueLookUpKey;
    }

    public String getElementId() {
        return elementId;
    }

    public boolean isVisible(Map<String, String> identifierValues) {
        println "identifierValues ${identifierValues}"
        boolean match = false;
        if (identifierValues[valueLookUpKey]?.split(',')?.contains(identifier)) {
            match = true;
        }
        println "match ${match}"
        switch (visibilityMode) {
            case VisibilityMode.SHOW_IF_MATCH: return match;
            case VisibilityMode.HIDE_IF_MATCH: return !match;
        }

        return match;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "HiddenElement{" +
                "elementId='" + elementId + '\'' +
                ", valueLookUpKey='" + valueLookUpKey + '\'' +
                '}';
    }


}
