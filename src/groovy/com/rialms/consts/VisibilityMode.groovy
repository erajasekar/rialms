package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/4/12
 * Time: 12:13 AM
 * To change this template use File | Settings | File Templates.
 */
public enum VisibilityMode
{
    SHOW_IF_MATCH("show"),

    HIDE_IF_MATCH("hide");

    private static Map<String, VisibilityMode> visibilityModes;

    static {
        visibilityModes = new HashMap<String, VisibilityMode>();

        for (VisibilityMode visibilityMode: VisibilityMode.values())
            visibilityModes.put(visibilityMode.visibilityMode, visibilityMode);
    }

    private String visibilityMode;

    private VisibilityMode(String visibilityMode) {
        this.visibilityMode = visibilityMode;
    }

    @Override
    public String toString() {
        return visibilityMode;
    }


    public static VisibilityMode valueOfString(String visibilityMode) {
        VisibilityMode result = visibilityModes.get(visibilityMode);

        if (result == null)
            throw new IllegalArgumentException("Invalid ${visibilityMode}");

        return result;
    }
}
