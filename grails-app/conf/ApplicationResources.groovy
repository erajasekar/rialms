modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery,jquery-ui,bootstrap'
        resource url: 'http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML', disposition: 'head'
        resource url: 'js/render.js', disposition: 'head'
        resource url: 'js/sample.js'
        resource url: 'css/rialms.css'
    }
}