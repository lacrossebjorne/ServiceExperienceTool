'use strict'

angular.module('auth')

.controller('AuthController', ['$scope', 'AuthService', function($scope, AuthService) {
	
	$scope.login = function(loginForm) {
		var data = AuthService.login({user : loginForm})
		.$promise.then(function(data) {
			if(data.isValid)
				console.log(data.isValid);
		});
	};
}]);