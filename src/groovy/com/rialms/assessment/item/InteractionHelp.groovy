package com.rialms.assessment.item

import com.rialms.consts.Tag

/**
 * Created with IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 8/16/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
class InteractionHelp {
    public static final String titleMessageCode = "interaction.help.title";
    public static final int height = 300;
    public static final int width = 500;

    private static Map<Tag,String> interactionHelps;
    public static final String IMAGE_DIR = "images/qti"


    static {
        interactionHelps = [(Tag.gapMatchInteraction): "gap-match-help.gif"];
    }

    public static Map<Tag,String> getAll(){
        return Collections.unmodifiableMap(interactionHelps)
    }

    public static String getHelpImage(Tag tag){
        return interactionHelps[tag];
    }
}
