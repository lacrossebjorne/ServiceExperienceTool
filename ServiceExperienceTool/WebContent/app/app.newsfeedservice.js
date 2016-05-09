angular.module('app')
  .factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$resource','app.paths'];

function newsfeedservice($resource, paths) {
  var service = {
    getNews: getNews,
    saveNews: saveNews
  };

  return service;


  function getNews() {
    var newsobject = $resource(paths.api + "newsServlet", {}, {
      get: {
        method: 'GET',
        params: {
          action: 'getNews',
          type: 'json',
          selectedPage: '1',
          resultsPerPage: '5',
          showDisabled: false
        },
        isArray: false
      }
    });

    return newsobject.get();
  }
  
  function saveNews() {
	    var newsobject = $resource(paths.api + "newsServlet", {}, {
	      get: {
	        method: 'GET',
	        params: {
	          action: 'getNews',
	          type: 'json',
	          selectedPage: '1',
	          resultsPerPage: '5',
	          showDisabled: false
	        },
	        isArray: false
	      }
	    });

	    return newsobject.get();
	  }
}
