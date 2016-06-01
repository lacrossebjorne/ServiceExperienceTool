'use strict';

angular.module('other-products', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/other-products', {
    templateUrl: 'app/other-products/other-products.html',
    controller: 'OtherProductsController'
  });
}])

.config(function($provide) {
  $provide.value('products.data', {
    tabs: [{
      title: 'Logi',
      url: 'app/other-products/templates/tabs/tab.logi.html'
    }, {
      title: 'Paket',
      url: 'app/other-products/templates/tabs/tab.paket.html'
    }, {
      title: 'Aktiviteter',
      url: 'app/other-products/templates/tabs/tab.aktiviteter.html'
    }, {
      title: 'Special',
      url: 'app/other-products/templates/tabs/tab.special.html'
    }],

    imgUris: [
      'img/set_mock-img_05.jpg',
      'img/set_mock-img_06.jpg',
      'img/set_mock-img_07.jpg',
      'img/set_mock-img_08.jpg'
    ]
  });
});
