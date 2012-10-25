import groovy.io.FileType
import org.codehaus.groovy.grails.commons.GrailsResourceUtils
import java.util.regex.Pattern
import com.rialms.auth.UserRole
import com.rialms.auth.User
import com.rialms.auth.Role

class BootStrap {

    def testService;
    def itemService;
    def featureService;
    def packageService;
    def grailsApplication;

    def init = { servletContext ->
        initData();
    }
    def destroy = {
    }

    def initData() {
        initUsers();
        featureService.createFeatures();
        createItems();
        createTests();
        //TODO P1: REMOVE testService.createTest(packageService.unpackContent('content/packages/packages-test1.zip'));
    }

    def initUsers(){
        String password = 'spkavi22'

        def roleAdmin = new Role(authority: 'ROLE_ADMIN').save()
        def roleUser = new Role(authority: 'ROLE_USER').save()

        def user = new User(email: 'raja@rialms.com', name: 'Raja', password: password, enabled: true).save()
        def admin = new User(email: 'admin@rialms.com', name: 'Admin', password: password, enabled: true).save()

        UserRole.create user, roleUser
        UserRole.create admin, roleUser
        UserRole.create admin, roleAdmin, true
    }

    def createDemoItems() {
        String demoPath = grailsApplication.config.rialms.demoItemsPath;
        File demoDir = grailsApplication.parentContext.getResource("${grailsApplication.config.rialms.contentPath}/${demoPath}").getFile()
        demoDir.eachFile(FileType.FILES) { file ->
            itemService.createItem(demoPath, file.name);
        }
    }

    def createDemoTests() {
        String demoPath = grailsApplication.config.rialms.demoTestsPath;
        File contentDir = grailsApplication.parentContext.getResource("${grailsApplication.config.rialms.contentPath}").getFile()
        File demoDir = new File(contentDir, demoPath);

        demoDir.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith(".xml")) {
                def xml = new XmlSlurper().parse(file);
                if (xml.name() == 'assessmentTest') {
                    def relativePath = file.parent.substring(contentDir.absolutePath.length()).replace('\\', '/')
                    testService.createTest(relativePath, file.name);
                }
            }
        }
    }

    def createItems() {

        log.info("Initializing Item data...");
        createDemoItems();

        //1-5
        itemService.createItem('exercise', 'perimeter1.xml');
        itemService.createItem('qti', 'choice_multiple_chocolate.xml');
        itemService.createItem('qti', 'feedback.xml');
        itemService.createItem('qti', 'adaptive.xml');
        itemService.createItem('qti', 'mathml-templated.xml');

        //6-10
        itemService.createItem('qti', 'content-with-html-tags.xml');

    }

    def createTests() {

        log.info("Initializing Test data...");
        createDemoTests();

        //1-5
        testService.createTest('tests/qti/NonLinearSimpleTest', 'assessment.xml');
        testService.createTest('tests/exercise/adaptive', 'exercises.xml');
        testService.createTest('tests/qti/ModulePretest', 'Test_Template-Individual-Submission.xml');
        testService.createTest('tests/qti/ModulePretest', 'Test_Template-Simultaneous-Submission.xml');
        testService.createTest('tests/navigation/individual', 'individual_navigation.xml');

        //6-10
        testService.createTest('tests/navigation/simultaneous', 'simultaneous_navigation.xml');
        testService.createTest('tests/qti/Interactions', 'interactions.xml');
        testService.createTest("tests/qti/NavigationMixed", "test-ns-deep-nested-disabled-review.xml");
        testService.createTest('tests/qti/NavigationMixed', 'test-ns-disabled_allowReview.xml');
        testService.createTest("tests/qti/NavigationMixed", "test-nested-sections.xml");

        //11-15
        testService.createTest("tests/qti/IMSExamples", "rtest12.xml");
        testService.createTest("tests/qti/IMSExamples", "rtest26.xml");
        testService.createTest("tests/qti/IMSExamples", "rtest01.xml");

    }
}
