package com.rialms.consts

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import groovy.xml.QName

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class TagTests {

    void testValueOf() {
        String msg = "testValueOf Failed ";
        assertEquals(msg,Tag.p, Tag.valueOf( new QName("P")));
        assertEquals(msg,Tag.p, Tag.valueOf( new QName("p")));
        assertEquals(msg,Tag.textEntryInteraction, Tag.valueOf( new QName("TEXTENTRYINTERACTION")));
        assertEquals(msg,Tag.choiceInteraction, Tag.valueOf( new QName("choiceinteraction")));
    }
    
    void testIsMixedTag(){
        String msg = "testIsMixedTag Failed ";
        assertTrue(msg, Tag.isMixedTag(new QName("h1")));
        assertTrue(msg, Tag.isMixedTag(new QName("div")));
        assertFalse(msg, Tag.isMixedTag(new QName("textinteraction")));
    }
}
