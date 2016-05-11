'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'newsfeedservice', function($scope, newsfeedservice) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.statusMessage = "Please write news.";
  $scope.urlTitle = "";
  $scope.urlPath = "";
  $scope.urlList = [];
  $scope.bundle = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent, urlList: null };

  $scope.addUrl = function() {
    if (newsfeedservice.addUrl($scope.urlTitle, $scope.urlPath, function(urlItem) { $scope.urlList.push(urlItem); })) {
      console.log("url was added");
    } else {
      console.log("url couldn't be added");
    }
  }

  $scope.removeUrl = function() {
    if(newsfeedservice.removeUrl($scope.urlList, this.urlItem)) {
      console.log("url was removed");
    } else {
      console.log("url couldn't be removed");
    }
  }

  $scope.submit = function() {

    var formData = { newsHeader: $scope.newsHeader, newsContent: $scope.newsContent};
    if (!newsfeedservice.validateFormInput(formData,
      function(rejectedStatusMessage) {
        $scope.statusMessage = rejectedStatusMessage;
      })) {
      return;
    }

    $scope.bundle.newsHeader = $scope.newsHeader;
    $scope.bundle.newsContent = $scope.newsContent;
    $scope.bundle.urlList = JSON.stringify($scope.urlList);

    console.log($scope.bundle);
    var publisher = newsfeedservice.getPublisher();
    publisher.save($scope.bundle).$promise.then(function(result) {
      console.log("Success? " + result);
      $scope.statusMessage = "Successfully posted news!"
    })
    .catch(function(errorMsg) {
      $scope.statusMessage = "Couldn't post news!"
      console.log("Error: " + errorMsg);
    });

  }
}]);
