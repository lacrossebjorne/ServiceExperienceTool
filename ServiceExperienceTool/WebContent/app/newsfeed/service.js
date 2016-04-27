'use strict';

angular.module('newsfeed')

.factory('NewsfetchService', ['$resource', 'app.paths',
  function($resource, paths) {
    return $resource(paths.nytimes, {}, {
      query: {
        method: 'GET',
        params: {},
        isArray: false
      }
    });
  }
]);
