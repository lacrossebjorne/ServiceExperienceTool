'use strict';

angular.module('newspublish', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/newspublish', {
	  	templateUrl: 'app/newspublish/newspublish.html',
	  	controller: 'NewspublishController',
	    service: 'NewsPublishService',
	    directive: 'fileModel'
  });
}]);
