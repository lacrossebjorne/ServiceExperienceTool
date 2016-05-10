angular.module('app')
.factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$http', '$log', '$resource', 'app.paths'];

function newsfeedservice($http, $log, $resource, paths) {
	var service = {
		saveNews : saveNews,
		test : test,
		getMovie: getMovie
	};

	return service;

	function saveNews(header, content) {
		$log.info('saveNews called');
		var saveResource = $resource(paths.local + "newsServlet", {}, {
			save : {
				method : 'POST',
				params : {
					action : 'publishNews',
					header : header,
					content : content
				},
				isArray : false
			}
		});

		return saveResource;

	}

	function getMovie(t, y, plot) {
		$log.info('getMovie called');
		var saveResource = $resource("http://www.omdbapi.com/", {}, {
			get : {
				method : 'POST',
				params : {
					t : t,
					y : y,
					plot : plot
				},
				transformRequest : [ function(data, headersGetter) {
					$log.info('Transform req');
					$log.info(data);
					$log.info(headersGetter());
				} ].concat($http.defaults.transformRequest),
				transformResponse : [ function(data, headersGetter) {
					$log.info('Transform resp');
					$log.info(data);
					$log.info(headersGetter());
				} ].concat($http.defaults.transformRequest),
				isArray : false
			}
		});

		return saveResource.get().$promise.then(function(result) {
		      $log.info("Succesfully got movie!");
		    })
		    .catch(function(errorMsg) {
		      $log.info("Couldn't get movie!");
		      $log.log("Error: ");
		      $log.error(errorMsg);
		    });

	}
    
	function test(inText) {
		return 'app.newsfeedservice: service is working.';
	}
}