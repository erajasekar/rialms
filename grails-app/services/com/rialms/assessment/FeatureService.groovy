package com.rialms.assessment

import org.springframework.validation.Errors

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
class FeatureService {

    public void createFeatures(){

        GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader);
        def conf = new ConfigSlurper().parse(classLoader.loadClass('FeaturesData'));

        conf.data.features.each{feature ->
           createFeature(feature.name,feature.description);
        }

    }

    private Feature createFeature(String name, String description){
        if (!Feature.findByNameAndDescription(name,description, [cache:true])){
            Feature f = new Feature(name: name,description: description);
            f.save();
            if (f.hasErrors()){
                log.error("Errors in creating feature : ${f.errors}")
            }
        }
    }
}
