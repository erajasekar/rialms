package com.rialms.assessment.render

import com.rialms.consts.VisibilityMode
import com.rialms.consts.Tag
import com.rialms.consts.Constants as Consts
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

    public enum ValueLookUpType{
        Template,
        Outcome
        //TODO remove if not used
        public ValueLookUpType forIdentifierName(String identifierName){
            if (identifierName == Consts.templateIdentifier) {
                return Template;
            }else if (identifierName == Consts.outcomeIdentifier){
                return Outcome;
            }else{
                throw new IllegalArgumentException("Invalid identifierName ${identifierName}, should be ${Consts.templateIdentifier} or ${Consts.outcomeIdentifier}")
            }
        }
    };
    private Tag tag;
    private String identifier;
    private String valueLookUpKey;
    private VisibilityMode visibilityMode;
    private String elementId;
    private ValueLookUpType valueLookUpType;

    public HiddenElement(String identifier, String valueLookUpKey, Tag tag, String visibilityMode,ValueLookUpType valueLookUpType) {
        this.identifier = identifier;
        this.valueLookUpKey = valueLookUpKey
        this.tag = tag;
        this.visibilityMode = com.rialms.consts.VisibilityMode.valueOfString(visibilityMode)
        this.valueLookUpType=valueLookUpType;
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

    public boolean isVisible(Map<String, String> outcomeValues, Map<String, String> templateValues){
        return valueLookUpType == valueLookUpType.Template ? isVisible(templateValues) : isVisible(outcomeValues)
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
