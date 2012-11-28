package com.rialms.assessment
import grails.plugins.springsecurity.Secured
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile
import grails.converters.JSON
import com.rialms.consts.Constants
import org.qtitools.util.ContentPackage
import com.rialms.assessment.test.Test
import org.qtitools.qti.validation.ValidationResult

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class UploadController {

    def uploadService;
    def packageService;
    def testService;

    @Secured(['ROLE_USER'])
    def item(){
        render 'Upload items'
    }

    @Secured(['ROLE_USER'])
    def test(){
       String uploadDir = uploadService.getUserContentDir();
       return [uploadDir:uploadDir];
    }

    def saveUploadedTest(){
        log.info("save uploaded test called with params ${params}")

        File uploadedFile = new File(uploadService.getUserContentDir(),params[Constants.uploadedFile] );
        ContentPackage cp = packageService.unpackContent(uploadedFile);
        Test test = testService.createTest(cp);
        ValidationResult validationResult = testService.validateTest(test);
        Map uploadResult = ['validationResult': validationResult.allItems]
        log.info("DEBUG validationResult => ${uploadResult}");
        render uploadResult as JSON;
    }
}
