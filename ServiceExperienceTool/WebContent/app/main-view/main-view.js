'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'menu'
])

/* 
 * This routing should only redirect on '.otherwise'.
 * Separate routing in corresponding module.
 *  */

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
