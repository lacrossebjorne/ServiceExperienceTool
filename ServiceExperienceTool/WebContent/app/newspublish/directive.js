'use strict';

angular.module('newspublish')

.directive('newsPublishButton', function() {
	return {
		restrict: 'E',
		transclude: false,
		template:
			"<div class=\"w3-margin-bottom\"><button type=\"submit\" ng-click=\"submit()\" " +
			"class=\"w3-btn w3-blue-grey\" />Publish</button></div>"
	}
});