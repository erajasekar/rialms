package com.rialms.assessment.item

import com.rialms.assessment.Feature

class Item {
    String dataPath;
    String dataFile;
    String title;

    static hasMany = [itemFeatures:ItemFeature]

    static constraints = {
        dataPath(unique: ['dataFile'])
    }

    public Set<Feature> getFeatures() {
        itemFeatures.collect { it.feature } as Set
    }

    public Set<String> getFeatureNames() {
        itemFeatures.collect { it.feature.name } as Set
    }
}
