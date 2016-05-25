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
  $provide.value('company.data', {
    links: [{
      title: 'Google',
      url: 'https://www.google.com'
    }, {
      title: 'Yahoo',
      url: 'https://www.yahoo.com'
    }, {
      title: 'Aftonbladet',
      url: 'https://www.aftonbladet.com'
    }, {
      title: 'Expressen',
      url: 'https://www.expressen.com'
    }],

    tabs: [{
      title: 'Logi',
      url: 'app/other-products/templates/tabs/tab.vision-affarside.html'
    }, {
      title: 'Paket',
      url: 'app/other-products/templates/tabs/tab.historia.html'
    }, {
      title: 'Aktiviteter',
      url: 'app/other-products/templates/tabs/tab.miljo-socialt-ansvar.html'
    }, {
      title: 'Special',
      url: 'app/other-products/templates/tabs/tab.forsta-arbetsdag.html'
    }],

    docs: [{
      title: 'Grossister',
      icon: 'fa-file-pdf-o',
      url: 'suppliers.pdf'
    }, {
      title: 'Budget Analys 2015-2016',
      icon: 'fa-file-excel-o',
      url: 'budget.svc'
    }, {
      title: 'Städguide',
      icon: 'fa-file-pdf-o',
      url: 'cleaning-for-dummies.pdf'
    }, {
      title: 'Recept: test',
      icon: 'fa-file-word-o',
      url: 'macRecipe.doc'
    }, {
      title: 'Mall: företagsbrev',
      icon: 'fa-file-word-o',
      url: 'letterTemplate.doc'
    }, {
      title: 'Manual för medarbetare',
      icon: 'fa-file-text-o',
      url: 'manual.txt'
    }],

    imgUris: [
      'img/set_mock-img_01.jpg',
      'img/set_mock-img_02.jpg',
      'img/set_mock-img_03.jpg',
      'img/set_mock-img_04.jpg'
    ]
  });
});
