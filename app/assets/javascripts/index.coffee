$ ->
  $.get "/shits", (shits) ->
    $.each shit, (index, shit) ->
      name = $("<div>").addClass("name").text shit.name
      comment = $("<div>").addClass("comment").text shit.comment
      $("#shits").append $("<li>").append(name).append(comment)