angular.module('app')
.controller('NavbarController', NavbarController);

NavbarController.$inject = ['$scope', 'pushService'];

function NavbarController($scope, pushService){
	var vm = this;
	
	vm.callback = function(event, data){
		$scope.pushes += 1;
		$scope.pushObjects.push(data);
		console.log('CALLBACK: ' + data.path + " " + data.message);
		console.log($scope.pushObjects);
	}
	
	$scope.pushes = 0;
	$scope.pushObjects = [];
	$scope.selectPush = function(index){
		$scope.pushObjects.splice(index, 1);
		$scope.pushes -= 1;
	}
	
	$scope.hideNotifications = function(){
		return $scope.pushObjects.length > 0 ? true : false;
	}
	
	pushService.listen(vm.callback);
}