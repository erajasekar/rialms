package com.rialms.util

import org.springframework.beans.factory.InitializingBean
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode
import groovy.text.SimpleTemplateEngine
import org.springframework.web.context.request.RequestContextHolder
import com.rialms.auth.User

class EmailService implements InitializingBean  {

    static transactional = false
    def mailService;
    def conf;
    def g;

    void afterPropertiesSet() {
        Class configClass;
        GroovyClassLoader classLoader = new GroovyClassLoader(EmailService.classLoader)
        try {
            configClass = classLoader.loadClass('EmailServiceConfig')
        } catch (ClassNotFoundException ex) {
            log.error( "EmailServiceConfig.groovy not found");
        }
        ConfigSlurper configSlurper = new ConfigSlurper(GrailsUtil.environment);
        conf = configSlurper.parse(configClass);
        g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();
    }

    public void sendForgotPassword(String email){
        def registrationCode = new RegistrationCode(username: email).save()
        String url = generateLink('openId' , 'resetPassword', [t: registrationCode.token])

        def body = conf.email.forgotPassword.body
        if (body.contains('$')) {
            body = evaluate(body, [url: url])
        }

        mailService.sendMail {
            to email
            from conf.email.from
            subject conf.email.forgotPassword.subject
            html body.toString()
        }
    }

    public void sendVerifyRegistration(String email, String name){
        def registrationCode = new RegistrationCode(username: email).save()
        String url = generateLink('openId' ,'verifyRegistration', [t: registrationCode.token])

        def body = conf.email.register.body

        if (body.contains('$')) {
            body = evaluate(body, [url: url, name : name])
        }
        mailService.sendMail {
            to email
            from conf.email.from
            subject conf.email.register.subject
            html body.toString()
        }
    }

    protected String generateLink(String controller, String action, linkParams) {
        def request = RequestContextHolder.getRequestAttributes().getRequest();
        g.createLink(base: "$request.scheme://$request.serverName:$request.serverPort$request.contextPath",
                controller: controller, action: action,
                params: linkParams)
    }

    protected String evaluate(s, binding) {
        new SimpleTemplateEngine().createTemplate(s).make(binding)
    }

}
