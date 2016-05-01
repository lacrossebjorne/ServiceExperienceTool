app.controller('AdminController', [$scope, 'AdminFactory', function($scope, AdminFactory) {
    var self = this;
    self.users = [];
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

    self.listAllUsers = function() {
        AdminFactory.listAllUsers()
            .then(
                function(data) {
                    self.users = data;
                },
                function(errResponse) {
                    console.error('Error fetching data');
                }
            );
    };

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

    self.listAllUsers();

    self.submit = function() {
        if (self.user.id === null) {
            self.createUser(self.user);
        } else {
            self.updateUser(self.user, self.user.id)
        }
        self.close();
    };

    self.edit = function(id) {
        angular.forEach(self.users, function(user) {
            if (user.id === id) {
                self.user = angular.copy(user);
                break;
            }
        });
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
}]);
