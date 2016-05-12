'use strict';

angular.module('company')

.controller('CompanyController', ['$scope', 'company.data', function($scope, companyData) {
  var imgIndex = 0;
  var imgUris = companyData.imgUris;
  var imageSwitch = function() {
    return imgUris[imgIndex++ % imgUris.length];
  }

  $scope.links = companyData.links;
  $scope.documents = companyData.docs;
  $scope.tabs = companyData.tabs;
  $scope.currentTab = 'app/company/tabs/tab.history.html';
  $scope.imgA = imageSwitch();

  $scope.onClickTab = function(tab) {
    $scope.currentTab = tab.url;
    $scope.imgA = imageSwitch();
  }

  $scope.isActiveTab = function(tabUrl) {
    return tabUrl == $scope.currentTab;
  }

  $scope.addLink = function(title, url){
      $scope.links.push({title, url});
      $scope.newLinkTitle = "";
      $scope.newLinkUrl = "";
  }

  $scope.addDocument = function(title, url){
      $scope.documents.push({title, icon:'fa-file-text-o', url});
      $scope.newDocTitle = "";
      $scope.newDocUrl = "";
  }

  $scope.setFile = function(element) {
    $scope.$apply(function($scope) {
        $scope.upFile = element.files[0].name;
        console.log('FileNAME');
        console.log(element.files[0].name);
    });
};
}]);
