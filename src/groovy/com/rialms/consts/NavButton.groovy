package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/18/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public enum NavButton {
    previous, backward, next, forward, skip;
    private static final String ICON_WHITE = 'icon-white';

    private static final EnumMap<NavButton, String> icons = new EnumMap<NavButton, String>(NavButton.class);

    static {
        icons[forward] = 'icon-step-forward';
        icons[backward] = 'icon-step-backward';
        icons[previous] = 'icon-backward';
        icons[next] = 'icon-forward';
        icons[skip] = 'icon-flag';
    }

    public String getId() {
        return name();
    }

    public String getName() {
        return name();
    }

    public String getValue() {
        return name().capitalize();
    }

    public String getIconClass() {
        return "${icons[this]} ${ICON_WHITE}";
    }
    
    public boolean getAppendIcon(){
        return (this == next || this == forward);
    }
}