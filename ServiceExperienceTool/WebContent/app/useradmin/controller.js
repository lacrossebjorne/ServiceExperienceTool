'use strict'

angular.module('useradmin')

.controller('AdminController', ['$scope', 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    
    //Scope variables 
    $scope.manageRolesShowHide = true;
	$scope.userListShowHide = true;
	$scope.addUserShowHide = true;
	$scope.userRoles = [];
	$scope.user = {};
	$scope.form = {};
	/*$scope.usernameAvailable = true;*/
	$scope.user.roles = [];
	$scope.roleForm = {};
	$scope.today = new Date();
	$scope.isExpanded = false;
	$scope.searchIsExpanded = false;
	$scope.regexUserName = '([a-z])\\w+\\.([a-z])\\w+';
	$scope.regexNumber = '\\d+';
	$scope.sortReverse = false;
	$scope.usernameValidationCheckKey = '';
	
	//Local variables
	self.users = [];
	self.resetPassword = "";
	self.user = {};
	self.roleList = [];
	self.userRoles = [];
	
	//User Functions
	$scope.submitUser = function() {
    	if ($scope.user.password === $scope.user.verifypassword) {
    		if ($scope.form.newUserForm.$valid) {
    			if ($scope.user.userId == null)
    				self.createUser($scope.user);
    			else
    				self.updateUser($scope.user)
    		}
    	}
    };
	
	self.listAllUsers = function() {
    	AdminFactory.listAllUsers()
    	.$promise.then(function(data){
    		if (data.isValid) {
    			$scope.users = data.userList;
    		}
    	});
    };

    self.createUser = function(user) {
    	AdminFactory.createUser({user : user})
    	.$promise.then(function(data) {
    		if(data.isValid) {
    			alert("Användare (" + user.username + ") skapad med AnvändarID: " + data.user.userId);
    			self.reset('addUserView');
    		} else
    			alert('Ett fel upstod när användaren skulle skapas');
    	});    	
    };
    
    $scope.usernameUpdated = function() {
    	$scope.usernameChanged = true; 
    };

    self.updateUser = function(user) {
        AdminFactory.updateUser({user : user})
            .$promise.then(function(data) {
            	self.showHide('addUserView');
            	if(data.isUpdated) {
            		self.listAllUsers();
            		alert('Användare (' + user.username + ') har uppdaterats');
            		self.reset('addUserView');
            	}
            	else {
            		alert('Ett fel uppstod när användaren skulle uppdateras');
                }
            });
    };
    
    $scope.editUser = function() {
    	var user = this.user;
    	self.user = angular.copy(user);
    	$scope.usernameValidationCheckKey = user.username;
    	self.listAllRoles().then(function(roleList) {
    		var roleObj = {};
    		for (var i = 0; i < roleList.length; i++) {
    			roleObj[roleList[i].name] = false;
    		}
    		angular.forEach(user.roles, function(role){
    				roleObj[role.name] = true;
    		});
    		var j = 0;
    		for (var item in roleObj) {
    			var obj = {};
    			obj[item] = roleObj[item];
    			self.user.roles[j] = obj;
    			j++;
    		}
    		self.showHide('addUserView');
    		$scope.user = self.user;
    	});
    };

    self.deleteUser = function(user) {
        AdminFactory.deleteUser({user : user.userId})
            .$promise.then(function(data) {
            	if(data.isDeleted) {
            		self.listAllUsers();
            		alert('Användare ' + user.username + ' har raderats');
            	}
            	else {
                    alert('Ett fel upstod när användaren skulle raderas');
                }
            });
    };
    
    $scope.deleteUser = function() {
    	var answer = confirm('Är du säker på att radera användaren:\nAnvändarID: ' + this.user.userId + ' Användarnamn: ' + this.user.username + '\nDetta går inte att ångra.');
    	if(answer)
    		self.deleteUser(this.user);
    };
    
    //Role functions
    $scope.submitRole = function() {
    	if ($scope.form.newRoleForm.$valid) {
    		if($scope.roleForm.roleId == null)
    			self.createRole($scope.roleForm);
    		else
    			self.updateRole($scope.roleForm);
    		self.reset('manageRolesView');
    	}
    };
    
    self.createRole = function(roleForm) {
    	AdminFactory.createRole({role : roleForm})
    	.$promise.then(function(data) {
    		if (data.isValid) {
    			self.listAllRoles();
    			alert('Rollen (' + roleForm.name + ') skapad med Roll-ID: ' + data.role.roleId);
    		} else
    			alert('Ett fel uppstod när rollen skulle skapas.')
    	});
    };
    
    self.listAllRoles = function() {
    	return AdminFactory.listAllRoles()
    	.$promise.then(function(data) {
    		if(data.isValid) {
    			$scope.roleList = data.roleList;
    			return data.roleList;
    		}
    	});
    };
    
    self.updateRole = function(roleForm) {
    	AdminFactory.updateRole({role : roleForm})
    	.$promise.then(function(data) {
    		if(data.isUpdated) {
    			self.listAllRoles();
    			alert('Rollen ' + roleForm.name + ' har uppdaterats.');
    		}
    		else
    			alert('Ett fel uppstod när rollen uppdaterades.');
    	});
    };
    
    $scope.editRole = function() {
    	var role = this.role;
    	$scope.roleForm = angular.copy(role);
    };
    
    self.deleteRole = function(role) {
    	AdminFactory.deleteRole({roleId : role.roleId})
    	.$promise.then(function(data) {
    		if(data.isDeleted) {
    			self.listAllRoles();
    			alert('Rollen ' + role.name + '  har raderats');
    		}
    		else
    			alert('Ett fel uppstod när rollen ' + role.name + ' skulle raderas');
    	});
    };
    
    $scope.deleteRole = function() {
    	var answer = confirm('Är du säker på att du vill radera rollen:\nRoleID: ' + this.role.roleId + ' : ' + this.role.name + '\nDetta går inte att ångra.');
    	if(answer)
    		self.deleteRole(this.role);
    };
    
    
    //Clean up the lists and tables when you close them
    self.reset = function(view) {
    	$scope.searchIsExpanded = false;
    	$scope.search = {};
    	$scope.isExpanded = false;
    	$scope.roleForm = {};
    	if (view == 'addUserView') {
    		$scope.user = {};
    		$scope.user.roles = [];
    		$scope.user.resetPassword = [];
    		self.user = {};
    		$scope.usernameValidationCheckKey = '';
    		$scope.form.newUserForm.$setPristine();
    		$scope.form.newUserForm.$setUntouched();
    	} else if (view == 'manageRolesView') {
    		$scope.roleList = [];
    		$scope.form.newRoleForm.$setPristine();
    		$scope.form.newRoleForm.$setUntouched();
    	} else if (view == 'userListView') {
    		$scope.users = [];
    	}
    };
    
    //Opens the selected modal window
    $scope.showHide = function(id) {
    	self.showHide(id)
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
    			self.reset('addUserView');
    		}
    	    break;
    	case 'userListView':
    		if ($scope.userListShowHide) {
    			$scope.userListShowHide = false;
    			$scope.sortType = 'userId';
    			self.listAllUsers();
    		}
    		else{
    			$scope.userListShowHide = true;
    			self.reset('userListView');
    		}
    	    break;
    	case 'manageRolesView':
    		if ($scope.manageRolesShowHide) {
    			$scope.manageRolesShowHide = false;
    			$scope.sortType = 'roleId';
    			self.listAllRoles();
    		}
    		else {
    			$scope.manageRolesShowHide = true;
    			self.reset('manageRolesView');
    		}
    	    break;
    	}		
    };
    
    //Exands the selected row
    $scope.expandRow = function() {
    	this.isExpanded = !this.isExpanded;
    };
    
    $scope.expandSearch= function() {
    	this.searchIsExpanded = !this.searchIsExpanded;
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
