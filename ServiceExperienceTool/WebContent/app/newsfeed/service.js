'use strict';

angular.module('newsfeed')

.factory('NewsfetchService', ['$resource', 'app.paths',
  function($resource, paths) {
    console.log('NewsfetchService calls: ' + paths.api);
    return $resource(paths.api + "newsServlet", {}, {
      query: {
        method: 'GET',
        params: {},
        isArray: true
      }
    });
  }
])

.factory('NewsEditService', ['$resource', 'app.paths',
  function($resource, paths) {
    return $resource(paths.api + "newsServlet", {}, {
      save: {
        method: 'POST',
        params: { action: 'updateNews', id: '@id', header: '@header', content: '@content'},
        timeout: 2000
      },
      disable: {
        method: 'POST',
        params: { action: 'disableNews', id: '@id' },
        timeout: 2000
      }
    });
  }
]);
