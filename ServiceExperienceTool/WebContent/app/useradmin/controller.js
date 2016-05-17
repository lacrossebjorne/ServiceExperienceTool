'user strict'

angular.module('useradmin')

.controller('AdminController', ['$scope', 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    //Hides left and right columns
    hideLeftRightColumns();
    
    $scope.manageRolesShowHide = true;
	$scope.userListShowHide = true;
	$scope.addUserShowHide = true;
	$scope.userRoles = [];
	self.users = [];
	self.resetPassword = "";
	$scope.user = {
        id: null,
        firstname: '',
        lastname: '',
        username: '',
        email: '',
        phone: '',
        enabled: false,
        password: '',
        resetPassword: null,
        userRoles: null
    };
	$scope.userForm = {};
	$scope.roleForm = {};
	$scope.userForm.roles = [];
	
	$scope.submitUser = function() {
    	if ($scope.userForm.password === $scope.userForm.verifypassword) {
    		if ($scope.newUserForm.$valid) {
    			if ($scope.userForm.userId == null)
    				self.createUser($scope.userForm);
    			else
    				self.updateUser($scope.userForm)
    			self.close($scope.newUserForm);
    		}
    	}
    };
	
	self.listAllUsers = function() {
    	var data = AdminFactory.listAllUsers()
    	.$promise.then(function(data){
    		if (data.isValid) {
    			$scope.users = data.userList;
    		}
    	});
    };

    self.createUser = function(userForm) {
        var data = AdminFactory.createUser({user : userForm})
            .$promise.then(function(data) {
            	if(data.isValid)
            		alert("New user (" + userForm.username + ") created with UserID: " + data.userId);
            	else {
            		alert('Error creating user');
                }
            });
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
    
    self.editUser = function(id) {
    	for(var i = 0; i < self.users.length; i++) {
    		if(self.users[i].id === id) {
    			self.user = angular.copy(user);
    			break;
    		}
    	}
    };

    self.deleteUser = function() {
        AdminFactory.deleteUser(id)
            .then(
                self.listAllUsers(),
                function(errResponse) {
                    console.error('Error deleting user');
                }
            );
    };
    
    $scope.submitRole = function() {
    	if ($scope.newRoleForm.$valid) {
    		if($scope.roleForm.roleId == null)
    			self.createRole($scope.roleForm);
    		else
    			self.updateRole($scope.roleForm);
    		self.close($scope.newRoleForm);
    	}
    };
    
    self.createRole = function(roleForm) {
    	AdminFactory.createRole({role : roleForm})
    	.$promise.then(function(data) {
    		if (data.isValid) {
    			
    		}
    		
    	});
    };
    
    self.listAllRoles = function() {
    	var data = AdminFactory.listAllRoles()
    	.$promise.then(function(data) {
    		if(data.isValid) {
    			$scope.roleList = data.roleList;
    		}
    	});
    };

    

    self.close = function(form) {
        //self.roles = {reception: false, service: false, kitchen: false, conference: false, maintenance: false, housekeeping: false, admin: false};
    	$scope.userRoles = [];
    	$scope.user = {
            id: null,
            first_name: '',
            last_name: '',
            user_name: '',
            email: '',
            password: '',
            resetPassword: null,
            userRoles: null
        };
    	if (form == $scope.newUserForm) {
    		$scope.userForm = {};
    		$scope.userForm.roles = [];
    		$scope.newUserForm.$setPristine();
    	} else {
    		$scope.roleList = [];
    		$scope.roleForm = {};
            $scope.newRoleForm.$setPristine();
    	}
    };
    
/*    $scope.addRemoveUserRole = function (role) {
    	console.log(role);
    	if ($scope.userRoles[role]) {
    		var i = $scope.userRoles(role);
    		$scope.userRole.splice(i, 1);
    	}
    	else
    		$scope.userRole.push(role);
    	console.log(self.userRole);
    }*/
    
    $scope.showHide = function(id) {
    	switch (id) {
    	case 'addUserView':
    		if ($scope.addUserShowHide) {
    			 $scope.addUserShowHide = false;
    			 self.listAllRoles();
    		}
    		else {
    			$scope.addUserShowHide = true;
    			self.close($scope.newUserForm);
    		}
    	    break;
    	case 'userListView':
    		if ($scope.userListShowHide) {
    			$scope.userListShowHide = false;
    			self.listAllUsers();
    		}
    		else{
    			$scope.userListShowHide = true;
    			$scope.users = [];
    		}
    	    break;
    	case 'manageRolesView':
    		if ($scope.manageRolesShowHide) {
    			$scope.manageRolesShowHide = false;
    			self.listAllRoles();
    		}
    		else {
    			$scope.manageRolesShowHide = true;
    			self.close($scope.newRoleForm);
    		}
    	    break;
    	}		
    };
    
    $scope.expandRow = function (id) {
    	var hidden = angular.element(document.getElementById(id));
    	if(hidden.css('display') != 'table-row')
    		hidden.css('display', 'table-row');
    	else
    		hidden.css('display', 'none');
    };
    
    $scope.w3_open = function() {
    	document.getElementsByClassName("w3-sidenav")[0].style.display = "block";
		document.getElementsByClassName("w3-overlay")[0].style.display = "block";
    }
    $scope.w3_close = function() {
    	document.getElementsByClassName("w3-sidenav")[0].style.display = "none";
		document.getElementsByClassName("w3-overlay")[0].style.display = "none";
    };
    

    function hideLeftRightColumns() {
    	var leftCol = angular.element(document.querySelector('#mainLeftColumn'));
    	var rightCol = angular.element(document.querySelector('#mainRightColumn'));
    	var midCol = angular.element(document.querySelector('#mainMiddleColumn'));
    	if (leftCol.prop('display') != 'none' && rightCol.prop('display') != 'none') {
    		leftCol.css('display', 'none');
    		rightCol.css('display', 'none');
    		midCol.removeClass('m7').addClass('m12');
    	}
    }

}]);
