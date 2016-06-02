'use strict';

angular.module('auth')

.factory('AuthService', ['$resource','app.paths', '$q', function($resource, paths, $q) {
    return $resource(paths.api + 'LoginServlet', {}, {
        login: {
        	method: 'POST',
        	params: { 'login' : '@login' },
        	isArray: false
        	
        }
    });
}]);
    	