package com.rialms.assessment
import grails.plugins.springsecurity.Secured
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile
import grails.converters.JSON
import com.rialms.consts.Constants
import org.qtitools.util.ContentPackage
import com.rialms.assessment.test.Test
import com.rialms.assessment.ValidationResult

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

    def saveUploadedTest(){  //TODO P1 move it to service
        log.info("save uploaded test called with params ${params}")

        File uploadedFile = new File(uploadService.getUserContentDir(),params[Constants.uploadedFile] );

        ValidationResult validationResult = new ValidationResult();
        Test test = null;
        try{
            ContentPackage cp = packageService.unpackContent(uploadedFile);
            test = testService.createTest(cp);
        }catch(Exception e){
            String msg = "Error in upacking content : ${e.getMessage()}";
            log.error(msg, e);
            validationResult.add(ValidationResult.Type.ERROR, msg);
        }

        if (test){
            try {
                validationResult = testService.validateTest(test);
            }catch (Exception e){
                String msg = "Exception in validating test : ${e.getMessage()}";
                log.error(msg, e);
                validationResult.add(ValidationResult.Type.ERROR, msg);
            }
        }

        render validationResult as JSON;
    }
}
