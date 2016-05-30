angular.module('app')
    .service('pushService', pushService);

pushService.$inject = ['$rootScope'];

/**
 * Emits events to be used as pushservice from other modules.
 * 
 * @param $rootScope
 */
function pushService($rootScope) {
	
	/**
	 * Emits event, making the event exclusive for rootscope.
	 * 
	 * @param	message	text to display
	 */
	this.broadcast = function(message) {
		$rootScope.$emit("pushEvent", message);
	}
	
	/**
	 * Listens for emitted events.
	 * 
	 * @param	callback	function to perform as callback
	 */
	this.listen = function(callback) {
		$rootScope.$on("pushEvent", callback);
	}
}