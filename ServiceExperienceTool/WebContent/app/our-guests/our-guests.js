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
      'img/set_mock-img_12.jpg',
      'img/set_mock-img_13.jpg',
      'img/set_mock-img_14.jpg',
      'img/set_mock-img_15.jpg'
    ]
  });
});
