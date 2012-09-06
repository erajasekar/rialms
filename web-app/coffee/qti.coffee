window.initInteractions =->
  window.initOrderInteraction();
  window.initGapInteraction();
  window.initMatchInteraction();
  return;

window.initOrderInteraction =->
  $('.order-interaction').sortable(axis: 'y', containment: 'parent', cursor: 'move' );
  $('.order-interaction').disableSelection();
  return;

window.initGapInteraction =->

  $(".draggable-gap-text").draggable(cursor:'move', containment:'#content', helper:'clone');
  $(".droppable-gap").droppable
    accept: ".draggable-gap-text"
    drop: (event, ui) ->
      droppedElement = ui.draggable
      droppedOn = $(this)
      droppedOnSpan = droppedOn.find('span');
      #Accept only if droppedOn is empty
      if (droppedOnSpan.html() is "&nbsp;")
        matchMax =  (Number) droppedElement.data('matchmax')
        #console.log("matchMax #{matchMax}")
        if matchMax > 1
          droppedElement.data("matchmax", --matchMax)
          droppedElement = ui.draggable.clone()
        else droppedElement = ui.draggable.clone()  if matchMax is 0

        droppedOnSpan.html(droppedElement.html())
        droppedOn.find('input').val(droppedElement.data('identifier') + ' ' + droppedOn.data('identifier'))
        droppedElement.remove()
  return

window.initMatchInteraction = ->
  #console.log($('.associate-interaction').data('initialized'));
  if ($('.associate-interaction').data('initialized'))
    return;

  connectorStyle = {
  strokeStyle: "green"
  lineWidth: 2
  }
  endpointStyle = {
  width: 10
  height: 10
  fillStyle: "green"
  }
  connector = [ "StateMachine", curviness: 20 ]

  jsPlumb.importDefaults(
    Endpoint : ["Dot", {radius:2}]
    ConnectionOverlays: [ [ "Arrow",
      location: 1
      id: "arrow"
      length: 10
      foldback: 0.8
    ] ]
  );

  endPointOptions = {
  endpoint: "Rectangle"
  paintStyle: endpointStyle
  connector: connector
  connectorStyle: connectorStyle
  }

  responseIdentifier = '';

  $(".associable-choice-endpoint").each (index, element) ->
    jqueryElement = $(element)
    parent = jqueryElement.parent()
    jsPlumb.makeSource jqueryElement,
      parent: parent
      anchor: "Continuous"
      connector: connector
      connectorStyle:connectorStyle
      maxConnections: parent.data('matchmax')
    return;

  $(".associable-choice").each (index, element) ->
    jqueryElement = $(element)
    responseIdentifier =  jqueryElement.data('responseidentifier')

    if jqueryElement.data("role") is "sourceAndTarget"
      jsPlumb.draggable(element,{containment:".associate-interaction"});
      jqueryElement.css({position:'absolute', cursor:'move'})
      jsPlumb.makeTarget element,
        anchor : "Continuous"
        maxConnections: jqueryElement.data('matchmax')
    else
      isSource = jqueryElement.data("role") is "source"
      jsPlumb.addEndpoint element,
        anchor: (if isSource then "RightMiddle" else "LeftMiddle")
        isSource: isSource
        isTarget: not isSource
        maxConnections: jqueryElement.data('matchmax'),
        endPointOptions
    return

  parent = $('.associable-choice').closest('.match-interaction , .associate-interaction');
  hiddenElements = parent.find('input:hidden[name="' + responseIdentifier + '"]');
  #console.log(hiddenElements) ;
  hiddenElements.each (index,element)->
    responseValues = $(element).attr('value').split(' ');
    source = parent.find(':data(identifier='+ responseValues[0] + ')');
    target = parent.find(':data(identifier='+ responseValues[1] + ')');
    jsPlumb.connect
      source: source
      target: target
      endpoint: "Rectangle"
      newConnection:false
      endpointStyle: endpointStyle
      anchors:["RightMiddle", "LeftMiddle" ]
      connector: connector
      paintStyle:connectorStyle

  bindJsPlumbEvents();
  $('.associate-interaction').data('initialized','true')
  return

window.bindJsPlumbEvents = ->
  jsPlumb.bind "click", (c) ->
    jsPlumb.detach(c);
    return;

  jsPlumb.bind "jsPlumbConnection", (connection) ->
    parent = $(connection.source).closest('.match-interaction , .associate-interaction');
    inputValue = connection.source.data('identifier') + ' ' + connection.target.data('identifier');
    hiddenElements = parent.find('input:hidden[value="' + inputValue + '"]')
    responseIdentifier =  connection.source.data('responseidentifier')
    parent.append('<input type="hidden" name="' + responseIdentifier + '" value="' + inputValue + '" />') if hiddenElements.length is 0
    return;

  jsPlumb.bind "jsPlumbConnectionDetached", (connection) ->
    parent = $(connection.source).closest('.match-interaction , .associate-interaction');
    inputValue = connection.source.data('identifier') + ' ' + connection.target.data('identifier');
    hiddenElements = parent.find('input:hidden[value="' + inputValue + '"]')
    hiddenElements.each (index,element)->
      $(element).remove();
      return;
    return;

  return;

window.showInteractionHelp = (params)->
  $(params.elementId).dialog(params);
  return false;

window.closeInteractionHelp = ->
  $('div[id^=interactionHelp]').dialog('close');
  return;