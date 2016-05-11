'use strict';

angular.module('company')

.controller('CompanyController', ['$scope', function($scope) {
  var imgIndex = 0;
  var imgUris = ['img/company_main_01.jpg', 'img/company_main_02.jpg'];
  var imageSwitch = function() {
    return imgUris[imgIndex++ % imgUris.length];
  }

  $scope.tabs = [{
    title: 'History',
    url: 'app/company/tab.one.html'
  }, {
    title: 'Atmosphere',
    url: 'app/company/tab.two.html'
  }, {
    title: 'Values',
    url: 'app/company/tab.three.html'
  }];

  $scope.currentTab = 'app/company/tab.one.html';

  $scope.imgA = imageSwitch();

  $scope.onClickTab = function(tab) {
    $scope.currentTab = tab.url;
    $scope.imgA = imageSwitch();
  }

  $scope.isActiveTab = function(tabUrl) {
    return tabUrl == $scope.currentTab;
  }
}]);
