'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'menu',
  'company',
  'useradmin',
  'schedule',
  'sms',
  'ngMaterial',
  'other-products',
  'program-pm',
  'team',
  'our-guests',
  'security',
  'contacts',
  'salary'
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
		if ($location.path() == '/useradmin' || $location.path() == '/schedule' )
			$scope.hideCol = true;
		else
			$scope.hideCol = false;
	}
};
