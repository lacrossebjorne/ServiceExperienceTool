(function () {

    var injectParams = ['$q', 'AdminFactory'];

    var usernameValidationDirective = function ($q, AdminFactory) {

        var link = function (scope, element, attrs, ngModel) {
            ngModel.$asyncValidators.unique = function (modelValue, viewValue) {
                var deferred = $q.defer(),
                    currentValue = modelValue || viewValue
                    key = attrs.usernameValidationKey;/*,
                    property = attrs.usernameValidationProperty;*/

                //First time the asyncValidators function is loaded the
                //key won't be set  so ensure that we have 
                //key and propertyName before checking with the server 
                if (key && property) {
                    AdminFactory.existUsername({existUsername : currentValue})
                    .then(function (data) {
                        if (data.exists) {
                            deferred.resolve(); //It's unique
                        }
                        else {
                            deferred.reject(); //Add unique to $errors
                        }
                    });
                    return deferred.promise;
                }
                else {
                    return $q.when(true);
                }
            };
        };

        return {
            restrict: 'A',
            require: 'ngModel',
            link: link
        };
    };

    usernameValidationDirective.$inject = injectParams;

    angular.module('useradmin').directive('usernameValidation', usernameValidationDirective);

}());