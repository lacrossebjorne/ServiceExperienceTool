'use strict';

angular.module('newsfeed')

.directive('newsEditButtons', function() {
	return {
		restrict: 'E',
		transclude: false,
		template:
			"<button type='button' ng-click='saveNews(x, data)' class='w3-btn w3-theme-d1 w3-margin-bottom'>Save</button> " +
			"<button type='button' ng-click='toggleEdit(x)' class='w3-btn w3-theme-d1 w3-margin-bottom'>Cancel</button> " +
			"<button type='button' ng-click='deleteNews(x, statusMessage)' class='w3-btn w3-theme-d1 w3-margin-bottom'>Delete</button> XXX"
	}
});