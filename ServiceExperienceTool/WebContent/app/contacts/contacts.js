'use strict';

angular.module('contacts', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/contacts', {
    templateUrl: 'app/contacts/contacts.html',
    controller: 'ContactsController'
  });
}])

.config(function($provide) {
  $provide.value('contacts.data', {
    tabs: [{
      title: 'Leverantörer',
      url: 'app/contacts/templates/tabs/tab.leverantorer.html'
    }, {
      title: 'Samarbetspartners',
      url: 'app/contacts/templates/tabs/tab.samarbetspartners.html'
    }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
