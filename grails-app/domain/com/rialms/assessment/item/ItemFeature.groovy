/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */

package com.rialms.assessment.item


import com.rialms.assessment.Feature

class ItemFeature implements Serializable{

    Feature feature;
    static belongsTo = [item:  Item]

    static mapping = {
        version false
    }
    static constraints = {
        item(unique: ['feature'])
    }

    public static List<Feature> getFeatures(){
        def criteria = ItemFeature.createCriteria()
        def distinctFeatures = criteria.list {
            projections {
                distinct "feature"
            }
        }
        return distinctFeatures;
    }
}
