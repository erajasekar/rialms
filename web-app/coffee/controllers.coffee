angular.module("RialmsAngularApp", []).filter "searchByStatus", ->
  (input, filterStatus) ->
    console.log(input + ' == ' + filterStatus)
    if (input.isHeader == filterStatus)
      out = input
    return out
window.TestStatusController = ($scope)->
  $scope.getStatusEntries = (testStatusModel) ->
    return testStatusModel


  $scope.getStyleClass = (statusEntry) ->
    if statusEntry.isSectionTitle
      "nav-header"
    else if statusEntry.isCurrent
      "active"
  return
