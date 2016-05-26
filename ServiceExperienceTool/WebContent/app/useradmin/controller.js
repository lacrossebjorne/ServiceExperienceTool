'use strict'

angular.module('useradmin')

.controller('AdminController', ['$scope', 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    //Hides left and right columns
    //hideLeftRightColumns();
    
//Variables 
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
	$scope.today = new Date();
	$scope.isExpanded = false
	$scope.searchIsExpanded = false
	$scope.regexUserName = '([a-z])\\w+\\.([a-z])\\w+';
	$scope.regexNumber = '\\d+';
	$scope.sortType = 'userId';
	$scope.sortReverse = false;
	
	//User Functions
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
    			angular.forEach($scope.users, function(user){
    			});
    			
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

    self.updateUser = function(userForm) {
        var data = AdminFactory.updateUser({user : userForm})
            .$promise.then(function(data) {
            	if(data.isUpdated)
            		alert('User (' + userForm.username + ') have been updated');
            	else {
            		alert('Error creating user');
                }
                self.listAllUsers();
            });
    };
    
    self.editUser = function(id) {
    	for(var i = 0; i < self.users.length; i++) {
    		if(self.users[i].id === id) {
    			self.user = angular.copy(user);
    			break;
    		}
    	}
    };
    
    $scope.editUser = function() {
    	$scope.userForm = angular.copy(this.user);
    	self.showHide('addUserView');
    };

    self.deleteUser = function(user) {
        AdminFactory.deleteUser({user : user.userId})
            .$promise.then(function(data) {
            	if(data.isValid) {
            		self.listAllUsers();
            		alert('User ' + user.username + ' have been deleted');
            	}
            	else {
                    alert('Error deleting user');
                }
            });
    };
    
    $scope.deleteUser = function() {
    	var answer = confirm('Are you sure you want to delete user:\nUserID: ' + this.user.userId + ' Username: ' + this.user.username + '\nThis can not be undone.');
    	if(answer)
    		self.deleteUser(this.user);
    };
    
    //Role functions
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
    			$scope.roleList.push(data.role);
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
    
    
    //Clean up the lists and tables when you close them
    self.close = function(view) {
    	$scope.userRoles = [];
    	$scope.user = {
            userId: null,
            firstName: '',
            lastName: '',
            username: '',
            email: '',
            password: '',
            phoneNumber:'',
            enable: false,
            resetPassword: null,
            userRoles: null
        };
    	if (view == 'addUserView') {
    		$scope.userForm = {};
    		$scope.userForm.roles = [];
    		$scope.newUserForm.$setPristine();
    	} else if (view == 'manageRolesView') {
    		$scope.manageRolesShowHide = true;
    		$scope.roleList = [];
    		$scope.roleForm = {};
            $scope.newRoleForm.$setPristine();
    	} else if (view == 'userListView') {
    		$scope.users = [];
    	}
    	$scope.searchIsExpanded = false;
    	$scope.search = {};
    };
    
    //Opens the selected modal window
    $scope.showHide = function(id) {
    	self.showHide(id);
    };
    
    self.showHide = function(id) {
    	switch (id) {
    	case 'addUserView':
    		if ($scope.addUserShowHide) {
    			 $scope.addUserShowHide = false;
    			 self.listAllRoles();
    		}
    		else {
    			$scope.addUserShowHide = true;
    			self.close('addUserView');
    		}
    	    break;
    	case 'userListView':
    		if ($scope.userListShowHide) {
    			$scope.userListShowHide = false;
    			self.listAllUsers();
    		}
    		else{
    			$scope.userListShowHide = true;
    			self.close('userListView');
    		}
    	    break;
    	case 'manageRolesView':
    		if ($scope.manageRolesShowHide) {
    			$scope.manageRolesShowHide = false;
    			self.listAllRoles();
    		}
    		else {
    			self.close('manageRolesView');
    		}
    	    break;
    	}		
    };
    
    //Exands the selected row
    $scope.expandRow = function(id) {
    	if(id == 'userSearchRow' | id == 'rolesSearchRow')
    		this.searchIsExpanded = !this.searchIsExpanded;
    	else 
    		this.isExpanded = !this.isExpanded;
    };
    
    //Side nav open close on small screens
    $scope.w3_open = function() {
    	document.getElementsByClassName("w3-sidenav")[0].style.display = "block";
		document.getElementsByClassName("w3-overlay")[0].style.display = "block";
    }
    $scope.w3_close = function() {
    	document.getElementsByClassName("w3-sidenav")[0].style.display = "none";
		document.getElementsByClassName("w3-overlay")[0].style.display = "none";
    };
}]);
