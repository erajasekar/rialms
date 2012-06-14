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

$.fn.field = (inputName, value) ->
  return false  if typeof inputName isnt "string"
  $inputElement = $(this).find("[name=" + inputName + "]")
  switch $inputElement.attr("type")
    when "checkbox"
      $inputElement.each (i) ->
        checked = $.inArray($(this).val(), value.split(",")) >= 0
        $(this).attr('checked', checked)
    when "radio"
      $inputElement.each (i) ->
        $(this).attr('checked', true)  if $(this).val() is value
    when "button"
      break;
    when "submit"
      break
    when `undefined`
      switch $inputElement.attr("id")
        when "select"
          $inputElement.children('option').each (i) ->
            $(this).attr('selected', 'selected') if $(this).val() is value
        else
          $(this).append "<input type=\"hidden\" name=\"" + inputName + "\" value=\"" + value + "\" />"
    else
    #console.log("warning unhandled element " + $inputElement)
      $inputElement.val value
  $inputElement

window.initTestRendering = ->
  $("a").tooltip()
  $('.dropdown-toggle').dropdown();
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


window.initTestStatusModel = (testStatusModel)->
  testStatusScope = angular.element('#testStatusContent').scope()
  if (testStatusScope)
    testStatusScope.$apply ->
      testStatusScope.testStatusModel = testStatusModel if testStatusModel

window.initNavigationButtonStates = (navigationButtonStates)->
  navigationControlsScope = angular.element('#navigationControls').scope()
  if (navigationControlsScope)
    navigationControlsScope.$apply ->
      navigationControlsScope.navigationButtonStates = navigationButtonStates if navigationButtonStates
  #console.log('Navs Json ' + navigationControlsScope.navigationButtonStates)

window.initAngularScopeObjects = (data)->
  #TODO refactor to automatically assign all props
  contentScope = angular.element('#content').scope()
  contentScope.$apply ->
    contentScope.assessmentHeader = data.assessmentHeader if data.assessmentHeader
    contentScope.endAttemptButtons = data.endAttemptButtons if data.endAttemptButtons
  #console.log(contentScope.assessmentHeader)
  initTestStatusModel(data.testStatusModel)
  initNavigationButtonStates(data.navigationButtonStates)



