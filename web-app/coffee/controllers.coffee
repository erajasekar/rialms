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

window.TestContentController = ($scope,$compile)->
  $scope.recompile = ->
    console.log($scope.isResponseValid)
    console.log($('#itemResult').contents())
    $compile($('#itemResult').contents())($scope);
    console.log($('#itemResult').contents())

