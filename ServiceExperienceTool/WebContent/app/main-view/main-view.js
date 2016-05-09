'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'app.newsfeedservice'
])

/* 
 * This routing should only redirect on '.otherwise'.
 * Separate routing in corresponding module.
 *  */

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.when('/menu', {
        templateUrl: 'app/menu/menu.html'
          //, controller: 'MenuController'
      })
      .otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
