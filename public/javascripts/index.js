(function() {
  $(function() {
    return $.get("/shits", function(shits) {
      return $.each(shits, function(index, shit) {
        var comment, name;
        name = $("<div>").addClass("name").text(shit.name);
        comment = $("<div>").addClass("comment").text(shit.comment);
        return $("#shits").append($("<li>").append(name).append(comment));
      });
    });
  });

}).call(this);

//# sourceMappingURL=index.js.map