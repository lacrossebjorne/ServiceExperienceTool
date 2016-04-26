'use strict';

/* App Module */

angular.module('app', [
  'ngRoute',
  'ngAnimate-animate.css',
  'mainView'
])
.run(['$route', function($route)  {
  $route.reload();
}]);
