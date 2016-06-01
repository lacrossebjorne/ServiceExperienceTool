'use strict';

angular.module('useradmin')

.directive('usernameValidation', [ 'AdminFactory', '$q', function(AdminFactory, $q, $timeout) {
	return {
		require : 'ngModel',
		//scope:{ onChangeCallback: '&onChange' },
		link : function(scope, element, attrs, ctrl) {
			
			/*if(angular.isFunction(scope.onChangeCallback)){
		          //Adding the function to the watched Array
		          ctrl.$viewChangeListeners.push(scope.onChangeCallback);
			}*/
			
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