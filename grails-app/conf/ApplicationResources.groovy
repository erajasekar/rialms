modules = {
    application {
        resource url: 'js/application.js'
    }

    core {
        dependsOn 'jquery,jquery-ui,mybootstrap,coffee,angular,jsPlumb,jqueryPlugins, customScrollbar'
        resource url: 'js/sample.js'
        resource url: 'css/rialms.css'

    }

    coffee {
        resource url: 'coffee/render.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/datetime-utils.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/angular-utils.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/bootstrap-utils.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/qti.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/controllers.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/dialog-utils.coffee', bundle: 'render', disposition: 'head'
        resource url: 'coffee/validation.coffee', bundle: 'render', disposition: 'head'
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
        // resource url: 'js/bootstrap-modal.js'
        resource url: 'js/bootstrap-popover.js'
        resource url: 'js/bootstrap-scrollspy.js'
        resource url: 'js/bootstrap-tab.js'
        resource url: 'js/bootstrap-tooltip.js'
        resource url: 'js/bootstrap-transition.js'
        resource url: 'js/bootstrap-typeahead.js'
    }

    angular {
        resource url: 'js/angular/angular-1.0.1.js', disposition: 'head'
        resource url: 'js/angular/angular-sanitize-1.0.1.js', disposition: 'head'
    }

    jsPlumb {
        resource url: 'js/jsPlumb/jquery.jsPlumb-1.3.10-all.js'
    }

    jqueryPlugins {
        resource url: 'js/jquery/jquery.dataSelector.js'
        resource url: 'js/jquery/jquery.jgrowl.js'
    }

   /* fileupload {
        resource url: 'js/fileupload/jquery.fileupload.js'
        resource url: 'js/fileupload/jquery.iframe-transport.js'
        resource url: 'js/fileupload/vendor/jquery.ui.widget.js'
    }*/

    prettify {
        resource url: 'js/prettify/prettify.js'
        resource url: 'css/prettify/prettify.css'
    }

    zocial {
        resource url: 'css/zocial/zocial.css'
    }
    customScrollbar {
        //TODO P3: Fix scrollbar customization
      //  resource url: 'js/scrollbar/jquery.mCustomScrollbar.js'
      //  resource url: 'js/scrollbar/jquery.mousewheel.min.js'
      //  resource url: 'css/scrollbar/jquery.mCustomScrollbar.css'
       // resource url: 'css/scrollbar/mCSB_buttons.png'
       // resource url:'css/scrollbar/jquery.jscrollpane.css'
        //resource url:'js/scrollbar/jquery.jscrollpane.min.js'
        //resource url:'js/scrollbar/mwheelIntent.js'
        //resource url: 'js/scrollbar/jquery.tinyscrollbar.min.js'

    }
}