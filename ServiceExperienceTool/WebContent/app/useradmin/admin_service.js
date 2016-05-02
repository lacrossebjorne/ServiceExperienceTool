app.factory('AdminFactory', ['$http', '$q', function($http, $q) {
    return {
        listAllUsers: function() {
            return $http.get('http://localhost:8080/ServiceExperienceTool_DOA_Login/UserAdminServlet?getUserList')
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
            return $http.post('http://localhost:8080/ServiceExperienceTool_DOA_Login/UserAdminServlet?insertUser=' + user)
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
            return $http.put('http://localhost:8080/ServiceExperienceTool_DOA_Login/UserAdminServlet?updateUser=' + id, user)
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
            return $http.remove('http://localhost:8080/ServiceExperienceTool_DOA_Login/UserAdminServlet?deleteUser=' + id)
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
