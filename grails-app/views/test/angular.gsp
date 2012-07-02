<!doctype html>
<html ng-app>
<head>
    <meta name="layout" content="primary"/>
</head>

<body>
<div>
    <label>Name:</label>
    <input type="text" ng-model="yourName" placeholder="Enter a name here">

    <hr>

    <h1>Hello {{yourName}}!</h1>
    <div ng-init="yourName='Hello'"></div>

</div>
<div>
<span id="source1"> source 1</span>
<span id="target1"> target 1</span>
    <span id="target2"> target 2</span>
    <span id="target3"> target 3</span>
    <span id="target4"> target 4</span>
    <span id="target5"> target 5</span>

</div>

<r:script>
    $(document).ready(function () {

        // retrieve the elements however you want (class name, tag name, ..)
        var elems = document.getElementsByTagName('span');
        var increase = Math.PI * 2 / elems.length;
        var x = 0, y = 0, angle = 0;

        for (var i = 0; i < elems.length; i++) {
            elem = elems[i];
            // modify to change the radius and position of a circle
            x = 100 * Math.cos(angle) + 200;
            y = 100 * Math.sin(angle) + 200;
            elem.style.position = 'absolute';
            elem.style.left = x + 'px';
            elem.style.top = y + 'px';
            angle += increase;
        }

        var exampleGreyEndpointOptions = {
            endpoint:"Rectangle",
            paintStyle:{ width:5, height:5, fillStyle:'#666' },
            isSource:true,
            connector : "Straight",
            connectorStyle: { lineWidth:2, strokeStyle:'blue' },
            isTarget:true
        };

        var endpointOptions1 = { isSource:true };
        var endpoint1 = jsPlumb.addEndpoint('source1',{ anchor:"RightMiddle" }, exampleGreyEndpointOptions);

        var endpointOptions2 = { isTarget:true, endpoint:"Rectangle", paintStyle:{ fillStyle:"gray" } };
        var endpoint2 = jsPlumb.addEndpoint("target1", { anchor:"LeftMiddle" }, exampleGreyEndpointOptions);
    });
</r:script>
</body>
</html>