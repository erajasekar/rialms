angular.module("RialmsAngularApp", ['ngSanitize']).filter "filterByStatus", ->
  (input, filterStatus) ->
    true if (filterStatus == 'All' || input.status == filterStatus)

window.TestStatusController = ($scope,$filter)->

  $scope.filterStatus = 'All'

  $scope.getStatusEntries =  ->

    filteredStatusEntries = []
    angular.forEach($scope.testStatusModel, (value,key)->
      filteredStatusEntries.push(value) if ($filter('filterByStatus')(value,$scope.filterStatus))
      )
    #console.log('testStatusModel' + $scope.testStatusModel);
    filteredStatusEntries

  $scope.getStyleClass = (statusEntry) ->
    if statusEntry.isSectionTitle
      "nav-header"
    else if statusEntry.isCurrent
      "active"
  return

window.ItemContentController = ($scope,$compile)->
  $scope.recompile = ->
    $compile($('#testContent').contents())($scope);

  $scope.multiHintClicked = ->
    if $scope.multiHintStepCount > $scope.multiHintClickCount
      $scope.multiHintClickCount++
      $scope.multiHintRemainingCount =  $scope.multiHintStepCount - $scope.multiHintClickCount

  $scope.getMultiHintStyle = ->
    if ($scope.multiHintStepCount > 0)
      if $scope.multiHintRemainingCount > 0
        "btn btn-info"
      else
        "btn btn-info disabled"
    else
      "btn btn-info"

window.LoginController = ($scope)->
  $scope.toggleForms = ->
    $scope.isSignUp = !$scope.isSignUp
    if $scope.isSignUp
      $scope.title = 'Create a new account'
      $scope.message = 'Have an account?'
      $scope.buttonLabel = 'Log in'
    else
      $scope.title = 'Login'
      $scope.message = 'Need an account?'
      $scope.buttonLabel = 'Sign Up'
