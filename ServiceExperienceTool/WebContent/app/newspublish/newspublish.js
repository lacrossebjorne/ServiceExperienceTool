'use strict';

angular.module('newspublish', [
  'ngRoute',
  'newspublishController'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/newspublish', {
    templateUrl: 'app/newspublish/newspublish.html',
    controller: 'NewsPublishController'
  });
}]);


//.config(['$routeProvider', function($routeProvider) {
//  $routeProvider.when('/newsfeed', {
//    templateUrl: 'app/newsfeed/newsfeed.html',
//    controller: 'NewsfeedController'
//  });
//}]);
