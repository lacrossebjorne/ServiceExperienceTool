'use strict';

angular.module('newsfeed')

.controller('NewsfeedController', ['$scope', 'NewsfetchService', 'NewsEditService', function($scope, Newsfetch, NewsEditor) {
  var minArticleTextLimit = 50;
  var initialStatusMessage = "Enter you news.";
  $scope.TEST = "Newsfetch.get";
  $scope.limit = 0;
  $scope.maxLimit = 100;
  $scope.articleTextLimit = minArticleTextLimit;
  $scope.news = [];
  $scope.selectedPage = 0;
  $scope.isEditing = false;
  $scope.statusMessage = initialStatusMessage;
  $scope.isShowingDisabledEntries = false;
  
  var tagsData = [{
	    text: '#important'
	  }, {
	    text: '#kitchen'
	  }, {
	    text: '#service'
	  }, {
	    text: '#hotel'
	  }, {
	    text: '#cleaning'
	  }];
  
  $scope.tags = [];

  expand();

  function expand() {
    $scope.limit += 5;
    $scope.selectedPage++;
    Newsfetch.get({
      action:'getNews', type: 'json', selectedPage: $scope.selectedPage, resultsPerPage: '5', showDisabled: $scope.isShowingDisabledEntries
    }, function(newsobject) {
      for (var i = 0; i < newsobject.news.length; i++) {
        newsobject.news[i].isEditing = false;
        newsobject.news[i].tagData = dummyTags();
        $scope.news.push(newsobject.news[i]);
      }
    });
  }
  
   function dummyTags(){
	  var temp = [];
	  var r = Math.floor((Math.random() * 4));
	  
	  temp.push(tagsData[r].text);
	  
	  if (Math.random() < .5){
		  temp.push(tagsData[r + 1].text);
	  }
	  
	  return temp;
  }
   
   $scope.sortImportant = function(t) {
	    return t.tagData.toString().indexOf("important") > -1 ? 1 : void(0);
	  }

  var validateFormInput = function(newsHeader, newsContent) {
    if (newsHeader != undefined && newsHeader.length >= 3 &&
      newsContent != undefined && newsContent.length >= 12) {
      return true;
    } else {
      return false;
    }
  }

  // Expands article
  $scope.expandArticle = function(textLimit) {
    textLimit == minArticleTextLimit ? this.articleTextLimit = 999 : this.articleTextLimit = minArticleTextLimit;
  };

  // Expands newsfeed
  $scope.expandList = function() {
    expand();
  };

  $scope.iconChange = function() {
    return this.articleTextLimit == minArticleTextLimit;
  };

  $scope.toggleEdit = function(news) {
    console.log(news);
    news.isEditing = !news.isEditing;
  }

  $scope.saveNews = function(news, index, statusMessage) {
    //first validate the input
    var editedHeader = angular.element(document.querySelector('#edit-header-' + index)).val();
    var editedContent = angular.element(document.querySelector('#edit-content-' + index)).val();
    var self = this;
    if (!validateFormInput(editedHeader, editedContent)) {
      this.statusMessage = "Either you typed to little or to much!";
      return;
    } else {
      //do backend call here
      NewsEditor.save({
        id: news.newsId, header: editedHeader, content: editedContent
      }).$promise.then(function(result) {
        //if backend call is successful
        news.header = editedHeader;
        news.content = editedContent;
        self.statusMessage = "Successfully updated news!";
      })
      .catch(function(errorMsg) {
        //if backend call is not successful
        self.statusMessage = "Couldn't update news!";
      });
    }
  }

  $scope.deleteNews = function(news, statusMessage) {
    var self = this;
    NewsEditor.disable({
      id: news.newsId
    }).$promise.then(function(result) {
      //if backend call is successful
      self.statusMessage = "Successfully deleted news!";
      if (!$scope.isShowingDisabledEntries) {
        //removing entry from array
        var index = $scope.news.indexOf(news);
        $scope.news.splice(index, 1);
      }
    })
    .catch(function(errorMsg) {
      //if backend call is not successful
      self.statusMessage = "Couldn't delete news!";
    });
  }

}]);
