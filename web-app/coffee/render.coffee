$ = jQuery
window.updateRenderedItem = (data) ->
  initAngularScopeObjects(data);
  #Close any interaction help dialogs open.
  window.closeInteractionHelp();

  if data.redirectUrl
    window.doRedirect(data);
    return
  else
    window.updateContent(data);
  window.initInteractions(data);
  return

window.initTestRendering = ->

  #$("#check-sb").mCustomScrollbar()
  #$("#sidebar-status-list").jScrollPane();
  #$("#sidebar-status-list").tinyscrollbar();

  window.initBootstrap();
  window.initInteractions();
  window.handleToggleNav();
  return;

window.handleToggleNav = ->
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
    return;

window.updateContent = (data)->
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
    testContentElement = $('#testContent')
    contentClass = $('#content').attr("class")
    testContentScope = angular.element('#testContent').scope()
    testContentElement.html(data.testContent)
    testContentScope.$apply ->
      testContentScope.recompile();
    $('#content').attr("class", contentClass)
    window.MathJax = null
    $.getScript($("script[src*='MathJax.js']").attr('src'))

window.doRedirect = (data)->
  $.post(data.redirectUrl, (resp) ->
    document.open()
    document.write(resp)
    document.close()
  )
  return;