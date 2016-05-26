'use strict';

angular.module('program-pm', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/program-pm', {
    templateUrl: 'app/program-pm/program-pm.html',
    controller: 'ProgramPmController'
  });
}])

.config(function($provide) {
  $provide.value('program-pm.data', {
    tabs: [{
      title: 'Måndag',
      url: 'app/program-pm/templates/tabs/tab.monday.html'
    }, {
      title: 'Tisdag',
      url: 'app/program-pm/templates/tabs/tab.tuesday.html'
    }, {
      title: 'Onsdag',
      url: 'app/program-pm/templates/tabs/tab.wednesday.html'
    }, {
      title: 'Torsdag',
      url: 'app/program-pm/templates/tabs/tab.thursday.html'
    }, {
        title: 'Fredag',
        url: 'app/program-pm/templates/tabs/tab.friday.html'
      }, {
          title: 'Lördag',
          url: 'app/program-pm/templates/tabs/tab.saturday.html'
        }, {
            title: 'Söndag',
            url: 'app/program-pm/templates/tabs/tab.sunday.html'
          }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
