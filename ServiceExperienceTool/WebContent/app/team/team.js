'use strict';

angular.module('team', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/team', {
    templateUrl: 'app/team/team.html',
    controller: 'TeamController'
  });
}])

.config(function($provide) {
  $provide.value('team.data', {
    tabs: [{
      title: 'Logi',
      url: 'app/team/templates/tabs/tab.logi.html'
    }, {
      title: 'Paket',
      url: 'app/team/templates/tabs/tab.paket.html'
    }, {
      title: 'Aktiviteter',
      url: 'app/team/templates/tabs/tab.aktiviteter.html'
    }, {
      title: 'Special',
      url: 'app/team/templates/tabs/tab.special.html'
    }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
