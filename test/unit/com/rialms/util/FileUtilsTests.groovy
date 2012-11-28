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

    void testGetUniqueFile(){
        String msg = 'testGetUniqueFile Failed';
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        String testFileName = "testGetUniqueFile.txt";
        File expectedFile = new File(tmpDir, testFileName);
        assertEquals(msg, expectedFile.name, FileUtils.getUniqueFile(tmpDir,testFileName).name);
        expectedFile.createNewFile();
        for ( i in 1..3 ) {
            new File(tmpDir,"testGetUniqueFile-${i}.txt").createNewFile();
        }
        expectedFile = new File(tmpDir, "testGetUniqueFile-4.txt");

        File actualFile = FileUtils.getUniqueFile(tmpDir,"testGetUniqueFile.txt");

        assertEquals(msg, expectedFile.name, actualFile.name);

        String testDirName = "testGetUniqueFileDir"
        new File(tmpDir, testDirName).mkdir();
        new File(tmpDir, "${testDirName}-1").mkdir();
        expectedFile = new File(tmpDir, "${testDirName}-2");
        actualFile = FileUtils.getUniqueFile(tmpDir,testDirName);
        assertEquals(msg, expectedFile.name, actualFile.name);

        //test with . in dir name
        testDirName = "testGetUniqueFileDir.something"
        new File(tmpDir, testDirName).mkdir();
        new File(tmpDir, "${testDirName}-1").mkdir();
        expectedFile = new File(tmpDir, "${testDirName}-2");
        actualFile = FileUtils.getUniqueFile(tmpDir,testDirName);
        assertEquals(msg, expectedFile.name, actualFile.name);

        //cleanup
        tmpDir.eachFileMatch(~ "testGetUniqueFile.*txt"){  f ->
            f.delete();
        }
        tmpDir.eachDirMatch(~ "testGetUniqueFile.*"){  f ->
            f.delete();
        }
    }
}
