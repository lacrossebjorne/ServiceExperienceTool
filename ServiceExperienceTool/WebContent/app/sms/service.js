'use strict';

angular.module('sms')

.factory('SMSService', ['$resource', 'app.paths',
		function($resource, paths) {
			return $resource(paths.api + "smsServlet", {}, {
				getContacts: {
					method: 'GET',
					params: {
						action: 'listUsers'
					},
					isArray: true
				}
			});
		}
]);