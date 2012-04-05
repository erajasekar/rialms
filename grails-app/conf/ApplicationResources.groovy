modules = {
    application {
        dependsOn 'jquery,jquery-ui'
        resource url: 'js/application.js'
        resource url: 'js/render.js'
    }

    core {
        dependsOn 'jquery,jquery-ui'
        resource url: 'js/render.js', disposition: 'head'
    }
}