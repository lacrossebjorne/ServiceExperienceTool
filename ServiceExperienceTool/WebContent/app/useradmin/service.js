/*
 * @author Björn Dalberg
 * 
 * API for connectivity to the backend.
 * 
 */

'use strict';

angular.module('useradmin')

.factory('AdminFactory', ['$resource','app.paths', function($resource, paths) {
    return $resource(paths.local + 'userAdminServlet', {}, {
        
    	listAllUsers: {
        	method: 'GET',
        	params: {'getUserList' : ''},
    		isArray: false
        },
        
        get: {
        	method: 'GET',
        	params: { 'findUser' : '@id' },
    		isArray: false
        },
    
    	listAllRoles: {
    		method: 'GET',
    		params: { 'getRolesList' : ''},
    		isArray: false
    	},
    	
    	createUser: {
            method: 'POST',
            params: { 'insertUser': '@user'},
    	},
    	
    	updateUser: {
            method: 'POST',
            params: { 'updateUser' : '@user'}
    	},
    	
    	deleteUser: {
            method: 'POST',
            params: { 'deleteUser' : '@user'}
    	},
    	
    	existUsername: {
    		method: 'GET',
    		params: { 'existUsername' : '@username'}
    	},
    	
    	createRole: {
            method: 'POST',
            params: { 'insertRole': '@role'},
    	},
    	
    	updateRole: {
            method: 'POST',
            params: { 'updateRole' : '@role'}
    	},
    	
    	deleteRole: {
            method: 'POST',
            params: { 'deleteRole' : '@roleId'}
    	},
    	
    });
}]);
