package com.rialms.assessment.item

import com.rialms.assessment.Feature

class Item {
    String dataPath;
    String dataFile;
    String title;

    static constraints = {
        dataPath(unique: ['dataFile'])
    }

    public Set<Feature> getFeatures() {
        ItemFeature.findAllByItem(this).collect { it.feature } as Set
    }
}
