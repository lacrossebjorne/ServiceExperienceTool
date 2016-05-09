'use strict';

angular.module('menu')

.controller('MenuController', ['$scope', 'MenuService', 'Category', function($scope, MenuService, Category) {
  	
	/*TODO
	 * Make details hidden on default, add visibilit switch
	 * Split categories into main and sub categories
	 */
  $scope.menus = MenuService.query();  
  $scope.selectedCat = 0;

  // Show/Hide details
  $scope.showDetails = function() {
	  //TODO implement
  };
  
  $scope.showCategory = function(id) {
	  $scope.selectedCat = id;
  };
  $scope.showMenu = function(id) {
	  $scope.selectedMenu = id;
	  $scope.categories = Category.query({menuId : id});
	  $scope.menuItems = MenuService.query({
		    menuId: id,
	  });
  };

}]);
