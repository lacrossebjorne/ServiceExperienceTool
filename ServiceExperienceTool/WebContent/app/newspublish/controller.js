'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'newsfeedservice', function($scope, newsfeedservice) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.statusMessage = "Please write news.";
  $scope.urlTitle = "";
  $scope.urlPath = "";
  $scope.urlList = [];
  $scope.bundle = {};
  $scope.data = {};
  $scope.buttonsTemplate = "app/partials/publishbutton.html";
	  
  $scope.initData = function(data) {
	  //here initialize variables like data.urlList = []
	  //data.bundle = {} , etc.
	  console.log("initData() is not implemented");
  }

  $scope.addUrl = function(data) {
	if (newsfeedservice.addUrl(data)) {
	  console.log("url was added");
	} else {
	  console.log("url couldn't be added");
	}
  }

  $scope.removeUrl = function(data) {
    if(newsfeedservice.removeUrl(data.urlList, this.urlItem)) {
      console.log("url was removed");
    } else {
      console.log("url couldn't be removed");
    }
  }

  $scope.submit = function(data) {

    if (!newsfeedservice.validateFormInput(data,
      function(rejectedStatusMessage) {
    	data.statusMessage = rejectedStatusMessage;
      })) {
      return;
    }
    
    var bundle = { newsHeader: data.newsHeader, newsContent: data.newsContent };
    
    if (data.urlList != null && data.urlList.length > 0) {
    	bundle.urlList = JSON.stringify(data.urlList);
    }
    
    if (data.file != null) {
    	bundle.file = data.file;
    }
    
    if (data.tagData != null) {
    	bundle.tagData = JSON.stringify(data.tagData);
    }

    console.log(bundle);
    var publisher = newsfeedservice.getPublisher();
    publisher.save(bundle).$promise.then(function(result) {
      console.log("Success? " + result);
      data.statusMessage = "Successfully posted news!"
    })
    .catch(function(errorMsg) {
      data.statusMessage = "Couldn't post news!"
      console.log("Error: " + errorMsg);
    });

  }
}]);
