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
    p, img, textEntryInteraction, feedbackBlock;

    public boolean equals(QName qn){
          return name().equalsIgnoreCase(qn.getLocalPart());
    }
}
