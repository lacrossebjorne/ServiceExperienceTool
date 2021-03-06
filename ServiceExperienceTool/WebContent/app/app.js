'use strict';

/* App Module */

angular.module('app', [
  'ngRoute',
  'ngAnimate-animate.css',
  'ngTagsInput',
  'mainView',
  'ngMessages'
])
.run(['$route', function($route)  {
  $route.reload();
}])
.config(function($provide) {
    $provide.value('app.paths', {
      api: 'http://90.226.191.195:8080/ServiceExperienceTool/',
      local: 'http://localhost:8080/ServiceExperienceTool/',
      nytimes: 'http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=ddbb1c79dda444649d290bc19bd923c6%3A12%3A75027211'
    });
});
