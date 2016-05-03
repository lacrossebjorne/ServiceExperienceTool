'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'NewsPublishService', function($scope, Publisher) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.customer = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent };
  $scope.statusMessage = "Please write news."
  $scope.publishNews = function() {
	  console.log('Publishing: ' + $scope.newsSubject + ", " + $scope.newsContent);
	  //alert('Publishing: ' + $scope.newsSubject + ", " + $scope.newsContent);
  };

  $scope.submit = function() {
    if (!validateFormInput()) {
      return;
    }

    $scope.customer.newsHeader = $scope.newsHeader;
    $scope.customer.newsContent = $scope.newsContent;
    //$scope.customer.file = null;
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
