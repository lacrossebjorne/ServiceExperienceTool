'use strict';

angular.module('team')

.controller('TeamController', ['$scope', '$location', 'team.data', 'pushService', function($scope, $location, teamData, pushService) {
  var imgIndex = 0;
  var imgUris = teamData.imgUris;
  
  /**
   * Changes image on top of tabpage
   * 
   * @return  url to image
   */
  var imageSwitch = function() {
      return teamData.imgUris[imgIndex++ % imgUris.length];
  }

  $scope.documents = teamData.docs;
  $scope.tabs = teamData.tabs;
  $scope.currentTab = $scope.tabs[0].url;
  $scope.imgA = imageSwitch();
  
  /**
   * Sets tab to active.
   * Calls imageSwitch to change image.
   * 
   * @param   tab an Tabobject containing url and title.
   */
  $scope.onClickTab = function(tab) {
      $scope.currentTab = tab.url;
      $scope.imgA = imageSwitch();
  }

  /**
   * Checks if tab is active.
   * 
   * @param   {string}    tabUrl  URL to tab
   * @return          true if tab is active
   */
  $scope.isActiveTab = function(tabUrl) {
      return tabUrl == $scope.currentTab;
  }

  /**
   * Adds a document to list of documents.
   * Uses pushService broadcast function to send a push notification.
   * 
   * @param   {string}    title   displayed title for document
   * @param   {string}    url     url for document
   */
  $scope.addDocument = function(title, url) {
      $scope.documents.push({
          title: title,
          icon: 'fa-file-text-o',
          url: url
      });
      pushService.broadcast({
          path: $location.absUrl(),
          message: 'Document added: ' + title
      });
      $scope.newDocTitle = "";
      $scope.upFile = "";
  }

  $scope.removeDocument = function(index) {
      $scope.documents.splice(index, 1);
  }

  /**
   * Sets the name of uploaded file to upFileName on scope.
   * 
   * @param   element DOM element
   */
  $scope.setFile = function(element) {
      $scope.$apply(function($scope) {
          $scope.upFileName = element.files[0].name;
          console.log(element.files[0].name);
      });
  };
}]);