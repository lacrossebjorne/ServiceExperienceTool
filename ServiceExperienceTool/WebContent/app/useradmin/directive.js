/*
 * @author Björn Dalberg
 * 
 * Custom directive for useradmin module extends ngModelController
 * Checks if an username already exists
 * When editing a user the directive uses the usernameValidationKey
 * to exclude the existing username.
 * Uses AdminFactory for lookup usernames in the system
 * 
 */

'use strict';

angular.module('useradmin')

.directive('usernameValidation', [ 'AdminFactory', '$q', function(AdminFactory, $q) {
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, ctrl) {
			
			ctrl.$asyncValidators.unique = function(modelValue, viewValue) {
				
				var key = attrs.usernameValidationKey;			
				
				var deferred = $q.defer();
				
				if(key === modelValue)
					deferred.resolve();
				else {
					AdminFactory.existUsername({existUsername : modelValue})
					.$promise.then(function(data) {
						if(data.exists)
							deferred.reject();
						else
							deferred.resolve();
					});
				}
				return deferred.promise;
			}
		}
	};
} ]);