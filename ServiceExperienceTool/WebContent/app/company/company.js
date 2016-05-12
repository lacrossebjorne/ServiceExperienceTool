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
      title: 'History',
      url: 'app/company/tabs/tab.history.html'
    }, {
      title: 'Atmosphere',
      url: 'app/company/tabs/tab.atmosphere.html'
    }, {
      title: 'Values',
      url: 'app/company/tabs/tab.values.html'
    }, {
      title: 'Links',
      url: 'app/company/tabs/tab.links.html'
    }, {
      title: 'Documents',
      url: 'app/company/tabs/tab.documents.html'
    }],

    docs: [{
      title: 'Suppliers',
      icon: 'fa-file-pdf-o',
      url: 'suppliers.pdf'
    }, {
      title: 'Budget Analysis 2015-2016',
      icon: 'fa-file-excel-o',
      url: 'budget.svc'
    }, {
      title: 'How to: Clean the Bathroom',
      icon: 'fa-file-pdf-o',
      url: 'cleaning-for-dummies.pdf'
    }, {
      title: 'Recipe: Mac & Cheeze',
      icon: 'fa-file-word-o',
      url: 'macRecipe.doc'
    }, {
      title: 'Company Letter Template',
      icon: 'fa-file-word-o',
      url: 'letterTemplate.doc'
    }, {
      title: 'Co-workers Manual',
      icon: 'fa-file-text-o',
      url: 'manual.txt'
    }],

    imgUris: [
      'img/company_main_01.jpg',
      'img/company_main_02.jpg'
    ]
  });
});
