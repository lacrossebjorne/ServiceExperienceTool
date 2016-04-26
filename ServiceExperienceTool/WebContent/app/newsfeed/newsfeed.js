'use strict';

angular.module('newsfeed', [
  'ngRoute',
  'newsfeedController',
  'newsfeedService'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/newsfeed', {
    templateUrl: 'app/newsfeed/newsfeed.html',
    controller: 'NewsfeedController'
  });
}]);
