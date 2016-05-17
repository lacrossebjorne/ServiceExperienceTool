'use strict';

angular.module('useradmin')

.factory('AdminFactory', ['$resource','app.paths', '$q', function($resource, paths, $q) {
    return {
        listAllUsers: function() {
            return $resource(paths.local + 'userAdminServlet?getUserList', {});
                
        },
    
    	listAllRoles: function() {
    		return $resource(paths.local + 'userAdminServlet?getRolesList', {});
    		
    	},
    	
    	createUser: function(user) {
            return $resource.post(paths.local + 'userAdminServlet?insertUser=' + user)
    	},
    	
    	updateUser: function(user, id) {
            return $resource.put(paths.local + 'userAdminServlet?updateUser=' + id, user)
    	},
    	
    	deleteUser: function(id) {
            return $resource.remove(paths.local + 'userAdminServlet?deleteUser=' + id)
    	}
    }
}]);
