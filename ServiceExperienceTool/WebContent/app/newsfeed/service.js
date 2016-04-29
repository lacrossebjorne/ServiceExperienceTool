'use strict';

angular.module('newsfeed')

.factory('NewsfetchService', ['$resource', 'app.paths',
  function($resource, paths) {
    return $resource(paths.api + 'newsServlet', {}, {
      query: {
        method: 'GET',
        params: {},
        isArray: false
      }
    });
  }
]);
