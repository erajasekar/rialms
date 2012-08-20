package com.rialms.assessment.item

import com.rialms.consts.Tag
import com.rialms.consts.Constants
import groovy.transform.ToString

/**
 * Created with IntelliJ IDEA.
 * User: Rajasekar Elango
 * Date: 8/16/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
@ToString(includeFields = true)
class InteractionHelp {
    private String titleMessageCode = "interaction.help.title";
    private String imageFile;
    private String id;
    private int height;
    private int width;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int DEFAULT_WIDTH = 500;

    private static Map<Tag,InteractionHelp> interactionHelps;
    public static final String IMAGE_DIR = "images/qti"


    static {
        interactionHelps = [(Tag.gapMatchInteraction): new InteractionHelp(Tag.gapMatchInteraction,'interaction.help.gapMatch.title',"gap-match-interaction-help.gif"),
                            (Tag.associateInteraction):new InteractionHelp(Tag.associateInteraction,'interaction.help.associate.title',"associate-interaction-help.gif", 400,500),
                            (Tag.matchInteraction):new InteractionHelp(Tag.matchInteraction,'interaction.help.match.title',"match-interaction-help.gif",300,650),
                            (Tag.orderInteraction):new InteractionHelp(Tag.orderInteraction,'interaction.help.order.title',"order-interaction-help.gif",300,520)
                            ];
    }

    public InteractionHelp(Tag tag, String titleMessageCode, String imageFile){
      this(tag,titleMessageCode,imageFile,DEFAULT_HEIGHT,DEFAULT_WIDTH)
    }

    public InteractionHelp(Tag tag, String titleMessageCode, String imageFile,int height, int width){
        id = "${Constants.interactionHelp}-${tag.name()}";
        this.titleMessageCode = titleMessageCode;
        this.imageFile = imageFile
        this.height = height;
        this.width = width;

    }

    public static InteractionHelp forTag(Tag tag){
        return interactionHelps[tag];
    }

    int getWidth() {
        return width
    }

    String getTitleMessageCode() {
        return titleMessageCode
    }

    String getImageFile() {
        return imageFile
    }

    String getId() {
        return id
    }

    int getHeight() {
        return height
    }
}
