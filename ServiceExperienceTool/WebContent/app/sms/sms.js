'use strict';

angular.module('sms', [
	'ngRoute', 
	'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/sms', {
		templateUrl: 'app/sms/sms.html',
		controller: 'SMSController'
	});
}]);