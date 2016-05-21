'use strict';

angular.module('newsfeed')

.directive('tagDisplay', function() {
	var link = function(scope, element, attrs) {

		init();

		function init() {
			scope.$watch('tags', function() {
				update();
			}, true);
		}

		function update() {
			scope.tagNames = "";
			(scope.tags || []).forEach(function(tag) {
				if (scope.tagNames.length > 0)
				scope.tagNames += ", ";
				scope.tagNames += tag.text;
			});
		}
	},
	template = '<strong>{{title}}</strong><br>{{tagNames}}</div></br><em><ng-transclude></ng-transclude></em>';

	return {
		restrict: 'E',
		transclude: true,
		scope: {
			title: '@',
			tags: '=tags'
		},
		link: link,
		template: template
	}
});
