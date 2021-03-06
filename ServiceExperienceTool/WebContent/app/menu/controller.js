'use strict';

angular.module('menu')

.controller('MenuController', ['$scope', 'MenuService', 'Category', 'Allergen', function($scope, MenuService, Category, Allergen) {
	
  $scope.menus = MenuService.query();  
  $scope.selectedCat = 0;
  $scope.detailsVisible = false;
  $scope.searchVisible = false;
  $scope.aDropVisible = false;
  
  //testdata
//  $scope.allergenList = [{id: '1', name: 'gluten'},{id: '2', name: 'laktos'},{id: '3', name: 'nötter'},{id: '4', name: 'ägg'}];
  //end testdata
  
  $scope.selectedAllergens = [];
  
  $scope.toggleAllergen = function(id) {
	  var index = $scope.selectedAllergens.indexOf(id);
	  if(index == -1){
		  $scope.selectedAllergens.push(id);
		  this.selected = true;
	  }else{
		  $scope.selectedAllergens.splice(index, 1);
		  this.selected = false
	  }
  };
  
  $scope.toggleSearchBar = function() {
	  $scope.searchVisible = !$scope.searchVisible;
  };
  
  $scope.toggleAllergenDropdown = function() {
	  $scope.aDropVisible = !$scope.aDropVisible;
	  console.log($scope.aDropVisible);
  };
  
  
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
	  
	  $scope.allergenList = Allergen.query({menuId : id});

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
  
  $scope.allergenFilter = function(inclusive){
	  return function(item){
		  if($scope.selectedAllergens.length == 0) return true;
		  var hit = false;
		  angular.forEach(item.allergens, function(allergen){
			  if($scope.selectedAllergens.indexOf(allergen.id) != -1){
				  hit = true;
				  return;
			  }
		  });
		  return (inclusive) ? hit : !hit;
	  }
  };
  
  $scope.allergenHighlighter = function(){
	  return function(item){
		  if($scope.selectedAllergens.length == 0){
			  item.highlighted = false;
			  return true;
		  }
		  var hit = false;
		  angular.forEach(item.allergens, function(allergen){
			  if($scope.selectedAllergens.indexOf(allergen.id) != -1){
				  hit = true;
				  return;
			  }
		  });
		  item.highlighted = hit;
		  return true;
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
