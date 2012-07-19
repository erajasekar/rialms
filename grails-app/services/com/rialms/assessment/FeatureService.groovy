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
           createFeature(feature.name, feature.relatesTo, feature.description);
        }

    }

    private Feature createFeature(String name,String relatesTo, String description){
        Feature f = new Feature(name: name,description: description,relatesTo: relatesTo);
        f.save();
        Errors errors = f.errors;
        if (errors && !errors.allErrors.isEmpty()){
           log.error("Errors in creating feature : ${f.errors}")
        }
    }
}
