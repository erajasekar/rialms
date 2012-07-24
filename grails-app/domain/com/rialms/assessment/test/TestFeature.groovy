/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */

package com.rialms.assessment.test

import com.rialms.assessment.Feature;

class TestFeature implements Serializable{

    Feature feature;
    static belongsTo = [test:Test]

    static constraints = {
        test(unique: ['feature'])
    }

    static mapping = {
        version false
    }

    public static List<Feature> getFeatures(){
        def criteria = TestFeature.createCriteria()
        def distinctFeatures = criteria.list {
            projections {
                distinct "feature"
            }
        }
        return distinctFeatures;
    }
}

