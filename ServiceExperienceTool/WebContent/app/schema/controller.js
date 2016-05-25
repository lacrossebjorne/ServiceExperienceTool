'use strict';

angular.module('company')

.controller('CompanyController', ['$scope', '$location', 'schedule.data', function($scope, $location, scheduleData) {

  $scope.resources = scheduleData.resources;
  $scope.events = scheduleData.events;
  $scope.options = scheduleData.options;

}]);