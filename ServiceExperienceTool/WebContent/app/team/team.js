'use strict';

angular.module('team', [ 'ngRoute', 'ngResource' ])

.config([ '$routeProvider', function($routeProvider) {
  $routeProvider.when('/team', {
    templateUrl : 'app/team/team.html',
    controller : 'TeamController'
  });
} ])

.config(function($provide) {
  $provide.value('team.data', {
    tabs : [ {
      title : 'Målsättningar',
      url : 'app/team/templates/tabs/tab.malsattningar.html'
    }, {
      title : 'Kontaktlista',
      url : 'app/team/templates/tabs/tab.kontaktlista.html'
    }, {
      title : 'Ansvarsområden',
      url : 'app/team/templates/tabs/tab.ansvarsomraden.html'
    }, {
      title : 'Rutiner & Körscheman',
      url : 'app/team/templates/tabs/tab.rutiner-korscheman.html'
    }, {
      title : 'Utbildningar',
      url : 'app/team/templates/tabs/tab.utbildningar.html'
    }, {
      title : 'Ordbok',
      url : 'app/team/templates/tabs/tab.ordbok.html'
    } ],

    docs : [ {
      title : 'Checklista Kök',
      icon : 'fa-file-pdf-o',
      url : 'checklist_kitchen.pdf'
    }, {
      title : 'Checklista Städ',
      icon : 'fa-file-excel-o',
      url : 'checklist.svc'
    }, {
      title : 'Rutiner',
      icon : 'fa-file-pdf-o',
      url : 'routines.pdf'
    } ],

    links : [ {
      title : 'Google',
      url : 'https://www.google.com'
    }, {
      title : 'Yahoo',
      url : 'https://www.yahoo.com'
    }, {
      title : 'Aftonbladet',
      url : 'https://www.aftonbladet.com'
    }, {
      title : 'Expressen',
      url : 'https://www.expressen.com'
    } ],

    imgUris : [ 'img/set_mock-img_08.jpg', 'img/set_mock-img_09.jpg', 'img/set_mock-img_10.jpg', 'img/set_mock-img_11.jpg' ]
  });
});
