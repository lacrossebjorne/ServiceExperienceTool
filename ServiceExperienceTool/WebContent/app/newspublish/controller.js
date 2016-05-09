'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'NewsPublishService', function($scope, Publisher) {
  $scope.newsHeader = "this is a newsheader";
  $scope.newsContent = "this is some newscontent";
  $scope.statusMessage = "Please write news.";
  $scope.urlTitle = "";
  $scope.urlPath = "";
  $scope.urlList = [];
  $scope.urlList.push({title: "Google, nice webpage!", path: "http://www.google.com"});
  $scope.urlList.push({title: "Altavista!", path: "http://www.altavista.com"});
  $scope.urlList.push({title: "Göögle, googles cousin!", path: "http://www.google.com"});
  $scope.bundle = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent, urlList: null };

  $scope.addUrl = function() {
	if ($scope.urlPath == null || !$scope.publishForm.urlPath.$valid || $scope.urlPath.length == 0) {
      return;
    }

    $scope.urlList.push({title: $scope.urlTitle.length > 0 ? $scope.urlTitle : $scope.urlPath, path: $scope.urlPath});
  }

  $scope.removeUrl = function() {
    var index = $scope.urlList.indexOf(this.urlItem);
    $scope.urlList.splice(index, 1);
  }

  $scope.submit = function() {
    if (!validateFormInput()) {
      return;
    }

    $scope.bundle.newsHeader = $scope.newsHeader;
    $scope.bundle.newsContent = $scope.newsContent;
    $scope.bundle.urlList = JSON.stringify($scope.urlList);
    //$scope.bundle.urlList = $scope.urlList;

    console.log($scope.bundle);
    Publisher.save($scope.bundle).$promise.then(function(result) {
      console.log("Success? " + result);
      $scope.statusMessage = "Succesfully posted news!"
    })
    .catch(function(errorMsg) {
      $scope.statusMessage = "Couldn't post news!"
      console.log("Error: " + errorMsg);
    });
  }

  var validateFormInput = function() {
    console.log($scope.newsHeader);
    console.log($scope.newsContent);

    if ($scope.newsHeader != undefined && $scope.newsHeader.length >= 3 &&
      $scope.newsContent != null && $scope.newsContent.length >= 12) {
      return true;
    } else {
      $scope.statusMessage = "Either you typed to little or to much!";
      return false;
    }
  }
}]);
