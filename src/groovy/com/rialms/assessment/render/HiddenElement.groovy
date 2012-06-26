package com.rialms.assessment.render

import com.rialms.consts.VisibilityMode
import com.rialms.consts.Tag
import groovy.util.logging.Log4j

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/4/12
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
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
        this.elementId = "${tag}-${identifier}-${valueLookUpKey}-${visibilityMode}"
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
        log.info("DEBUG identifierValues ${identifierValues}" );
        boolean match = false;
        String identifierValue = identifierValues[valueLookUpKey];

        if (identifierValue){
            if (identifierValue.split(',').contains(identifier)) {
                match = true;
            }
            switch (visibilityMode) {
                case VisibilityMode.SHOW_IF_MATCH: return match;
                case VisibilityMode.HIDE_IF_MATCH: return !match;
            }
        }
        log.info(" DEBUG match ${elementId} ==> ${match}");
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
