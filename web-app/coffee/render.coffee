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
  return

window.initTestRendering = ->
  $("a").tooltip()
  $('.dropdown-toggle').dropdown();
  $('.order-interaction').sortable(axis: 'y', containment: 'parent', cursor: 'move' );
  $('.order-interaction').disableSelection();
  window.initDragAndDrop()
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


window.initDragAndDrop =->
  $('.draggable').draggable(cursor:'move', containment:'parent', helper:'clone');
  $(".droppable").droppable
    accept: ".draggable"
    drop: (event, ui) ->
      droppedElement = ui.draggable
      matchMax = (Number) droppedElement.attr('matchMax')
      console.log("matchMax #{matchMax}")
      if matchMax > 1
        droppedElement.attr "matchMax", --matchMax
        droppedElement = ui.draggable.clone()
      else droppedElement = ui.draggable.clone()  if matchMax is 0
      droppedOn = $(this)
      droppedOn.find('span').html(droppedElement.html())
      droppedOn.find('input').val(droppedElement.attr('id') + ' ' + droppedOn.attr('id'))
      droppedElement.remove()
  return

window.initAngularScopeObjects = (data)->
  console.log(data)
  if data.angularData
    contentScope = angular.element('#content').scope()
    contentScope.$apply ->
      for key,value of data.angularData
        contentScope[key] = value;
  return



