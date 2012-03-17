package com.rialms.util

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class CollectionUtilsTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testGetValuesOrderedByPosition() {
        Map input = [5:"e", 1:"a",3:"c",2:"b",4:"d"];
        List output = CollectionUtils.orderValuesByPosition(input);
        assertEquals("testGetValuesOrderedByPosition Failed" , ["a","b","c","d","e"], output);
    }
    
    void testShuffleWithFixedPositions(){
        List input = [10,15,20,30,50];
        Map fixedItems = [2:14, 5:23];
        List output = CollectionUtils.shuffleWithFixedPositions(input,fixedItems);
        String msg = "testShuffleWithFixedPositions Failed"
        assertEquals(msg , fixedItems[2],output[2]);
        assertEquals(msg , fixedItems[5],output[5]);
    }
}
