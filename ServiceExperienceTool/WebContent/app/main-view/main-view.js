'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish'
])

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.when('/menu', {
        templateUrl: 'app/menu/menu.html'
          //, controller: 'ScheduleController'
      })
      .when('/newspublish', {
    	  redirectTo: '/newspublish'
      })
      .otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
