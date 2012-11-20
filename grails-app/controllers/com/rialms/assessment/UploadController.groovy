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
       return;
    }

    def upload() {
        def results = []
        File dest;
        if (request instanceof MultipartHttpServletRequest){
            for(filename in request.getFileNames()){
                MultipartFile file = request.getFile(filename)

                String newFileName = UUID.randomUUID().toString() + file.originalFilename.substring(file.originalFilename.lastIndexOf("."))
                dest = new  File("C:\\Raja\\projects\\rialms\\dev\\rialms\\${newFileName}");
                file.transferTo(dest);

                results << [
                        name: file.originalFilename,
                        size: file.size,
                        url: createLink(controller: 'image', action: 'image'),
                        thumbnail_url: createLink(controller: 'image', action: 'image'),
                        delete_url: createLink(controller: 'image', action: 'image'),
                        delete_type: "DELETE"
                ]
            }
        }
        log.info("Successfully saved file ${dest}");
        render results as JSON
    }
}
