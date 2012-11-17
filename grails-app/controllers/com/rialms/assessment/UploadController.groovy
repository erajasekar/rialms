package com.rialms.assessment
import grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class UploadController {

    @Secured(['ROLE_USER'])
    def item(){
        render 'Upload items'
    }

    @Secured(['ROLE_USER'])
    def test(){
       return;
    }
}
