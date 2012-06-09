angular.module("RialmsAngularApp", []).filter "searchByStatus", ->
  (input, filterStatus) ->
    #console.log(input + ' == ' + filterStatus)
    true if (filterStatus == 'All' || input.status == filterStatus)

window.TestStatusController = ($scope,$filter)->

  $scope.filterStatus = 'All'

  $scope.getStatusEntries =  ->

    filteredStatusEntries = []
    angular.forEach($scope.testStatusModel, (value,key)->
      filteredStatusEntries.push(value) if ($filter('searchByStatus')(value,$scope.filterStatus))
      )
    #console.log('testStatusModel' + $scope.testStatusModel);
    filteredStatusEntries

  $scope.getStyleClass = (statusEntry) ->
    if statusEntry.isSectionTitle
      "nav-header"
    else if statusEntry.isCurrent
      "active"
  return
