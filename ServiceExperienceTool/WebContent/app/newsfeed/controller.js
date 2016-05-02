'use strict';

angular.module('newsfeed')

.controller('NewsfeedController', ['$scope', 'NewsfetchService', function($scope, Newsfetch) {
  var minArticleTextLimit = 50;
  $scope.TEST = "Newsfetch.get";
  $scope.limit = 0;
  $scope.maxLimit = 100;
  $scope.articleTextLimit = minArticleTextLimit;
  $scope.news = [];
  $scope.selectedPage = 0;

  expand();

  function expand() {
    $scope.limit += 5;
    $scope.selectedPage++;
    Newsfetch.get({
      action:'getNews', type: 'json', selectedPage: $scope.selectedPage, resultsPerPage: '5'
    }, function(newsobject) {
      for (var i = 0; i < newsobject.news.length; i++) {
        $scope.news.push(newsobject.news[i]);
      }
    });
  }

  // Expands article
  $scope.expandArticle = function(textLimit) {
    textLimit == minArticleTextLimit ? this.articleTextLimit = 999 : this.articleTextLimit = minArticleTextLimit;
  };

  // Expands newsfeed
  $scope.expandList = function() {
    expand();
  };

  $scope.iconChange = function() {
    return this.articleTextLimit == minArticleTextLimit;
  };
}]);
