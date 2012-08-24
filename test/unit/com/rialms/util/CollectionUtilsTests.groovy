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
        Map input = [5: "e", 1: "a", 3: "c", 2: "b", 4: "d"];
        List output = CollectionUtils.orderValuesByPosition(input);
        assertEquals("testGetValuesOrderedByPosition Failed", ["a", "b", "c", "d", "e"], output);
    }

    void testShuffleWithFixedPositions() {
        List input = [10, 15, 20, 30, 50];
        Map fixedItems = [2: 14, 5: 23];
        List output = CollectionUtils.shuffleWithFixedPositions(input, fixedItems);
        String msg = "testShuffleWithFixedPositions Failed"
        assertEquals(msg, fixedItems[2], output[2]);
        assertEquals(msg, fixedItems[5], output[5]);
    }

    void testMergeMapsByKeyAsList() {
        String msg = "testMergeMapsByKeyAsList Failed"
        def a = [a: 1, b: 2, d: [5, 6]]
        def b = [b: 1, c: 3, d: []]
        def c = CollectionUtils.mergeMapsByKeyAsList(a, b);

        assertEquals(msg, [a: 1, b: [2, 1], c: 3, d: [5, 6]], c);
        assertEquals("${msg} on swapping params", [a: 1, b: [1, 2], c: 3, d: [5, 6]], CollectionUtils.mergeMapsByKeyAsList(b, a));
        a = [a:1 , b: 2, d: [e:1, f:2]];
        b = [b: 1, c: 3, d: [f: 3, g:5]];
        assertEquals("${msg} on multi dimensional map", [a: 1, b: [1, 2], c: 3, d: [e:1, f:2, g:5 ]], CollectionUtils.mergeMapsByKeyAsList(b, a))

        a = [a:1 , b: 2, d: [e:1, f:2]];
        b = [b: 1, c: 3, d: 5];
        try{
            CollectionUtils.mergeMapsByKeyAsList(b, a);
            fail("${msg} should have thrown IllegalArgument Exception for type mismatch");
        }catch (IllegalArgumentException e){
            String expectedMsg = 'Type mismatch';
            assertTrue("${msg} exception message did not match '${expectedMsg}'", e.getMessage().contains(expectedMsg))
        }

    }
}
