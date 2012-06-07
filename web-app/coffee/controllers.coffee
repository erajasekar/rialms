angular.module("RialmsAngularApp", []).filter "searchByStatus", ->
  (input,filterStatus) ->
    console.log(input + ' == ' + filterStatus)
    if (input.isHeader == filterStatus)
      out = input
    return out
window.TestStatusController = ($scope)->

  $scope.getStatusEntries = (testStatusModel) ->
    console.log('testStatusModel ' + testStatusModel)
    return testStatusModel

  return
