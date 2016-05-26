'use strict';

angular.module('sms')

.controller('SMSController', ['$scope', 'SMSService', function($scope, SMSService) {
	console.log("inside sms-controller");
	$scope.testData = "this is some testdata";
	$scope.contacts = [];
	
//	getContacts();
//	
//	function getContacts() {
//			SMSService.getContacts().$promise.then(function(result) {
//				$log.info("Successfully fetched users-list");
//				
////				for (var i = 0; i < result.length; i ++) {
////					contacts.push(result[i]);
////					var obj = contacts[i];
////					
////					console.log(obj);
////				}
//				
//			}, function(error) {
//				$log.error("Couldn't fetch users-list!");
//			});
//			
//			
////			tagResource.query().$promise.then(function(result) {
////				$log.info("Successfully fetched tags");
////				for (var i = 0; i < result.length; i++) {
////					result[i].selected = false;
////					tags.push(result[i]);
////				}
////		    return result;
////			}, function(error) {
////				$log.error("Couldn't fetch tags!");
////			});
////			return tags;
//			
//
//	}
}]);