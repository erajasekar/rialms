target(main: "The description of the script goes here!") {
    java(classname:'com.rialms.Less2Css'){
        arg(value:"d:/raja/projects/rialms/dev/rialms/src/less/rialms/rialms.less d:/raja/projects/rialms/dev/rialms/web-app/css/rialms.css" );
    }
}

setDefaultTarget(main)
