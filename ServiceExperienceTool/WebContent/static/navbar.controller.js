angular.module('app')
.controller('NavbarController', NavbarController);

NavbarController.$inject = ['$scope', 'pushService'];

function NavbarController($scope, pushService){
	var vm = this;
	
	vm.callback = function(event, data){
		$scope.pushes += 1;
		console.log('CALLBACK: ' + data.path + " " + data.message);
	}
	
	$scope.pushes = 0;
	pushService.listen(vm.callback);
}