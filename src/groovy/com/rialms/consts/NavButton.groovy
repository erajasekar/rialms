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
    
    public String getId(){
        return name();
    }
    
    public String getName(){
        return name();
    }

    public String getValue(){
        return name().capitalize();
    }
}