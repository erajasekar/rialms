window.initAngularScopeObjects = (data)->
  if data.angularData
    contentScope = angular.element('#content').scope()
    headScope = angular.element('#head').scope()
    for key,value of data.angularData
      #Stylesheet should go to head
      if key is "itemStylesheets"
        headScope.$apply ->
          headScope[key] = value
      else
        contentScope.$apply ->
          contentScope[key] = value
  return