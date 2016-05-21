angular.module('app')
.factory('newsfeedservice', newsfeedservice);

newsfeedservice.$inject = ['$http', '$log', '$resource', 'app.paths'];

function newsfeedservice($http, $log, $resource, paths) {
	var service = {
		test : test,
		getMovie : getMovie,
		getPublisher : getPublisher,
		addUrl : addUrl,
		removeUrl : removeUrl,
		validateFormInput : validateFormInput,
		dateAsString : dateAsString,
		dateAsDate: dateAsDate,
		dateAsMillis: dateAsMillis
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
				console.log("value: " + val);
				fd.append(key, val);
			});
			return fd;
		}

	return saveResource;
	}

	function addUrl(data) {
		var urlPattern = /https?:\/\/.+\..+\..+/;

		if (data.urlPath != null && data.urlPath.length > 0 && urlPattern.test(data.urlPath)) {
			if (data.urlTitle == null || data.urlTitle.length == 0) {
				data.urlTitle = data.urlPath;
			}

			var urlItem = { title: data.urlTitle, path: data.urlPath};
			if (data.urlList == null) {
				data.urlList = [];
			}
			data.urlList.push(urlItem);
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
	
	/**
	 * return date as string with the format yyyy-MM-dd
	 * @param days Difference from todays date
	 */
	function dateAsString(days) {
		var date = dateAsDate(days);
		if (date == undefined) 
			return;

		var fullYear = date.getFullYear();
		var monthNum = date.getMonth() + 1;
		var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
		var month = date.getMonth() < 9 ? "0" + monthNum : monthNum; //month is zero based
		var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
		
		var dateFormatted = fullYear + "-" + month + "-" + day;
		var timeFormatted = " at " + hours + ":" + minutes + ":" + seconds // not including this part for now
		return dateFormatted;
	}
	
	/**
	 * return date as Date.
	 * @param days Difference from todays date
	 */
	function dateAsDate(days) {
		var millis = dateAsMillis(days);
		if (millis == undefined) 
			return undefined;
		return new Date(millis);
	}
	
	/**
	 * return date as milliseconds.
	 * @param days Difference from todays date
	 */
	function dateAsMillis(days) {
		if (isNaN(days)) 
			return undefined;
		return Date.now() + 1000 * 60 * 60 * 24 * days;
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
