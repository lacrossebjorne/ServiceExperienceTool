'use strict';

angular.module('menu')

.controller('MenuController', ['$scope', 'MenuService', 'Category', function($scope, MenuService, Category) {

  $scope.menus = MenuService.query();  
  $scope.selectedCat = 0;
  $scope.detailsVisible = false;

  
  $scope.showDetails = function() {
	  this.detailsVisible = !this.detailsVisible;
  };
  
  $scope.showCategory = function(id) {
	  $scope.selectedCat = id;
  };
  
  $scope.showMenu = function(id) {
	  $scope.selectedMenu = id;

	  $scope.categories = Category.query({menuId : id},function(result){
		  $scope.categoryGroups = groupBy(result, function(item){
			  return item.parent;
			});
	  });

	  $scope.menuItems = MenuService.query({menuId: id}, function(result){
		  $scope.itemGroups = groupBy(result, function(item){
			  return item.category;
		  });
	  });
  };
  
  $scope.categoryFilter = function(id){
	  return function(category){
		  return (id == 0 || category.id == id || category.parent == id);
	  }
  };
  
  function groupBy(array, fn){
	  var groups = {};
	  angular.forEach(array, function(item){
		  var group = JSON.stringify(fn(item));
		  groups[group] = groups[group] || [];
		  groups[group].push(item);
	  });
	  return groups;
  };
  

}]);
