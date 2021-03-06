/*
 * @author Björn Dalberg
 * 
 * useradmin module
 * Controller AdminController
 * Custom directive usernameValidation
 * 
 */

'use strict'

angular.module('useradmin', [ 
	'ngRoute',
	'ngResource'
])
	
.config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/useradmin', {
		templateUrl : 'app/useradmin/user_admin.html',
		controller : 'AdminController',
		directive : 'usernameValidation'
	});
}]);