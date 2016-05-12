'use strict';

angular.module('useradmin')

.factory('AdminFactory', ['$resource','app.paths', '$q', function($resource, paths, $q) {
    return {
        listAllUsers: function() {
            return $resource(paths.local + 'UserAdminServlet?getUserList', {
            	query: {
            		method: 'GET',
            		params: {},
            		isArray: true
            	}
            });
                /*.then(
                    function(response) {
                        return response.data;
                    },
                    function(errResponse) {
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );*/
        }/*,

        createUser: function(user) {
            return $resource.post(paths.local + 'UserAdminServlet?insertUser=' + user)
                .then(
                    function(response) {
                        return response.data;
                    },
                    function(errResponse) {
                        console.error('Error while creating user');
                        return $q.reject(errResponse);
                    }
                );
        },

        updateUser: function(user, id) {
            return $resource.put(paths.local + 'UserAdminServlet?updateUser=' + id, user)
                .then(
                    function(response) {
                        return response.data;
                    },
                    function(errResponse) {
                        console.error('Error while updating user');
                        return $q.reject(errResponse);
                    }
                );
        },

        deleteUser: function(id) {
            return $resource.remove(paths.local + 'UserAdminServlet?deleteUser=' + id)
                .then(
                    function(response) {
                        return response.data;
                    },
                    function(errResponse) {
                        console.error('Error while deleting user');
                        return $q.reject(errResponse);
                    }
                );
        }
    };*/
    }
}]);
