package com.rialms.qti

/**
 * Created with IntelliJ IDEA.
 * User: relango
 * Date: 9/4/12
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class QtiCompatibility {
    private String feature;
    private boolean supported;
    //TODO P3: remove supported if its never used.

    public QtiCompatibility(String feature, boolean supported){
        this.feature = feature;
        this.supported = supported;
    }

    public String getFeature() {
        return feature
    }

    public boolean isSupported() {
        return supported
    }

    public static List<QtiCompatibility> getItemCompatibilityList(){
        List<QtiCompatibility> result = [];
        result << new QtiCompatibility("Associate Interaction" , true);
        result << new QtiCompatibility("Choice Interaction" , true);
        result << new QtiCompatibility("End Attempt Interaction" , true);
        result << new QtiCompatibility("Gap Match Interaction" , true);
        result << new QtiCompatibility("Hot Text Interaction" , true);
        result << new QtiCompatibility("Inline Choice Interaction" , true);
        result << new QtiCompatibility("Match Interaction" , true);
        result << new QtiCompatibility("Order Interaction" , true);
        result << new QtiCompatibility("Text Entry Interaction" , true);
        result << new QtiCompatibility("Adaptive Questions" , true);
        result << new QtiCompatibility("Partial Scoring" , true);
        result << new QtiCompatibility("Questions Using Templates" , true);
        result << new QtiCompatibility("Provide Interactive Feedback" , true);
        result << new QtiCompatibility("Text Entry Interaction" , true);
        result << new QtiCompatibility("Mathematical Expressions Using MathML" , true);
        result << new QtiCompatibility("Provide Hint" , true);
        result << new QtiCompatibility("Provide Solution" , true);

        result << new QtiCompatibility("Custom Interaction" , false);
        result << new QtiCompatibility("Drawing Interaction" , false);
        result << new QtiCompatibility("Extended Text Interaction" , false);
        result << new QtiCompatibility("Graphic Gap Match Interaction" , false);
        result << new QtiCompatibility("Graphic Associate Interaction" , false);
        result << new QtiCompatibility("Graphic Order Interaction" , false);
        result << new QtiCompatibility("Hot Spot Interaction" , false);
        result << new QtiCompatibility("Media Interaction" , false);
        result << new QtiCompatibility("Position Object Interaction" , false);
        result << new QtiCompatibility("Select Point Interaction" , false);
        result << new QtiCompatibility("Slider Interaction" , false);
        result << new QtiCompatibility("Upload Interaction" , false);

        result;

    }

    public static List<QtiCompatibility> getTestCompatibilityList(){
        List<QtiCompatibility> result = [];
        result << new QtiCompatibility("Linear Navigation Mode" , true);
        result << new QtiCompatibility("Non Linear Navigation Mode" , true);
        result << new QtiCompatibility("Individal Submission Mode" , true);
        result << new QtiCompatibility("Simultaneous Submission Mode" , true);
        result << new QtiCompatibility("Limit Max Duration of Attempt" , true);
        result << new QtiCompatibility("Branch based on Response" , true);
        result << new QtiCompatibility("Allow/Disable Skipping a Question" , true);
        result << new QtiCompatibility("Allow/Disable Reviewing a Question" , true);
        result << new QtiCompatibility("Specify the Number of Allowed Attempts" , true);
        result << new QtiCompatibility("Questions Arranged into Parts/Sections within Tests" , true);

        result << new QtiCompatibility("Limit Min Duration of Attempt" , false);
        result << new QtiCompatibility("Statistics on Quesions Usage Data" , false);

        //TODO p2: Determine if weight is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest02.xml
        //TODO p2: Determine if category is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest03.xml
        //TODO p2: Determine if exitText is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest10.xml
        //TODO p2: Determine if required & keepTogether is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest12.xml
        //TODO p2: Determine if random & ordering is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest24.xml
        //TODO p2: Determine if variable mapping is supported in test http://www.imsglobal.org/question/qtiv2p1pd2/examples/tests/rtest26.xml

        result;

    }
}
