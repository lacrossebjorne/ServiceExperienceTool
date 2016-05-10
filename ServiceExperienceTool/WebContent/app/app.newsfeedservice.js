angular.module('app')
.factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$http', '$log', '$resource', 'app.paths'];

function newsfeedservice($http, $log, $resource, paths) {
	var service = {
		saveNews : saveNews,
		test : test,
		getMovie : getMovie,
		getPublisher : getPublisher,
		validateFormInput : validateFormInput
	};

	return service;

	/*
	* Service functions
	*/

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

	function getPublisher() {
		var saveResource = $resource(paths.api + "newsServlet", { action : 'publishNews' },
			{
				save: { method: 'POST',
					params: { action : 'publishNews' },
					transformRequest: formDataObject,
					headers: {'Content-Type': undefined, enctype:'multipart/form-data' },
					timeout: 2000 },
				update: {
					method: 'POST',
					params: { action: 'updateNews' },
					transformRequest: formDataObject,
					timeout: 2000
				},
				disable: {
					method: 'POST',
					params: { action: 'disableNews' },
					transformRequest: formDataObject,
					timeout: 2000
				}
		});

		function formDataObject (data) {
			var fd = new FormData();
			angular.forEach(data, function(value, key) {
				console.log("key: " + key);
				console.log("value: " + value);
				fd.append(key, value);
			});
			return fd;
		}

	return saveResource;
	}

	/* not tested */
	function validateUrlInput(urlPath, urlField, urlName) {
		if (urlPath == null || urlField.$valid || urlPath.length == 0) {
			return false;
		}
		var urlName = urlName.length > 0 ? urlName : urlPath;
		var urlItem = { name: urlName, url: urlPath };
		return urlItem;
	}

	function validateFormInput(data, callbackMessage) {
		if (data.newsHeader != null && data.newsContent != null &&
			data.newsHeader.length >= 3 && data.newsContent.length >= 12) {
				return true;
		} else {
			callbackMessage("Either you typed to little or to much!");
			return false;
		}
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
		}, function(error) {
			$log.info("Couldn't get movie!");
			$log.log("Error: ");
			$log.error(error);
		});

	}

	function test(inText) {
		return 'app.newsfeedservice: service is working.';
	}
}
