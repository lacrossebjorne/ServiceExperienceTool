'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'NewsPublishService', function($scope, Publisher) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.customer = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent };
  $scope.statusMessage = "Please write news.";
  $scope.urlName = "";
  $scope.urlPath = "";
  $scope.urlList = [];

  $scope.publishNews = function() {
	  console.log('Publishing: ' + $scope.newsSubject + ", " + $scope.newsContent);
  };

  $scope.addUrl = function() {
    if (!$scope.publishForm.urlField.$valid && $scope.urlPath.length > 0) {
      return;
    }
    var urlName = $scope.urlName.length > 0 ? $scope.urlName : "no title";
    $scope.urlList.push({name: $scope.urlName.length > 0 ? $scope.urlName : "no title", url: $scope.urlPath});
  }

  $scope.removeUrl = function() {
    var index = $scope.urlList.indexOf(this.urlItem);
    $scope.urlList.splice(index, 1);
  }

  $scope.submit = function() {
    if (!validateFormInput()) {
      return;
    }

    $scope.customer.newsHeader = $scope.newsHeader;
    $scope.customer.newsContent = $scope.newsContent;

    console.log($scope.customer);
    Publisher.save($scope.customer).$promise.then(function(result) {
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
