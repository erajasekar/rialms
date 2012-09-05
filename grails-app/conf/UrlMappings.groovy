class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home")
        "500"(view: '/error')

        name processItem: "/processItem" {
            controller = 'item'
            action = 'process'
        }
        "/fieldFunctionTest"(view: 'uitest/fieldFunctionTest')

    }
}
