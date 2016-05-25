'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'menu',
  'company',
  'useradmin',
  'schedule'
])

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
	$scope.hideCol = false;
	hideColumn();
	$scope.$on("$locationChangeSuccess", function(event, next, current) {
		hideColumn();
	});

	function hideColumn(url) {
		if ($location.path() == '/useradmin')
			$scope.hideCol = true;
		else
			$scope.hideCol = false;
	}
};
