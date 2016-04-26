'use strict';

angular.module('newsfeedService', ['ngResource'])

.factory('NewsfetchService', ['$resource',
  function($resource) {
    return $resource('http://api.nytimes.com/svc/news/v3/content/all/all/.json?api-key=ddbb1c79dda444649d290bc19bd923c6%3A12%3A75027211', {}, {
      query: {
        method: 'GET',
        params: {},
        isArray: false
      }
    });
  }
]);
