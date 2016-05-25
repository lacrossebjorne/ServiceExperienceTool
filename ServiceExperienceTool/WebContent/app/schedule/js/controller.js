var app = angular.module('myApp', ['ngMaterial', 'ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/schedule', {
            templateUrl: 'app/schema/schema.html',
            controller: 'SchemaController'
        });
    }])

    .controller('SchemaController', function ($scope, $mdDialog, $mdMedia) {

        $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
        $scope.status = ' ';
        $scope.departmentSelected;
        $scope.employeeSelected;

        $scope.showAdvanced = function (ev) {
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'dialog1.tmpl.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: useFullScreen
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
            $scope.$watch(function () {
                return $mdMedia('xs') || $mdMedia('sm');
            }, function (wantsFullScreen) {
                $scope.customFullscreen = (wantsFullScreen === true);
            });
        };

        function DialogController($scope, $mdDialog) {

            $scope.departments = getDepartments();
            $scope.employees = getEmployees();

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };
        }
        function getDepartments() {
            return [
                {
                    id: 1, name: 'Restaurang',
                    children: [
                        { id: 1, name: 'Varmkök' },
                        { id: 2, name: 'Kallis' },
                        { id: 3, name: 'Servis' }
                    ]
                },
                {
                    id: 2, name: 'Hotell',
                    children: [


                        { id: 1, name: 'Reception' },
                        { id: 2, name: 'Städ' },
                        { id: 3, name: 'Vaktmästariet' }
                    ]
                }
            ];
        }
        function getEmployees() {
            return ['Mats Heed', 'Ingvar Kamprad', 'Anders Filipsson', 'Kalle Eriksson'];
        }
    });
