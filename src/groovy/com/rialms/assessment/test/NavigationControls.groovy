package com.rialms.assessment.test

import com.rialms.consts.NavButton

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/18/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
class NavigationControls {

    private EnumMap<NavButton, Boolean> buttonStates = new EnumMap<NavButton, Boolean>(NavButton.class);

    public NavigationControls(){}

    //Add all buttons with given state.
    public NavigationControls(boolean visible){
        NavButton.values().each { b->
            buttonStates[b] = visible;
        }
    }
    public NavigationControls addButtonState(NavButton navButton, boolean visible) {
        buttonStates[navButton] = visible;
        return this;
    }

    public boolean getButtonState(NavButton navButton) {
        return buttonStates[navButton];
    }

    public EnumMap<NavButton, Boolean> getButtonStates() {
        return buttonStates;
    }

    public List<String> getVisibleButtonIds() {
        buttonStates.findAll {it.value}.collect{ "#${it.key.id}"}
    }

    public List<String> getHiddenButtonIds() {
        buttonStates.findAll {!it.value}.collect{ "#${it.key.id}"}
    }

    public Map<String,List<String>> getVisibleAndHiddenElementIds(){
        return [visibleElementIds: visibleButtonIds, hiddenElementIds: hiddenButtonIds]
    }

    @Override
    public String toString() {
        return "NavigationControls{" +
                "buttonStates=" + buttonStates +
                '}';
    }


}
