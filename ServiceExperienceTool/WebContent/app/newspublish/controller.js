'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'newsfeedservice', function($scope, newsfeedservice) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.customer = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent };
  $scope.statusMessage = "Please write news.";
  $scope.urlName = "";
  $scope.urlPath = "";
  $scope.urlList = [];

  $scope.addUrl = function() {
	  if ($scope.urlPath == null || !$scope.publishForm.urlField.$valid || $scope.urlPath.length == 0) {
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

    if (!newsfeedservice.validateFormInput({ newsHeader: $scope.newsHeader,
        newsContent: $scope.newsContent},
        function(rejectedStatusMessage) {
          $scope.statusMessage = rejectedStatusMessage;
        })) {
      return;
    }

    $scope.customer.newsHeader = $scope.newsHeader;
    $scope.customer.newsContent = $scope.newsContent;

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
}]);
