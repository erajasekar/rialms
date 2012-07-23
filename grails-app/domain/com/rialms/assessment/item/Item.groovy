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
        ItemFeature.findAllByItem(this).collect { it.feature } as Set
    }

    public Set<String> getFeatureNames() {
        ItemFeature.findAllByItem(this).collect { it.feature.name } as Set
    }
}
