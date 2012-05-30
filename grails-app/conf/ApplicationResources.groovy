modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery,jquery-ui,mybootstrap,angular,angular-autobind'
        resource url: 'http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML', disposition: 'head'
        resource url: 'js/render.js', disposition: 'head'
        resource url: 'js/sample.js'
        resource url: 'css/rialms.css'
    }

    mybootstrap {
        resource url: 'css/bootstrap.css'
        resource url: 'css/bootstrap-responsive.css'
        resource url: 'js/bootstrap.js'
        resource url: 'js/bootstrap-alert.js'
        resource url: 'js/bootstrap-button.js'
        resource url: 'js/bootstrap-carousel.js'
        resource url: 'js/bootstrap-collapse.js'
        resource url: 'js/bootstrap-dropdown.js'
        resource url: 'js/bootstrap-modal.js'
        resource url: 'js/bootstrap-popover.js'
        resource url: 'js/bootstrap-scrollspy.js'
        resource url: 'js/bootstrap-tab.js'
        resource url: 'js/bootstrap-tooltip.js'
        resource url: 'js/bootstrap-transition.js'
        resource url: 'js/bootstrap-typeahead.js'
    }
}