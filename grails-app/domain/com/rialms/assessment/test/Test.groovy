package com.rialms.assessment.test

import com.rialms.assessment.Feature

class Test {

    String dataPath;
    String dataFile;
    String title;

    static hasMany = [testFeatures:TestFeature]

    static constraints = {
        dataPath(unique: ['dataFile'])
    }

    public Set<Feature> getFeatures() {
        testFeatures.collect { it.feature } as Set
    }

}
