package com.rialms.consts

import groovy.xml.QName

/**
 * com.rialms.consts
 *
 * Created on 3/14/12 . 9:35 PM
 * @Author E. Rajasekar 
 *
 */
public enum Tag {
    p,
    h1,
    h2,
    h3,
    h4,
    h5,
    ul,
    hr,
    pre,
    br,
    dl,
    ol,
    em,
    a,
    blockquote,
    code,
    span,
    sub,
    acronym,
    tt,
    big,
    kbd,
    q,
    i,
    dfn,
    abbr,
    strong,
    sup,
    small,
    samp,
    b,
    cite,
    table,
    div,
    img,
    prompt,
    simpleChoice,
    inlineChoice,
    textEntryInteraction,
    choiceInteraction,
    inlineChoiceInteraction,
    feedbackBlock,
    feedbackInline,
    templateBlock,
    templateInline,
    printedVariable;

    private static Map<String, Tag> valuesByLowerCase = intValuesByLowerCase();
    private static final EnumSet<Tag> mixedTags = EnumSet.range(p, div);
    private static final EnumSet<Tag> feedBackTags = EnumSet.of(feedbackBlock, feedbackInline);
    private static final EnumSet<Tag> templateTags = EnumSet.of(templateBlock, templateInline);

    private static Map<String, Tag> intValuesByLowerCase() {
        Map<String, Tag> map = [:]
        values().each { Tag t ->
            map[t.name().toLowerCase()] = t;
        }
        return map;
    }

    public boolean equals(QName qn) {
        return name().equalsIgnoreCase(qn.getLocalPart());
    }

    public static Set<Tag> mixedTags() {
        return mixedTags
    }

    public static Set<Tag> feedBackTags() {
        return feedBackTags;
    }

    public static Set<Tag> templateTags() {
        return templateTags;
    }

    public static boolean isMixedTag(Tag t) {
        return mixedTags().contains(t);
    }

    public static boolean isFeedBackTag(Tag t) {
        return feedBackTags().contains(t);
    }

    public static boolean isTemplateTag(Tag t) {
        return templateTags().contains(t);
    }

    public static Tag valueOf(QName qn) {
        return valuesByLowerCase[qn.localPart.toLowerCase()]
    }
}
