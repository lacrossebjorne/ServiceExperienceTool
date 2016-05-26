'use strict';

angular.module('security', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/security', {
    templateUrl: 'app/security/security.html',
    controller: 'SecurityController'
  });
}])

.config(function($provide) {
  $provide.value('security.data', {
    tabs: [{
      title: 'Egen Kontroll',
      url: 'app/security/templates/tabs/tab.egen-kontroll.html'
    }, {
      title: 'Alkoholhantering',
      url: 'app/security/templates/tabs/tab.alkoholhantering.html'
    }, {
      title: 'Brandsäkerhet',
      url: 'app/security/templates/tabs/tab.brandsakerhet.html'
    }, {
      title: 'Hot, Inbrott & Stöld',
      url: 'app/security/templates/tabs/tab.hot-inbrott-stold.html'
    }, {
        title: 'Arbetsmiljö',
        url: 'app/security/templates/tabs/tab.arbetsmiljo.html'
      }, {
          title: 'Tillstånd',
          url: 'app/security/templates/tabs/tab.tillstand.html'
        }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
