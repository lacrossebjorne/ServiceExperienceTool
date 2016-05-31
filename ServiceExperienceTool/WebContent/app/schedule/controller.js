'use strict';

angular.module('schedule')

    .controller('ScheduleController', ['$scope', '$location', 'schedule.data', function ($scope, $location, scheduleData) {

        $scope.options = scheduleData.options;
        
        $scope.status = ' ';
        $scope.departmentSelected;
        $scope.employeeSelected;

        $scope.departments = getDepartments();
        $scope.employees = getEmployees();

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