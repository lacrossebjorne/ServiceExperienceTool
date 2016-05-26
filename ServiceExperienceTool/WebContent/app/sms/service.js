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
    }, sendSMS : function(recipients, message, callback) {
    	var resource = $resource(paths.api + "smsServlet", {}, {
    	  sendSMS: {
    	  	method: 'POST',
    	  	params: {
    	  		action: 'sendSMS',
    	  		recipient: '@recipient',
    	  		message: '@message'
    	  	}
    	  }
    	});
    	
    	//check if there are any recipients
    	//and if the message is of the right length
    	if (recipients.length == 0) {
    		callback("Inga mottagare valda!");
    		return;
    	} else if (message.length > 160) {
    		callback("För många tecken!")
    	} else if (message.length == 0) {
    		callback("För få tecken!");
    	}
    	
  		//if sending an array with all recipients instead, then backend can handle
  		//all recipients at once. Right now it's only 1 recipient at a time
    	//also need to implement a check to see how many credits are left
  		for (var i = 0; i < recipients.length; i++) {
  			var recipient = recipients[i];
  			resource.sendSMS({ 'recipient': recipient, 'message': message});
  		}
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
