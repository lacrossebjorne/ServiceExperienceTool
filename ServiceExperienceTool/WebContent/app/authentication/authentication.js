'use strict'

angular.module('auth', [ 
	'ngRoute',
	'ngResource'
])
	
.config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/login', {
		templateUrl : 'app/authentication/login.html',
		controller : 'AuthController'
	});
}]);