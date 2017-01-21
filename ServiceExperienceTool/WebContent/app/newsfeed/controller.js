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
  $scope.selectedPage = 1;
  $scope.isEditing = false;
  $scope.statusMessage = initialStatusMessage;
  $scope.isShowingDisabledEntries = false;
  $scope.isShowingImportantEntries = true;
  $scope.bundle = {};
  $scope.testScope = { file: null};
  $scope.buttonsTemplate = "app/partials/editbuttons.html";
  
  $scope.selectedTags = [];
  $scope.availableTags = [];

  expand();
  getActiveNewsTags();
  
  function expand() {
    if (!$scope.isShowingImportantEntries) {
      $scope.limit += 5;
      $scope.selectedPage++;
    }
    //	  $scope.limit += 5;
    //      $scope.selectedPage++;
    fetchNews();
  }
  
  function getActiveNewsTags() {
  	$scope.availableTags = newsfeedservice.getActiveNewsTags();
  }

  function fetchNews() {
    Newsfetch.get({
      action:'getNews',
      type: 'json',
      selectedPage: $scope.selectedPage,
      resultsPerPage: '5',
      showDisabled: $scope.isShowingDisabledEntries,
      selectImportant: $scope.isShowingImportantEntries,
      tags: JSON.stringify($scope.selectedTags)
    }, function(newsobject) {
      for (var i = 0; i < newsobject.news.length; i++) {
        newsobject.news[i].isEditing = false;
        $scope.news.push(newsobject.news[i]);
        var date = new Date(newsobject.news[i].createdAt);
      }
      if ($scope.isShowingImportantEntries) {
        //turn $scope.isShowingImportantEntries to false
        $scope.isShowingImportantEntries = false;
        $scope.limit = $scope.news.length;
        //call expand, since more news-entries should be fetched
        expand();
      }
    });
  }

  $scope.tagClicked = function(tag) {
    tag.selected = !tag.selected;

    if (tag.selected) {
      $scope.selectedTags.push(tag);
    } else {
      var index = $scope.selectedTags.indexOf(tag);
      $scope.selectedTags.splice(index, 1);
    }

    //set variables to initial values and empty the $scope.news-array
    $scope.limit = 0;
    $scope.selectedPage = 0;
    $scope.news = []; //or $scope.news.length = 0;
    //get news by calling expand()
    expand();
  }

  $scope.sortImportant = function(t) {
    var isImportant = false;
    (t.tagData || []).forEach(function(tag) {
      if (tag.text.match(/important/i)) {
        isImportant = true;
      }
    });
    return isImportant ? 1 : void(0);
  };

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
  
  $scope.formatISODate = function(days) {
    return newsfeedservice.formatISODate(days);
  }

  $scope.dateAsString = function(days) {
    return newsfeedservice.dateAsString(days);
  }

  $scope.fillForm = function(news) {
    this.data = {};
    this.data.newsId = news.newsId;
    this.data.newsHeader = news.header;
    this.data.newsContent = news.content;
    this.data.urlList = news.urlList;
    this.data.tagData = news.tagData;
    var daysImportant = getDaysImportant(news.importantUntil);
    if (daysImportant > 0) {
      this.data.daysImportant = daysImportant;
    }
  }

  function getDaysImportant(date) {
    if (date == null) {
      return 0;
    }
    var futureDate = new Date(date);
    var millisLeft = futureDate.getTime() - Date.now();
    var daysLeft = Math.ceil(millisLeft / 1000 / 60 / 60 / 24);
    if (daysLeft > 0) {
      return daysLeft;
    } else {
      return 0;
    }
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

    if (data.tagData != null) {
      bundle.tagData = JSON.stringify(data.tagData);
    }

    if (data.daysImportant != null && !isNaN(data.daysImportant)) {
      var date = newsfeedservice.dateAsDate(data.daysImportant);
      bundle.importantUntil = date.toISOString();
    }

    var self = this;
    console.log("the bundle: " + bundle);

    //validate input
    if (!newsfeedservice.validateFormInput(bundle,
      function(rejectedStatusMessage) {
        data.statusMessage = rejectedStatusMessage;
      })) {
        return;
      }
      else {
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
