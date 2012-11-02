class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home")
        "500"(view: '/error')

       /* "/login/auth" {
            controller = 'openId'
            action = 'auth'
        }*/
        "/login/openIdCreateAccount" {
            controller = 'openId'
            action = 'createAccount'
        }

        name processItem: "/processItem" {
            controller = 'item'
            action = 'process'
        }

        name viewItemXML: "/viewItemXML/$id?"{
            controller = 'item'
            action = 'viewItemXML'
        }

        name viewTestXML: "/viewTestXML/$id?"{
            controller = 'test'
            action = 'viewTestXML'
        }

        "/fieldFunctionTest"(view: 'uitest/fieldFunctionTest')

    }
}
