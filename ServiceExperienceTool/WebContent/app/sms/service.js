'use strict';

angular.module('sms')

'use strict';

angular.module('sms')

.factory('SMSService', ['$resource', 'app.paths', 'AdminFactory',
function($resource, paths, AdminFactory) {
  return {
    getContacts : function() {
      var arr = [];
      AdminFactory.listAllUsers().$promise.then(function(data) {
        angular.forEach(data.userList, function(user) {
        	if (checkNumber(user.phoneNumber)) {
        		user.isSelected = false;
        		arr.push(user);
        	}
        });
      });
      return arr;
    }, checkCredits : function(callback) {
    	var resource = $resource(paths.api + "smsServlet", { action: 'checkCredits'}, { get: { timeout: 2000}});
    	resource.get().$promise.then(function(data) {
    		if (data.isValid) {
    			callback(data.creditsLeft);
    		} else {
    			callback("Okänt svar från servern");
    		}
    	}, function(error) {
    			callback("Kunde inte nå servern");
    	});
    },
    sendSMS : function(smsWrapper, callback) {
    	var resource = $resource(paths.api + "smsServlet", {}, {
    	  sendSMS: {
    	  	method: 'POST',
    	  	params: {
    	  		action: 'sendSMS',
    	  		smsWrapper: '@smsWrapper'
    	  	},
    	  	timeout : 2000
    	  }
    	});
    	
    	//check if there are any recipients
    	//and if the message is of the right length
    	// Staffan: added return statements in else-if
    	//to block obsolete call to server
    	if (smsWrapper.recipients.length == 0) {
    		callback({statusMessage: "Inga mottagare valda!"});
    		return;
    	} else if (smsWrapper.message.length > 160) {
    		callback({statusMessage: "För många tecken!"})
    		return;
    	} else if (smsWrapper.message.length == 0) {
    		callback({statusMessage: "För få tecken!"});
    		return;
    	}
    	
  		//if sending an array with all recipients instead, then backend can handle
  		//all recipients at once. Right now it's only 1 recipient at a time
    	//also need to implement a check to see how many credits are left
    	
    	resource.sendSMS({ 'smsWrapper': smsWrapper }).$promise.then(function(data) {
    		console.log("promise success");
    		
    		console.log("data.isValid: " + (data.isValid));
    		
    		if (data.isValid) {
    			console.log("data.statusCode: " + (data.statusCode));
    			
    			switch(data.statusCode) {
    			case 'SUCCESSFUL':
    				data.statusMessage = "SMS har skickats till alla mottagare!";
    				break;
    			case 'COMPLETED_WITH_ERRORS':
    				data.statusMessage = "Fel har uppkommit, SMS har inte skickats till alla!";
    				break;
    			case 'NOT_ENOUGH_CREDITS':
    				data.statusMessage = "För få krediter för att skicka till alla mottagare!";
    				break;
    			case 'UNSUCCESSFUL':
    				data.statusMessage = "Kunde inte skicka SMS!";
    				break;
    			}
    			
    			callback(data);
    		}
    		
    	}, function(error) {
    		console.log("error");
    		callback({statusMessage: "Kunde ej få svar från servern, SMS har inte skickats!", isValid: false});
    	});
    }
  }
  
  /**
   * Select users with a valid number
   */
  function checkNumber(number) {
  	
  	//Swedish cellphone-number pattern
  	//begin with 0 or +46, have following tre digits in beginning: 070 072 073 076 079 
  	//examples how numbers can be written: 
  	//+46761234567, 076-1234567, 076 1234567, 076 123 45 67, 076-123 45 67
  	var patt = /^(\+46|0)(7[02369])(-?|\s)\d{3}\s?\d{2}\s?\d{2}$/; 
  	var matches = patt.test(number);
  	
  	var result = "";
  	
  	if (matches) {
  		result = "it matches";
  	} else {
  		result = "it doesn't match";
  	}
  	
  	return matches;
  }
  
} ]);
