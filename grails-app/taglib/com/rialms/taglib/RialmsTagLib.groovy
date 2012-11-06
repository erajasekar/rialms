package com.rialms.taglib

class RialmsTagLib {

    static namespace = "rs";

    def alertMsg = { attrs ->
        String tag = "alertMsg";
        String messageCode = getRequiredAttribute(attrs, 'messageCode', tag);
        out << """<a class="close" data-dismiss="alert" href="#">&times;</a>""";
        out << """<div class="alert alert-block">""";
        out << "<h4>";
        out << g.message(code: messageCode);
        out << "</h4>";
        out << "</div>";
    }

    private getAttribute(Map attrs, String name, String tagName, boolean isRequired = false) {
        if (isRequired && !attrs.containsKey(name)) {
            throwTagError("Tag [$tagName] is missing required attribute [$name]")
        }
        attrs.remove name
    }

    private getRequiredAttribute(Map attrs, String name, String tagName) {
        return getAttribute(attrs, name, tagName, true);
    }

    private getOptionalAttribute(Map attrs, String name) {
        return getAttribute(attrs, name, null, false);
    }
}
