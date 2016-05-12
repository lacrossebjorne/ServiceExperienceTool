'use strict';

angular.module('useradmin')

.factory('AdminFactory', ['$resourse','app.paths', '$q', function($resourse, paths, $q) {
    return {
        listAllUsers: function() {
            return $resourse.get(paths.local + 'UserAdminServlet?getUserList')
                .then(
                    function(response) {
                        return response.data;
                    },
                    function(errResponse) {
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        createUser: function(user) {
            return $resourse.post(paths.local + 'UserAdminServlet?insertUser=' + user)
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
            return $resourse.put(paths.local + 'UserAdminServlet?updateUser=' + id, user)
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
            return $resourse.remove(paths.local + 'UserAdminServlet?deleteUser=' + id)
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
    };

}]);
