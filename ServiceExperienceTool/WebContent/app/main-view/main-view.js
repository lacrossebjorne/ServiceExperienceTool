'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed'
])

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.when('/menu', {
        templateUrl: 'app/menu/menu.html'
          //, controller: 'ScheduleController'
      })
      .otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
