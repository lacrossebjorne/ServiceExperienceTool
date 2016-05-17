'use strict';

/* App Module */

angular.module('mainView', [
  'ngRoute',
  'newsfeed',
  'newspublish',
  'menu',
  'useradmin'
])

/*
 * This routing should only redirect on '.otherwise'.
 * Separate routing in corresponding module.
 *  */

.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.otherwise({
        redirectTo: '/newsfeed'
      });
  }
])

.controller('MainViewController', MainViewController);

MainViewController.$inject = ['$scope', 'newsfeedservice'];

function MainViewController($scope, newsfeedservice) {
//	console.log('MainViewController initialized');
	var vm = this;
	vm.test = newsfeedservice.test();
	vm.testRes = newsfeedservice.getMovie('Titanic', '1997', 'short');
};
