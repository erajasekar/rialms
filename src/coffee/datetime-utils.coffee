window.convertMillisecondsToHrsMinsSecs = (ms, p) ->
  createClock = (unit) ->
    if pattern.match(unit)
      addUnitToClock hours, unit  if unit.match(/h/)
      addUnitToClock minuets, unit  if unit.match(/m/)
      addUnitToClock seconds, unit  if unit.match(/s/)
  addUnitToClock = (val, unit) ->
    val = "0" + val  if val < 10 and unit.length is 2
    clock.push val
  pattern = p or "hh:mm:ss"
  arrayPattern = pattern.split(":")
  clock = []
  hours = Math.floor(ms / 3600000)
  minuets = Math.floor((ms % 3600000) / 60000)
  seconds = Math.floor(((ms % 360000) % 60000) / 1000)
  i = 0
  j = arrayPattern.length

  while i < j
    createClock arrayPattern[i]
    i++
  hours: hours
  minuets: minuets
  seconds: seconds
  clock: clock.join(":")

window.initTimer = (timeRemaining)->
  console.log("initTimer timeRemaing = " + timeRemaining)
  window.timeRemaining = timeRemaining
  window.timeInterval = 1000
  updateTimer()
  window.timer = window.setInterval("window.updateTimer()", timeInterval) if $('#submit') && !$('#submit').attr('disabled')
  return

window.updateTimer = ->
  if window.timeRemaining <= 0
    window.clearInterval(window.timer)
    $('#submit').click()
  prettyTime = window.convertMillisecondsToHrsMinsSecs(parseInt(timeRemaining))
  #console.log(prettyTime)
  $('#timeRemaining').text(prettyTime.clock)
  window.timeRemaining -= window.timeInterval
  return