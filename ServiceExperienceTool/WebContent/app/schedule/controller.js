'use strict';

angular.module('schedule')

.controller('ScheduleController', ['$scope', '$location', 'schedule.data', function($scope, $location, scheduleData) {

  $scope.resources = scheduleData.resources;
  $scope.events = scheduleData.events;
  $scope.options = scheduleData.options;

}]);