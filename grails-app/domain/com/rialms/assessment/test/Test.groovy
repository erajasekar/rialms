package com.rialms.assessment.test

import com.rialms.util.QtiUtils

class Test {

    String dataPath;
    String dataFile;
    TestService testService;

    static constraints = {
    }
    static transients = ['testService']

    public String getTitle(){
        return QtiUtils.getTitleFromXml(testService.getTestDataFile(this));
    }
}
