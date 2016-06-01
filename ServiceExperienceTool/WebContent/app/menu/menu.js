'use strict';

angular.module('menu', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/menu', {
    templateUrl: 'app/menu/menu.html',
    controller: 'MenuController'
  })
  .when('/menu/edit', {
    templateUrl: 'app/menu/edit/menuedit.html',
    controller: 'MenuEditController'
  });
}]);