package com.rialms.consts

import groovy.xml.QName

/**
 * com.rialms.consts
 *
 * Created on 3/14/12 . 9:35 PM
 * @Author E. Rajasekar 
 * 
 */
public enum Tag {
    p,
    h1,
    h2,
    h3,
    h4,
    h5,
    ul,
    hr,
    pre,
    br,
    dl,
    ol,
    em,
    a,
    code,
    span,
    sub,
    acronym,
    tt,
    big,
    kbd,
    q,
    i,
    dfn,
    abbr,
    strong,
    sup,
    small,
    samp,
    b,
    cite,
    table,
    div,
    img,
    prompt,
    simpleChoice ,
    textEntryInteraction,
    choiceInteraction,
    feedbackBlock;

    public boolean equals(QName qn){
          return name().equalsIgnoreCase(qn.getLocalPart());
    }
    
    public static Set<Tag> mixedTags(){
        return EnumSet.range(p,div);
    }
    
    public static boolean isMixedTag(QName qn){
        boolean mixed = false;
        for(t in mixedTags()){
           if (t.equals(qn)){
               mixed = true;
               break;
           }
        }
        return mixed;
    }
}
