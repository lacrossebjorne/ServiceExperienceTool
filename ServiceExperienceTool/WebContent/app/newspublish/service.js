'use strict';

var module = angular.module('newsPublishService', ['ngResource'])
/*
module.factory('newsPublishService ', ['$resource',
  function($resource) {

  }
]);*/

module.service('multipartForm', ['$http', function($http) {
  this.post = function(uploadUrl, data) {
    var fd = new FormData();
    for (var key in data) {
      fd.append(key, data[key]);
      console.log("multipartForm[key:" + key + ", data: " + data[key] + "]");
    }

    $http.post(uploadUrl, fd, { transformRequest: angular.identity,
      headers: {'Content-type': undefined }
    })
  }
}]);
