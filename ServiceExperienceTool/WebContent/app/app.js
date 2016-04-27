'use strict';

/* App Module */

angular.module('app', [
  'ngRoute',
  'ngAnimate-animate.css',
  'mainView'
])
.run(['$route', function($route)  {
  $route.reload();
}])
.config(function($provide) {
    $provide.value('APIpath', 'http://78.68.50.137:8080/ServiceExperienceTool/');
});