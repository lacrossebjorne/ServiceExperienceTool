'user strict'

angular.module('useradmin')

.controller('AdminController', ['$scope', 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    $scope.manageRolesShowHide = false;
	$scope.userListShowHide = false;
	$scope.addUserShowHide = false;
    
   // self.users = [];
    self.resetPassword = {};
    self.userRoles = {
        reception: false,
        service: false,
        kitchen: false,
        conference: false,
        maintenance: false,
        housekeeping: false,
        admin: false
    };
    self.user = {
        id: null,
        first_name: '',
        last_name: '',
        user_name: '',
        email: '',
        password: '',
        resetPassword: null,
        userRoles: null
    };
    
    self.users = AdminFactory.listAllUsers();

    /*self.listAllUsers = function() {
        AdminFactory.listAllUsers()
            .then(
                function(data) {
                    self.users = data;
                    console.log(self.users);
                },
                function(errResponse) {
                    console.error('Error fetching data');
                }
            );
    };*/

    self.createUser = function() {
        AdminFactory.createUser(user)
            .then(
                self.listAllUsers(),
                function(errResponse) {
                    console.error('Error creating user');
                }
            );
    };

    self.updateUser = function() {
        AdminFactory.updateUser(user, id)
            .then(
                self.listAllUsers(),
                function(errResponse) {
                    console.error('Error creating user');
                }
            );
    };

    self.deleteUser = function() {
        AdminFactory.deleteUser(id)
            .then(
                self.listAllUsers(),
                function(errResponse) {
                    console.error('Error creating user');
                }
            );
    };

    //self.listAllUsers();

    self.submit = function() {
        if (self.user.id === null) {
            self.createUser(self.user);
        } else {
            self.updateUser(self.user, self.user.id)
        }
        self.close();
    };

    self.edit = function(id) {
    	for(var i = 0; i < self.users.length; i++) {
    		if(self.users[i].id === id) {
    			self.user = angular.copy(user);
    			break;
    		}
    	}
    };

    self.close = function() {
        self.roles = {reception: false, service: false, kitchen: false, conference: false, maintenance: false, housekeeping: false, admin: false};
        self.user = {
            id: null,
            first_name: '',
            last_name: '',
            user_name: '',
            email: '',
            password: '',
            resetPassword: null,
            userRoles: null
        };
        $scope.userForm.$setPristine();
        $location.path('/user_administration');
    };
    
    $scope.showHide = function(id) {
    	
    	switch (id) {
    	case 'addUserView':
    		console.log(id)
    		if ($scope.addUserShowHide)
    			 $scope.addUserShowHide = false;
    		else
    			$scope.addUserShowHide = true;
    	    break;
    	case 'userListView':
    		console.log(id);
    		if ($scope.userListShowHide)
    			$scope.userListShowHide = false;
    		else
    			$scope.userListShowHide = true;
    		console.log($scope.userListShowHide);
    	    break;
    	case 'manageRolesView':
    		console.log(id);
    		if ($scope.manageRolesShowHide)
    			 $scope.manageRolesShowHide = false;
    		else
    			$scope.manageRolesShowHide = true;
    	    break;
    	}
    		
    }
    
}]);
