modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery,jquery-ui,mybootstrap,angular'
        resource url: 'js/render.js', disposition: 'head'
        resource url: 'js/sample.js'
        resource url: 'css/rialms.css'
        resource url: 'coffee/render.coffee'
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

    angular {
          resource url: 'js/angular/angular-1.0.0rc10.js' , disposition: 'head'
    }
}