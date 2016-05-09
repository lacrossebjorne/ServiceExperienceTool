angular.module('app')
  .factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$resource', 'app.paths'];

function newsfeedservice($resource, paths) {
  var service = {
    saveNews: saveNews
  };

  return service;

  function saveNews(header, content) {
    var saveResource = $resource(paths.api + "newsServlet", {}, {
      save: {
        method: 'POST',
        params: {
          action: 'publishNews',
          header: header,
          content: content
        },
        isArray: false
      }
    });

    return saveResource.save().$promise.then(function(result) {
        console.log("Success? " + result);
        return "Succesfully posted news!";
      })
      .catch(function(errorMsg) {
        console.log("Error: " + errorMsg);
        return "Couldn't post news!";
      });
  }
}