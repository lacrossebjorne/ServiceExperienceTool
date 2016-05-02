'use strict';

angular.module('newsfeed')

.factory('NewsfetchService', ['$resource', 'app.paths',
  function($resource, paths) {
    console.log('NewsfetchService calls: '+ paths.local);
    return $resource(paths.local + "newsServlet", {}, {
      query: {
        method: 'GET',
        params: {},
        isArray: true
      }
    });
  }
]);
