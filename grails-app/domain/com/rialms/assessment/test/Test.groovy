package com.rialms.assessment.test

import com.rialms.assessment.Feature

class Test {

    String dataPath;
    String dataFile;
    String title;

    static constraints = {
        dataPath(unique: ['dataFile'])
    }

    public Set<Feature> getFeatures() {
        TestFeature.findAllByTest(this).collect { it.feature } as Set
    }

    public Set<String> getFeatureNames() {
        TestFeature.findAllByTest(this).collect { it.feature.name } as Set
    }
}
