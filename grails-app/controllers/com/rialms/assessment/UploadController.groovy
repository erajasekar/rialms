package com.rialms.assessment
import grails.plugins.springsecurity.Secured
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile
import grails.converters.JSON

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class UploadController {


    @Secured(['ROLE_USER'])
    def item(){
        render 'Upload items'
    }

    @Secured(['ROLE_USER'])
    def test(){
       return [uploadDir:"c:/Raja/projects/rialms/dev/rialms"];
    }
}
