angular.module('app')
.factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$http', '$log', '$resource', 'app.paths'];

function newsfeedservice($http, $log, $resource, paths) {
	var service = {
		test : test,
		getMovie : getMovie,
		getPublisher : getPublisher,
		validateFormInput : validateFormInput,
		addUrl : addUrl,
		removeUrl : removeUrl
	};

	return service;

	/*
	* Service functions
	*/

	function getPublisher() {
		var saveResource = $resource(paths.api + "newsServlet", { action : '' },
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
					headers: {'Content-Type': undefined, 'Content-Transfer-Encoding': 'binary', enctype:'multipart/form-data' },
					timeout: 2000
				},
				disable: {
					method: 'POST',
					params: { action: 'disableNews', newsId: '@newsId' },
					timeout: 2000
				}
		});

		function formDataObject (data) {
			var fd = new FormData();
			angular.forEach(data, function(value, key) {
				console.log("key: " + key);
				var val = value;
				/*
				if (key != "file") {
					val = encodeURIComponent(value);
				}*/
				console.log("value: " + val);
				fd.append(key, val);
			});
			return fd;
		}

	return saveResource;
	}

	function addUrl(urlTitle, urlPath, callback) {
    var urlPattern = /https?:\/\/.+\..+\..+/;

	  if (urlPath != null && urlPath.length > 0 && urlPattern.test(urlPath)) {
			if (urlTitle == null || urlTitle.length == 0) {
				urlTitle = urlPath;
			}

			var urlItem = { title: urlTitle, path: urlPath};
			callback(urlItem);
			return true;
    } else {
			return false;
		}
  }

	function removeUrl(urlList, urlItem) {
		var index = urlList.indexOf(urlItem);
		if (index < 0) {
			return false;
		}
		var removed = urlList.splice(index, 1);
		if (removed.length > 0) {
			return true;
		} else {
			return false;
		}
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
