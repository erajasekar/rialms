import com.rialms.assessment.Exercise

class BootStrap {

    def init = { servletContext ->

        new Exercise(dataFile:'perimeter1.xml').save();
    }
    def destroy = {
    }
}
