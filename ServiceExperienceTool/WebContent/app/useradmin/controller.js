'use strict'

angular.module('useradmin')

.controller('AdminController', ['$scope', 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    
    //Scope variables 
    $scope.manageRolesShowHide = true;
	$scope.userListShowHide = true;
	$scope.addUserShowHide = true;
	$scope.userRoles = [];
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
	$scope.userForm.roles = [];
	$scope.roleForm = {};
	$scope.today = new Date();
	$scope.isExpanded = false;
	$scope.searchIsExpanded = false;
	$scope.regexUserName = '([a-z])\\w+\\.([a-z])\\w+';
	$scope.regexNumber = '\\d+';
	$scope.sortReverse = false;
	
	//Local variables
	self.users = [];
	self.resetPassword = "";
	self.userForm = {};
	self.userForm.roles = [];
	self.roleList = [];
	self.userRoles = [];
	
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
    	AdminFactory.listAllUsers()
    	.$promise.then(function(data){
    		if (data.isValid) {
    			$scope.users = data.userList;
    		}
    	});
    };

    self.createUser = function(userForm) {
        AdminFactory.createUser({user : userForm})
            .$promise.then(function(data) {
            	if(data.isValid)
            		alert("Ny användare (" + userForm.username + ") skapad med AnvändarID: " + data.userId);
            	else {
            		alert('Ett fel upstod när användaren skulle skapas');
                }
            });
    };

    self.updateUser = function(userForm) {
        AdminFactory.updateUser({user : userForm})
            .$promise.then(function(data) {
            	self.showHide('addUserView');
            	if(data.isUpdated) {
            		self.listAllUsers();
            		alert('Användare (' + userForm.username + ') har uppdaterats');
            	}
            	else {
            		alert('Ett fel uppstod när användaren skulle uppdateras');
                }
            });
    };
    
    $scope.editUser = function() {
    	var user = this.user;
    	self.userForm = angular.copy(user);
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
    			self.userForm.roles[j] = obj;
    			j++;
    		}
    		self.showHide('addUserView');
    		$scope.userForm = self.userForm;
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
    	return AdminFactory.listAllRoles()
    	.$promise.then(function(data) {
    		if(data.isValid) {
    			$scope.roleList = data.roleList;
    			return data.roleList;
    		}
    	});
    };
    
    self.updateRole = function(roleForm) {
    	console.log(roleForm);
    	AdminFactory.updateRole({role : roleForm})
    	.$promise.then(function(data) {
    		if(data.isUpdated) {
    			self.listAllRoles();
    			alert('Rollen ' + roleForm.name + ' har uppdaterats');
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
    	AdminFactory.deleteRole({role : role.roleId})
    	.$promise.then(function(data) {
    		if(data.isDeleted)
    			alert('Rollen ' + role.name + '  har raderats');
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
    self.close = function(view) {
    	$scope.searchIsExpanded = false;
    	$scope.search = {};
    	$scope.isExpanded = false;
    	$scope.userRoles = [];
    	self.userRoles = [];
    	$scope.roleForm = {};
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
    		self.userForm = {};
    		self.userForm.roles = [];
    	} else if (view == 'manageRolesView') {
    		$scope.roleList = [];
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
    			self.close('addUserView');
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
    			self.close('userListView');
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
    			
    			self.close('manageRolesView');
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
