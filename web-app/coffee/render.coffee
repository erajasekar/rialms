$ = jQuery
window.updateRenderedItem = (data) ->
  initAngularScopeObjects(data)


  if data.redirectUrl
    $.post(data.redirectUrl, (resp) ->
        document.open()
        document.write(resp)
        document.close()
    )
    return
  else
    if data.visibleElementIds
      $(visibleElementId).show() for visibleElementId in data.visibleElementIds
    if data.hiddenElementIds
      $(hiddenElementId).hide() for hiddenElementId in data.hiddenElementIds
    if data.itemOutcomeValues
      outcomeValuesText = JSON.stringify(data.itemOutcomeValues)
      $('#itemOutcomeValues').text(outcomeValuesText)
    if data.testOutcomeValues
      outcomeValuesText = JSON.stringify(data.testOutcomeValues)
      $('#testOutcomeValues').text(outcomeValuesText)
    if data.disableElementIds
      $(disableElementId).attr("disabled", true) for disableElementId in data.disableElementIds
    if data.testFeedback
      $('#testFeedback').html(data.testFeedback)
    if data.testStatusContent
      sidebarClass = $('#sidebar').attr("class")
      ## $('#testStatusContent').html(data.testStatusContent)
      if (!sidebarClass)
        $('#sidebar').attr("class", sidebarClass)
        $('#sidebar').animate({width: 'toggle'}, 0)
    if data.testSectionTitleContent
      $('#testSectionTitleContent').html(data.testSectionTitleContent)
    if data.testContent
      contentClass = $('#content').attr("class")
      $('#testContent').html(data.testContent)
      $('#content').attr("class", contentClass)
      window.MathJax = null
      $.getScript($("script[src*='MathJax.js']").attr('src'))
  #Remove commented code based on performance
  #mathJaxScript = document.createElement("script");
  #mathJaxScript.type = "text/javascript";
  #mathJaxScript.src = $("script[src*='MathJax.js']").attr('src');
  #$('head').append(mathJaxScript);
  window.initInteractions();
  return

window.initTestRendering = ->
  $("a").tooltip()
  $('.dropdown-toggle').dropdown();
  window.initInteractions();
  $("a.toggleNav").click ->
    if $("a.toggleNav span").text() is $("<div>").html("&laquo;").text()
      $("a.toggleNav span").html "&raquo;"
    else
      $("a.toggleNav span").html "&laquo;"
    $("a.toggleNav").toggleClass "pull-right pull-left"
    if $("a.toggleNav").attr("data-original-title") is "Hide Sidebar"
      $("a.toggleNav").attr("title", "Show Sidebar").tooltip("fixTitle").tooltip "show"
    else
      $("a.toggleNav").attr("title", "Hide Sidebar").tooltip("fixTitle").tooltip "show"
    $("#sidebar").animate
      width: "toggle", 0
    $("#content").toggleClass "span12 span9"
    $("#content").toggleClass "no-sidebar"
    $("#sidebar").toggleClass "span3"

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
        console.log("matchMax #{matchMax}")
        if matchMax > 1
          droppedElement.data("matchmax", --matchMax)
          droppedElement = ui.draggable.clone()
        else droppedElement = ui.draggable.clone()  if matchMax is 0

        droppedOnSpan.html(droppedElement.html())
        droppedOn.find('input').val(droppedElement.data('identifier') + ' ' + droppedOn.data('identifier'))
        droppedElement.remove()
  return

window.initAngularScopeObjects = (data)->
  #console.log(data)
  if data.angularData
    contentScope = angular.element('#content').scope()
    contentScope.$apply ->
      for key,value of data.angularData
        contentScope[key] = value;
  return

window.initMatchInteraction = ->
  jsPlumb.importDefaults(
    Endpoint: [ "Dot",
      radius: 2
    ]
    ConnectionOverlays: [ [ "Arrow",
      location: 1
      id: "arrow"
      length: 10
      foldback: 0.8
    ] ]
  );
  jsPlumb.bind "click", (c) ->
    jsPlumb.detach(c);

  endPointOptions = {
    endpoint: "Rectangle"
    paintStyle:
      width: 7
      height: 7
      fillStyle: "green"

    anchor: "Continuous"
    connector: [ "StateMachine",
      curviness: 20
    ]
    connectorStyle:
      strokeStyle: "green"
      lineWidth: 2
  }
  responseIdentifier = '';
  $(".associable-choice").each (index, element) ->
    jqueryElement = $(element)
    responseIdentifier =  jqueryElement.data('responseidentifier')
    isSource = jqueryElement.data("role") is "lhs"
    jsPlumb.addEndpoint element,
      anchor: (if isSource then "RightMiddle" else "LeftMiddle")
      isSource: isSource
      isTarget: not isSource
      maxConnections: jqueryElement.data('matchmax'),
      endPointOptions
    return

  form = $(".associable-choice").closest('form');

  console.log(responseIdentifier);
  hiddenElements = form.find('input:hidden[name="' + responseIdentifier + '"]')
  console.log(hiddenElements);

  jsPlumb.bind "jsPlumbConnection", (connection) ->
    form = $(connection.source).closest('form');
    inputValue = connection.source.data('identifier') + ' ' + connection.target.data('identifier');
    hiddenElements = form.find('input:hidden[value="' + inputValue + '"]')
    responseIdentifier =  connection.source.data('responseidentifier')
    form.append('<input type="hidden" name="' + responseIdentifier + '" value="' + inputValue + '" />') if hiddenElements.length is 0
    return;

  jsPlumb.bind "jsPlumbConnectionDetached", (connection) ->
    form = $(connection.source).closest('form');
    inputValue = connection.source.data('identifier') + ' ' + connection.target.data('identifier');
    hiddenElements = form.find('input:hidden[value="' + inputValue + '"]')
    hiddenElements.each (index,element)->
      $(element).remove();
    return;

  return


