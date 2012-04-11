modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery,jquery-ui'
        resource url: 'js/render.js', disposition: 'head'

    }
}