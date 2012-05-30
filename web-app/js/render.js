(function() {
  var $;
  $ = jQuery;
  window.updateRenderedItem = function(data) {
    var contentClass, disableElementId, hiddenElementId, name, outcomeValuesText, sidebarClass, value, visibleElementId, _i, _j, _k, _len, _len2, _len3, _ref, _ref2, _ref3, _ref4;
    if (data.redirectUrl) {
      $.post(data.redirectUrl, function(resp) {
        document.open();
        document.write(resp);
        return document.close();
      });
      return;
    } else {
      if (data.responseValues) {
        _ref = data.responseValues;
        for (name in _ref) {
          value = _ref[name];
          $('form').field(name, value);
        }
      }
      if (data.visibleElementIds) {
        _ref2 = data.visibleElementIds;
        for (_i = 0, _len = _ref2.length; _i < _len; _i++) {
          visibleElementId = _ref2[_i];
          $(visibleElementId).show();
        }
      }
      if (data.hiddenElementIds) {
        _ref3 = data.hiddenElementIds;
        for (_j = 0, _len2 = _ref3.length; _j < _len2; _j++) {
          hiddenElementId = _ref3[_j];
          $(hiddenElementId).hide();
        }
      }
      if (data.itemOutcomeValues) {
        outcomeValuesText = JSON.stringify(data.itemOutcomeValues);
        $('#itemOutcomeValues').text(outcomeValuesText);
      }
      if (data.testOutcomeValues) {
        outcomeValuesText = JSON.stringify(data.testOutcomeValues);
        $('#testOutcomeValues').text(outcomeValuesText);
      }
      if (data.disableElementIds) {
        _ref4 = data.disableElementIds;
        for (_k = 0, _len3 = _ref4.length; _k < _len3; _k++) {
          disableElementId = _ref4[_k];
          $(disableElementId).attr("disabled", true);
        }
      }
      if (data.testFeedback) {
        $('#testFeedback').html(data.testFeedback);
      }
      if (data.testStatusContent) {
        sidebarClass = $('#sidebar').attr("class");
        $('#testStatusContent').html(data.testStatusContent);
        if (!sidebarClass) {
          $('#sidebar').attr("class", sidebarClass);
          $('#sidebar').animate({
            width: 'toggle'
          }, 0);
        }
      }
      if (data.testSectionTitleContent) {
        $('#testSectionTitleContent').html(data.testSectionTitleContent);
      }
      if (data.testContent) {
        contentClass = $('#content').attr("class");
        $('#testContent').html(data.testContent);
        $('#content').attr("class", contentClass);
        window.MathJax = null;
        $.getScript($("script[src*='MathJax.js']").attr('src'));
      }
    }
  };
  $.fn.field = function(inputName, value) {
    var $inputElement;
    if (typeof inputName !== "string") {
      return false;
    }
    $inputElement = $(this).find("[name=" + inputName + "]");
    switch ($inputElement.attr("type")) {
      case "checkbox":
        $inputElement.each(function(i) {
          var checked;
          checked = $.inArray($(this).val(), value.split(",")) >= 0;
          return $(this).attr('checked', checked);
        });
        break;
      case "radio":
        $inputElement.each(function(i) {
          if ($(this).val() === value) {
            return $(this).attr('checked', true);
          }
        });
        break;
      case "button":
        break;
      case "submit":
        break;
      case undefined:
        switch ($inputElement.attr("id")) {
          case "select":
            $inputElement.children('option').each(function(i) {
              if ($(this).val() === value) {
                return $(this).attr('selected', 'selected');
              }
            });
            break;
          default:
            $(this).append("<input type=\"hidden\" name=\"" + inputName + "\" value=\"" + value + "\" />");
        }
        break;
      default:
        $inputElement.val(value);
    }
    return $inputElement;
  };
  window.initTestRendering = function() {
    $("a").tooltip();
    return $("a.toggleNav").click(function() {
      if ($("a.toggleNav span").text() === $("<div>").html("&laquo;").text()) {
        $("a.toggleNav span").html("&raquo;");
      } else {
        $("a.toggleNav span").html("&laquo;");
      }
      $("a.toggleNav").toggleClass("pull-right pull-left");
      if ($("a.toggleNav").attr("data-original-title") === "Hide Sidebar") {
        $("a.toggleNav").attr("title", "Show Sidebar").tooltip("fixTitle").tooltip("show");
      } else {
        $("a.toggleNav").attr("title", "Hide Sidebar").tooltip("fixTitle").tooltip("show");
      }
      $("#sidebar").animate({
        width: "toggle"
      }, 0);
      $("#content").toggleClass("span12 span9");
      $("#content").toggleClass("no-sidebar");
      return $("#sidebar").toggleClass("span3");
    });
  };
  window.convertMillisecondsToHrsMinsSecs = function(ms, p) {
    var addUnitToClock, arrayPattern, clock, createClock, hours, i, j, minuets, pattern, seconds;
    createClock = function(unit) {
      if (pattern.match(unit)) {
        if (unit.match(/h/)) {
          addUnitToClock(hours, unit);
        }
        if (unit.match(/m/)) {
          addUnitToClock(minuets, unit);
        }
        if (unit.match(/s/)) {
          return addUnitToClock(seconds, unit);
        }
      }
    };
    addUnitToClock = function(val, unit) {
      if (val < 10 && unit.length === 2) {
        val = "0" + val;
      }
      return clock.push(val);
    };
    pattern = p || "hh:mm:ss";
    arrayPattern = pattern.split(":");
    clock = [];
    hours = Math.floor(ms / 3600000);
    minuets = Math.floor((ms % 3600000) / 60000);
    seconds = Math.floor(((ms % 360000) % 60000) / 1000);
    i = 0;
    j = arrayPattern.length;
    while (i < j) {
      createClock(arrayPattern[i]);
      i++;
    }
    return {
      hours: hours,
      minuets: minuets,
      seconds: seconds,
      clock: clock.join(":")
    };
  };
  window.initTimer = function(timeRemaining) {
    console.log("initTimer timeRemaing = " + timeRemaining);
    window.timeInterval = 1000;
    if (window.timerRunning) {
      window.clearInterval(window.timer);
      window.timerRunning = false;
    }
    window.timeRemaining = timeRemaining;
    window.updateTimer();
    if ($('#submit') && !$('#submit').attr('disabled')) {
      window.timer = window.setInterval("window.updateTimer()", timeInterval);
    }
  };
  window.updateTimer = function() {
    var prettyTime;
    if (window.timeRemaining <= 0) {
      window.clearInterval(window.timer);
      window.timerRunning = false;
      $('#submit').click();
    } else {
      window.timerRunning = true;
      prettyTime = window.convertMillisecondsToHrsMinsSecs(parseInt(timeRemaining));
      $('#timeRemaining').text(prettyTime.clock);
      window.timeRemaining -= window.timeInterval;
    }
  };
}).call(this);
