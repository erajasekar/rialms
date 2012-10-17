package com.rialms.util

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

/**
 * Created with IntelliJ IDEA.
 * User: relango
 * Date: 10/17/12
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
@TestMixin(GrailsUnitTestMixin)
class FileUtilsTests {
    void testGetBaseName(){
        String msg = 'testGetBaseName Failed';
        assertEquals(msg, 'packages-test1',FileUtils.getBaseName('packages-test1.zip')) ;

    }
}
