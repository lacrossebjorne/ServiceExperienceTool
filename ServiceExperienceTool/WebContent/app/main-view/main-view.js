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
  	  	templateUrl: 'app/newspublish/newspublish.html',
	  	controller: 'NewspublishController',
	    service: 'multipartForm',
	    directive: 'fileModel'	
      })
      .otherwise({
        redirectTo: '/newsfeed'
      });
  }
]);
