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
      title: 'Målsättningar',
      url: 'app/team/templates/tabs/tab.malsattningar.html'
    }, {
      title: 'Kontaktlista',
      url: 'app/team/templates/tabs/tab.kontaktlista.html'
    }, {
      title: 'Ansvarsområden',
      url: 'app/team/templates/tabs/tab.ansvarsomraden.html'
    }, {
      title: 'Rutiner & Körscheman',
      url: 'app/team/templates/tabs/tab.rutiner-korscheman.html'
    }, {
        title: 'Utbildningar',
        url: 'app/team/templates/tabs/tab.utbildningar.html'
      }, {
          title: 'Ordbok',
          url: 'app/team/templates/tabs/tab.ordbok.html'
        }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
