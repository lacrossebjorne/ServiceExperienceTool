'use strict';

angular.module('newsfeed')

.controller('NewsfeedController', ['$scope', 'NewsfetchService', 'newsfeedservice',
function($scope, Newsfetch, newsfeedservice) {
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
  $scope.bundle = {};
  $scope.testScope = { file: null};
  $scope.buttonsTemplate = "app/partials/editbuttons.html";

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

  $scope.fillForm = function(news) {
    this.data = {};
    this.data.newsId = news.newsId;
    this.data.newsHeader = news.header;
    this.data.newsContent = news.content;
    this.data.urlList = news.urlList;
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

  $scope.saveNews = function(news, data) {
    console.log("the data: " + data);
    	
    //create the bundle object
    var bundle = { newsId: data.newsId, newsHeader: data.newsHeader, newsContent: data.newsContent};

    if (data.urlList != null && data.urlList.length > 0) {
      var jsonUrlList = JSON.stringify(data.urlList);
      bundle.urlList = jsonUrlList;
    }

    if (data.file != null) {
      bundle.file = data.file;
    }

    var self = this;
    console.log("the bundle: " + bundle);

    //validate input
    if (!newsfeedservice.validateFormInput(bundle,
      function(rejectedStatusMessage) {
        data.statusMessage = rejectedStatusMessage;
      })) {
        return;
      } else {
        //do backend call here
        var publisher = newsfeedservice.getPublisher();
        //{newsId: news.newsId, newsHeader: editedHeader, newsContent: editedContent, urlList: editedUrls, file: file}
        publisher.save(bundle).$promise.then(function(result) {
          //if backend call is successful
          //change existing news information
          news.header = data.newsHeader;
          news.content = data.newsContent;
          news.urlList = data.urlList;
          data.statusMessage = "Successfully updated news!";
        })
        .catch(function(errorMsg) {
          //if backend call is not successful
          data.statusMessage = "Couldn't update news!";
        });
      }
    }

    $scope.deleteNews = function(news, data) {
      var self = this;
      var publisher = newsfeedservice.getPublisher();
      publisher.disable({
        newsId: news.newsId
      }).$promise.then(function(result) {
        //if backend call is successful
        data.statusMessage = "Successfully deleted news!";
        if (!$scope.isShowingDisabledEntries) {
          //removing entry from array
          var index = $scope.news.indexOf(news);
          $scope.news.splice(index, 1);
        }
      })
      .catch(function(errorMsg) {
        //if backend call is not successful
        data.statusMessage = "Couldn't delete news!";
      });
    }

  }]);
