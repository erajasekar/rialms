import com.rialms.assessment.Exercise

class BootStrap {

    def init = { servletContext ->

        new Exercise(title:'Exercise 1 - Perimeter' , dataPath: 'exercise', dataFile:'perimeter1.xml').save();
        new Exercise(title:'choice fixed' , dataPath: 'qti', dataFile:'choice_fixed.xml').save();
        new Exercise(title:'choice multiple' , dataPath: 'qti', dataFile:'choice_multiple.xml').save();

    }
    def destroy = {
    }
}
