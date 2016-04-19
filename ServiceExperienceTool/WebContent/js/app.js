'use strict';
var app = angular.module('myApp', ['ngAnimate']);

app.controller('MyController', function($scope, $http) {

  // Continously fetching input from searchbox.
  $scope.$watch('search', function() {
    fetch();
  });

  // Default searchtext on start.
  $scope.search = "Search...";

  // Limits to 5 news
  $scope.limit = 5;

  // Makes AJAX-call to API and sets response to 'details'
  function fetch() {
    $http.get("http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=ddbb1c79dda444649d290bc19bd923c6%3A12%3A75027211")
      .then(function(response) {
        $scope.details = response.data;
        $scope.title = response.data.results[0].section;
      });
  }

  // Expands newsfeed with 5 posts
  $scope.loadMore = function() {
    $scope.limit += 5;

    // Show all
    //$scope.limit = $scope.details.results.length;
  }

  // Expands news object
  $scope.clickMe = function(clickEvent) {
    var targetId = clickEvent.target.parentElement.id;
    var x = document.getElementById(targetId);
    var textBox = x.querySelector("#full-article-container");

    if (textBox.className.indexOf("w3-show") == -1) {
      textBox.className += " w3-show";
    } else {
      textBox.className = textBox.className.replace("w3-show", "");
    }
  };
});
