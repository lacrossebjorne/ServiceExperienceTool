'use strict';

angular.module('schedule')

    .controller('ScheduleController', ['$scope', '$location', 'schedule.data', '$mdDialog', '$mdMedia', function ($scope, $location, scheduleData, $mdDialog, $mdMedia) {
    	
    	/*//testar och köra document ready på kalendern i Angular.
    	angular.element(document).ready(function () {
            document.getElementById('calendar').fullCalendar({
            	weekNumbers : true,
            	fixedWeekCount: true,
            	defaultView : 'month',
            	firstDay : 1,
            	defaultDate: '2016-05-12',
            	editable: true,
            	eventLimit: true, // allow "more" link when too many events
            	events : getEvents(),
            	resources : getResources(),
            	schedulerLicenseKey : 'CC-Attribution-NonCommercial-NoDerivatives',
            	header : {
            		left : 'title',
            		center : 'agendaWeek, month',
            		right : 'today prev,next'
            	}
            });
        });*/
    	
    	$scope.events = scheduleData.events;
    	$scope.resources = scheduleData.resources;

    	$scope.test = "Jag Testar";
    	$scope.testArr = ['Mats', 'Arne', 'Erik'];
    	
    	
        $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');
        $scope.status = ' ';
        $scope.departmentSelected;
        $scope.employeeSelected;

        $scope.showAdvanced = function (ev) {
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && $scope.customFullscreen;
            $mdDialog.show({
                controller: ScheduleController,
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

    }]);