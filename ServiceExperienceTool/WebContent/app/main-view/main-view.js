'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'newsPublishService',
  'filePublishDirective'
])

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.when('/menu', {
        templateUrl: 'app/menu/menu.html'
          //, controller: 'ScheduleController'
      })
      .when('/newspublish', {
    	  templateUrl: 'app/newspublish/newspublish.html',
    	  controller: 'NewspublishController',
        service: 'multipartForm',
        directive: 'fileModel'
//        redirectTo: '/newspublish'
      })
      .otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
