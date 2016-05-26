'use strict';

angular.module('sms')

.controller('SMSController', ['$scope',  function($scope) {
	console.log("inside sms-controller");
	$scope.testData = "this is some testdata";
}]);