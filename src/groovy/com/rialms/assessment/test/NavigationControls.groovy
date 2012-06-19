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

    public Map getNavButtonStates(){
        return buttonStates.collectEntries{button,state -> [button.name,state]};
    }

    @Override
    public String toString() {
        return "NavigationControls{" +
                "buttonStates=" + buttonStates +
                '}';
    }


}
