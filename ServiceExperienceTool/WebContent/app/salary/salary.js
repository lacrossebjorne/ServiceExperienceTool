'use strict';

angular.module('salary', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/salary', {
    templateUrl: 'app/salary/salary.html',
    controller: 'SalaryController'
  });
}])

.config(function($provide) {
  $provide.value('salary.data', {
    tabs: [{
      title: 'Logi',
      url: 'app/salary/templates/tabs/tab.logi.html'
    }, {
      title: 'Paket',
      url: 'app/salary/templates/tabs/tab.paket.html'
    }, {
      title: 'Aktiviteter',
      url: 'app/salary/templates/tabs/tab.aktiviteter.html'
    }, {
      title: 'Special',
      url: 'app/salary/templates/tabs/tab.special.html'
    }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
