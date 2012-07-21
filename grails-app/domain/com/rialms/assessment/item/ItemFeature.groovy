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

    Item item;
    Feature feature;

    static constraints = {
    }

    static mapping = {
        id composite: ['item', 'feature']
        version false
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
