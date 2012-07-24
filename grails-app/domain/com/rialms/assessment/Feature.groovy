package com.rialms.assessment

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
class Feature {

    String name;
    String description;
    String relatesTo;//TODO p1 REMOVE IF NOT REQUIRED

    static constraints = {
        name (unique: true)
    }

}
