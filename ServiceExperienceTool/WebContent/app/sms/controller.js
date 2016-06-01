'use strict';

angular.module('sms')

.controller('SMSController', ['$scope', 'SMSService', function($scope, SMSService) {
	console.log("inside sms-controller");
	var smsWrapper = {}; // Holds all data needed for sms-service
	
	$scope.testData = "this is some testdata";
	$scope.contacts = [];
	$scope.statusMessage = "välj mottagare och skicka ett meddelande";
	$scope.smsMessage = "";
	$scope.creditsLeft = "okänt"; 
	$scope.isErrorSending = false;
	$scope.isShowingErrorMessage = false;
	$scope.errorLog = [];
	
	getContacts();
	checkCredits();
	
	function checkCredits() {
		SMSService.checkCredits(function(param) {
			$scope.creditsLeft = param;
		});
	}
	
	function getContacts() {
			$scope.contacts = SMSService.getContacts();
			console.log($scope.contacts);
	}
	
	function sendSmsCallback(data) {
		$scope.statusMessage = data.statusMessage;
		checkCredits();
		
		if (data.isValid) {
			console.log("data.errors.undefined: " + (data.errors == undefined));
			console.log("data.errors.null: " + (data.errors == null));
			
			if (data.errors != undefined) {
				$scope.isErrorSending = true;
				
				var someString = "";
				$scope.errorLog.length = 0;
				
				$scope.errorLog.push("Status-code: " + data.statusCode);
				$scope.errorLog.push("Sent " + data.successfulSendAttempts + " of " + data.totalSendAttempts + " messages.");
				angular.forEach(data.errors, function(data) {
					$scope.errorLog.push("recipient: " + data.recipient + ", error: " + data.error);
				});
				
			} else {
				$scope.isErrorSending = false;
			}
			
			console.log("totalSendAttempts: " + (data.totalSendAttempts));
			console.log("successfulSendAttempts: " + (data.successfulSendAttempts));
			console.log("isValid: " + (data.isValid));
			console.log("errors: ");
			console.log("statusCode: " + (data.statusCode));
		}
	}
	
	$scope.toggleSelected = function(contact) {
		contact.isSelected = !contact.isSelected;
	}
	
	$scope.isSelected = function(contact) {
		return contact.isSelected;
	}
	
	$scope.sendSMS = function() {
		$scope.statusMessage = "Skickar...";
		console.log("sending SMS");
		var recipients = [];
		angular.forEach($scope.contacts, function(contact) {
			if (contact.isSelected) {
				recipients.push(contact.phoneNumber);
			}
		});
		
		console.log("number of recipients selected: " + recipients.length);
		smsWrapper.recipients = recipients;
		smsWrapper.message = $scope.smsMessage;
		console.log(JSON.stringify(smsWrapper));
		
		SMSService.sendSMS(smsWrapper, sendSmsCallback);
	}
	
	$scope.toggleErrorModal = function() {
		$scope.isShowingErrorMessage = !$scope.isShowingErrorMessage;
	}
	
}]);