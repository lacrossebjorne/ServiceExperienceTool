'use strict';

angular.module('company', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/company', {
    templateUrl: 'app/company/company.html',
    controller: 'CompanyController'
  });
}]);
