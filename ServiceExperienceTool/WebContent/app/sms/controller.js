'use strict';

angular.module('sms')

.controller('SMSController', ['$scope', 'SMSService', function($scope, SMSService) {
	console.log("inside sms-controller");
	$scope.testData = "this is some testdata";
	$scope.contacts = [];
	$scope.statusMessage = "välj mottagare och skicka ett meddelande";
	$scope.smsMessage = "";
	
	getContacts();
	
	function getContacts() {
			$scope.contacts = SMSService.getContacts();
			console.log($scope.contacts);
	}
	
	$scope.toggleSelected = function(contact) {
		contact.isSelected = !contact.isSelected;
	}
	
	$scope.isSelected = function(contact) {
		return contact.isSelected;
	}
	
	$scope.sendSMS = function() {
		console.log("sending SMS");
		var recipients = [];
		angular.forEach($scope.contacts, function(contact) {
			if (contact.isSelected) {
				recipients.push(contact.phoneNumber);
			}
		});
		
		console.log("number of recipients selected: " + recipients.length);
		
    SMSService.sendSMS(recipients, $scope.smsMessage, function(statusMessage) {
    	$scope.statusMessage = statusMessage;;
    });
	}
	
}]);