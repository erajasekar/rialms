package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 6/5/12
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EndAttemptButton {
    hint,
    solution,
    other;

    private static final EnumMap<EndAttemptButton, String> icons = new EnumMap<EndAttemptButton, String>(EndAttemptButton.class);

    static {
        icons[hint] = 'icon-question-sign';
        icons[solution] = 'icon-book';
        icons[other] = '';
    }

    public String getIconClass(){
        return icons[this];
    }
    //TODO remove unsed methods
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
