'use strict';

angular.module('newspublish')

.controller('NewspublishController', ['$scope', 'multipartForm', function($scope, multipartForm) {
  $scope.newsHeader = "";
  $scope.newsContent = "";
  $scope.customer = {};
  $scope.publishNews = function() {
	  console.log('Publishing: ' + $scope.newsSubject + ", " + $scope.newsContent);
	  //alert('Publishing: ' + $scope.newsSubject + ", " + $scope.newsContent);
  };

  $scope.submit = function() {
    var uploadUrl = 'http://78.68.50.137:8080/ServiceExperienceTool/newsServlet?action=publishNews';
    uploadUrl += "&newsHeader=" + $scope.newsHeader;
    uploadUrl += "&newsContent=" + $scope.newsContent;
    console.log($scope.customer);
    console.log(uploadUrl);
    multipartForm.post(uploadUrl, $scope.customer);
  }
}]);
