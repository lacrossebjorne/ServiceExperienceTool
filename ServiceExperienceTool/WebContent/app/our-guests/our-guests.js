'use strict';

angular.module('our-guests', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/our-guests', {
    templateUrl: 'app/our-guests/our-guests.html',
    controller: 'OurGuestsController'
  });
}])

.config(function($provide) {
  $provide.value('guests.data', {
    tabs: [{
      title: 'Stamgäster',
      url: 'app/our-guests/templates/tabs/tab.stamgaster.html'
    }, {
      title: 'Våra Gäster',
      url: 'app/our-guests/templates/tabs/tab.vara-gaster.html'
    }, {
      title: 'Klagomålshantering',
      url: 'app/our-guests/templates/tabs/tab.klagomalshantering.html'
    }, {
      title: 'Kvalitetssäkring',
      url: 'app/our-guests/templates/tabs/tab.kvalitetssakring.html'
    }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
