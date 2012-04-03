package com.rialms.assessment.item

import com.rialms.util.QtiUtils


class Item {

    String dataPath;
    String dataFile;
    ItemService itemService;

    static constraints = { }
    static transients = ['itemService']

    public String getTitle(){
        return QtiUtils.getTitleFromXml(itemService.getItemDataFile(this));
    }

}
