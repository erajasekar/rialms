package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 6/5/12
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum  HeaderButton {
    hint,
    solution;

    private static final EnumMap<HeaderButton, String> icons = new EnumMap<HeaderButton, String>(HeaderButton.class);

    static {
        icons[hint] = 'icon-question-sign';
        icons[solution] = 'icon-book';
    }

    public String getIconClass(){
        return icons[this];
    }

    public boolean isHint(){
        return this == hint;
    }

    public boolean isSolution(){
        return this == solution;
    }

    public String configIdentifier(){
        return "${name()}Identifier";
    }
}
