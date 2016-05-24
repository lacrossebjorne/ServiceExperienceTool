'use strict';

angular.module('company', [
  'ngRoute',
  'ngResource'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/company', {
    templateUrl: 'app/company/company.html',
    controller: 'CompanyController'
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
      title: 'Vision & affärsidé',
      url: 'app/company/tabs/tab.vision-affarside.html'
    }, {
      title: 'Historia',
      url: 'app/company/tabs/tab.historia.html'
    }, {
      title: 'Miljö & socialt ansvar',
      url: 'app/company/tabs/tab.miljo-socialt-ansvar.html'
    }, {
      title: 'Innan din första arbetsdag!',
      url: 'app/company/tabs/tab.forsta-arbetsdag.html'
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
      'img/company_main_01.jpg',
      'img/company_main_02.jpg'
    ]
  });
});
