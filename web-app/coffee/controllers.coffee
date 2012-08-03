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

#TODO rename conroller name
window.TestContentController = ($scope,$compile)->
  $scope.recompile = ->
    $compile($('#testContent').contents())($scope);

  $scope.multiHintClicked = ->
    if $scope.multiHintStepCount > $scope.multiHintClickCount
      $scope.multiHintClickCount++
      $scope.multiHintRemainingCount =  $scope.multiHintStepCount - $scope.multiHintClickCount
    console.log('multiHintClicked...');
    console.log($scope.multiHintStepCount);
    console.log($scope.multiHintClickCount);
    console.log($scope.multiHintRemainingCount);

  $scope.getMultiHintStyle = ->
    if ($scope.multiHintRemainingCount?)
      if $scope.multiHintRemainingCount > 0
        "btn btn-info"
      else
        "btn btn-info disabled"
    else
      "btn btn-info"



