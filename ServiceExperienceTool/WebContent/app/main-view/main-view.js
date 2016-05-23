'use strict';

/* App Module */

angular.module('mainView',
		[ 'ngRoute', 'newsfeed', 'newspublish', 'menu', 'company' ])

/*
 * This routing should only redirect on '.otherwise'. Separate routing in
 * corresponding module.
 */

.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.otherwise({
		redirectTo : '/newsfeed'
	});
} ])

.controller('MainViewController', MainViewController);

MainViewController.$inject = [ '$rootScope', '$scope', '$location', '$log' ];

function MainViewController($rootScope, $scope, $location, $log) {
	$scope.hideCol;
	hideColumn();
	$scope.$on("$locationChangeSuccess", function(event, next, current) {
		hideColumn();
	});

	function hideColumn(url) {
		if ($location.path() === '/menu')
			$scope.hideCol = false;
		else
			$scope.hideCol = true;
	}
};
