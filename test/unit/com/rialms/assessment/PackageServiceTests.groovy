package com.rialms.assessment

import grails.test.GrailsUnitTestCase
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class PackageServiceTests extends GrailsUnitTestCase{
    def packageService;

    protected void setUp() {
        super.setUp();

        packageService = new PackageService();
        mockConfig("""
               rialms {
                   contentPath = 'content';
               }
           """);
    }

    void testUnpackContent() {
        File dest = new File("C:/Raja/projects/rialms/dev/rialms/web-app/content/packages/packages-test1");
        InputStream is = new FileInputStream(new File("C:\\Raja\\projects\\rialms\\dev\\rialms\\web-app\\content\\packages\\package-test1.zip"));
        packageService.handleZip(is,dest);

    }
}
