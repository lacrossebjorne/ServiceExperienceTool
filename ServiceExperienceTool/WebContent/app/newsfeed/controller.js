'use strict';

angular.module('newsfeed')

.controller('NewsfeedController', ['$scope', 'NewsfetchService', function($scope, Newsfetch) {
  var minArticleTextLimit = 50;
  $scope.TEST = "JS updated";
  $scope.limit = 5;
  $scope.articleTextLimit = minArticleTextLimit;

  Newsfetch.get({
    // parameter: 'value',
    // parameter: 'value'
  }, function(newsobject) {
    // Callback
    $scope.newsobject = newsobject;
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
