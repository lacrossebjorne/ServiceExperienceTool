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
	
	$scope.pushObjects = [];
	$scope.pushes = 0;
	pushService.listen(vm.callback);
}