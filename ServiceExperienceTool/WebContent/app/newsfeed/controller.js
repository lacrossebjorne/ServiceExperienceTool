'use strict';

angular.module('newsfeed')

.controller('NewsfeedController', ['$scope', 'NewsfetchService', function($scope, Newsfetch) {
  var minArticleTextLimit = 50;
  $scope.TEST = "Newsfetch.get";
  $scope.limit = 5;
  $scope.articleTextLimit = minArticleTextLimit;

  Newsfetch.get({
    action:'getNews', type: 'json'
  }, function(newsobject) {
    $scope.newsobject = newsobject;
    console.log(newsobject);
  });

  // Expands article
  $scope.expandArticle = function(textLimit) {
    textLimit == minArticleTextLimit ? this.articleTextLimit = 999 : this.articleTextLimit = minArticleTextLimit;
  };

  // Expands newsfeed
  $scope.expandList = function() {
    $scope.limit += 5;
  };

  $scope.iconChange = function() {
    return this.articleTextLimit == minArticleTextLimit;
  };
}]);
