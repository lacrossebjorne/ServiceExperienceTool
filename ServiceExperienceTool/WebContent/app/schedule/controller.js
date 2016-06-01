'use strict';

angular.module('schedule')

    .controller('ScheduleController', ['$scope', '$http', '$location', 'schedule.data', function ($scope, $http, $location, scheduleData) {

        $scope.options = scheduleData.options;
        
        $scope.status = ' ';
        $scope.departmentSelected;
        $scope.employeeSelected;

        $scope.departments = getDepartments();
        $scope.employees = getEmployees();
        
        $scope.allEvents = function() {
            $http({
                    method : 'GET',
                    url : '/schedule',
                    action: 'getAllEvents'
            }).success(function(data, status, headers, config) {
                    $scope.person = data;
                    console.log(data);
            }).error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
            });

        };
        $scope.allEvents();
       
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
            return [
                   {id: 1, name: 'Mats Heed', workId: 1},
                   {id: 2, name: 'Staffan Adolfsson', workId: 1},
                   {id: 3, name: 'Richard Strandberg', workId: 2},
                   {id: 4, name: 'Gunde Svan', workId: 2},
                   {id: 5, name: 'Elsa Eriksson', workId: 1},
                   {id: 6, name: 'Natalie Hansson', workId: 1},
                   ];
        }

    }]);