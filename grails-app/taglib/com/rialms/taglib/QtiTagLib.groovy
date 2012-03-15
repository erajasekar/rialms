package com.rialms.taglib

class QtiTagLib {
    static namespace = "qti";

    def img = {  attrs ->

        String dir = getRequiredAttribute(attrs, 'dir', 'img' ) ;
        String file =  getRequiredAttribute(attrs, 'file', 'img' ) ;
        String fullPath = dir + file;

        int i = fullPath.lastIndexOf('/');
        Map fieldAttributes =  [dir:fullPath.substring(0,i), file:fullPath.substring(i+1)];

        def tagBody = {
           g.img(fieldAttributes);
        }
       renderTag(fieldAttributes, tagBody);
    }

     def textField = {  attrs ->

        def tagBody = {
           g.textField(attrs);
        }
       renderTag(attrs, tagBody);
    }

    private void renderTag(Map fieldAttributes, Closure tagBody){
           out <<   """  <div> ${tagBody()} </div>
           """  ;
    }

    protected getAttribute(attrs, String name, String tagName, boolean isRequired) {
        if (isRequired && !attrs.containsKey(name)) {
            throwTagError("Tag [$tagName] is missing required attribute [$name]")
        }
        attrs.remove name
    }

    protected getRequiredAttribute(attrs, String name, String tagName) {
        return getAttribute(attrs, name, tagName, true);
    }

}
