angular.module('app')
    .service('pushService', pushService);

pushService.$inject = ['$rootScope'];

function pushService($rootScope) {
	this.broadcast = function(message) {
		$rootScope.$broadcast("pushEvent", message);
	}
	this.listen = function(callback) {
		$rootScope.$on("pushEvent", callback);
	}
}