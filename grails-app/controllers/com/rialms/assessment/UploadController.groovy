package com.rialms.assessment
import grails.plugins.springsecurity.Secured


class UploadController {

    @Secured(['ROLE_ADMIN'])
    def index() { render 'Logged in with ROLE_ADMIN' }

    @Secured(['ROLE_USER'])
    def items(){
        render 'Logged in with ROLE_USER'
    }
}
