(function () {
    'use strict';
    angular.module('myApp').factory('WebSocketService', function($q, urls, OAuthToken, $rootScope, $timeout) { 
    	var URL = urls.baseUrl + '/websocket';
    	var counter = 0;
        var self = this;
        var subscribers = {};
        var connected = $q.defer();
        
    	var methods = {
    			connect : function() {          
 		           		   console.log('connect WebSocketService ... URL: ' + URL);
 		    	           var socket = new SockJS(URL + '?access_token=' + OAuthToken.getAccessToken());
	 		   	           self.stompClient = Stomp.over(socket);    
	 		   	           self.stompClient.debug = true;
	    		           self.stompClient.connect({}, function(frame) {
	    	                   connected.resolve("success");
	    		               console.log('Connected: ' + frame);
	    		               
	    		               self.stompClient.subscribe("/user/queue/errors", function(message) {
	    			                console.log("error: " + message.body);
	    				        });
	    					    $rootScope.username 			= '';
	    					    $rootScope.messages 			= [];
	    					    $rootScope.users 				= {};
	    					
	    					    var TOPIC_CURRENT_USER 		= "/app/current.user";
	    					    
	    					    var TOPIC_PARTICIPANTS_PATH 		= "/app/chat.participants";
	    					    var TOPIC_LOGIN_PATH 				= "/topic/chat.login";
	    					    var TOPIC_LOGOUT_PATH 				= "/topic/chat.logout";
	    					    
	    					    var TOPIC_MESSAGES_PATH 		= "/app/chat.messages";
	    					    var TOPIC_MESSAGE_PATH 		= "/topic/chat.message";
	    					    var SEND_MESSAGE_PATH 		= "/app/chat.message";
	    					    var TOPIC_PRIVATE_MESSAGE   = "/user/queue/private.message";
	    					    var SEND_PRIVATE_MESSAGE_PATH = "/app/chat.message/";

	    						methods.subscribe(TOPIC_PRIVATE_MESSAGE, function(message) {
	    					        console.log("myCtrl - message from: " + TOPIC_PRIVATE_MESSAGE + "\n" + message.body);  
	    					        var item = JSON.parse(message.body);
	    					        item.isPrivate = true;
	    					        $rootScope.messages.push(item);
	    					        $rootScope.$apply();
	    					        $rootScope.focusLastMessage();    
	    						});
	    						
	    						methods.subscribe(TOPIC_CURRENT_USER, function(message) {
	    					        console.log("myCtrl - message from: " + TOPIC_CURRENT_USER + "\n" + message.body);        
	    					        $rootScope.username = message.body;
	    					        $rootScope.$apply();
	    						});
	    						
	    						methods.subscribe(TOPIC_MESSAGE_PATH, function(message) {
	    					        console.log("myCtrl - message from: " + TOPIC_MESSAGE_PATH + "\n" + message.body);
	    					        var item = JSON.parse(message.body);
	    					        $rootScope.messages.push(item);
	    					        $rootScope.$apply();
	    					        $rootScope.focusLastMessage();
	    					        
	    						});
	    						methods.subscribe(TOPIC_MESSAGES_PATH, function(messages) {
	    					        console.log("myCtrl - message from: " + TOPIC_MESSAGE_PATH + "\n" + messages.body);
	    					        $rootScope.messages = JSON.parse(messages.body);
	    					        $rootScope.$apply();
	    					        $rootScope.focusLastMessage();
	    					        
	    						});
	    						methods.subscribe(TOPIC_LOGIN_PATH, function(messages) {
	    					        console.log("myCtrl - message from: " + TOPIC_LOGIN_PATH + "\n" + messages.body);
	    					        var user = JSON.parse(messages.body);
	    					        $rootScope.users[user.username] = user;
	    					        $rootScope.$apply();        
	    						});
	    					
	    						methods.subscribe(TOPIC_LOGOUT_PATH, function(messages) {
	    					        console.log("myCtrl - message from: " + TOPIC_LOGOUT_PATH + "\n" + messages.body);
	    					        var user = JSON.parse(messages.body);
	    					        delete $rootScope.users[user.username];
	    					        $rootScope.$apply();        
	    						});
	    					
	    						methods.subscribe(TOPIC_PARTICIPANTS_PATH, function(messages) {
	    					        console.log("myCtrl - message from: " + TOPIC_PARTICIPANTS_PATH + "\n" + messages.body);
	    					        var users = JSON.parse(messages.body);
	    					        angular.forEach(users, function(value, key) {
	    					        	  $rootScope.users[value.username] = value;
	    					        	});
	    					        $rootScope.$apply();        
	    						});
	    					    $rootScope.sendMessage = function () {
	    					
	    					        console.log("chatController - sendMessage to: " + SEND_MESSAGE_PATH);
	    					        var message 	= $rootScope.message;
	    					        $rootScope.message 	= '';
	    					        methods.send(SEND_MESSAGE_PATH, {
	    					        											message: message,
	    					        											sender:{username:$rootScope.username}
	    					        										});
	    					    };
	    					
	    					    $rootScope.sendPrivateMessage = function () {
	    					    	var privateDestination = SEND_PRIVATE_MESSAGE_PATH + $rootScope.privateUser;
	    					        console.log("chatController - sendPrivateMesage to: " + privateDestination);
	    					        var message 	= $rootScope.privateMessage;
	    					        $rootScope.privateMessage	= '';
	    					        methods.send(privateDestination, {
	    					        											message: message,
	    					        											sender:{username:$rootScope.username}
	    					        										});
	    					    };
	    					    $rootScope.focusLastMessage = function () {
	    					        $timeout(function() {
	    					            var scroller = document.getElementById("chat-ul");
	    					            scroller.scrollTop = scroller.scrollHeight;
	    					          }, 500, false);
	    					    };
	    		           });
    			},
    			send : function(destination, json) {
    	            if (self.stompClient != null && !self.stompClient.connected) {
    	            	methods.connect();
    	            }
                    connected.promise.then(function() {
	                    if (self.stompClient != null) {
		    	               console.log("WebSocketService - send to: " + destination);
		    	               self.stompClient.send(destination, {}, JSON.stringify(json));
	                    }
                    }, null, null);
    			},
    			subscribe : function(path, callback) {
    	            methods.connectIfNotConnected();
    	            
                    connected.promise.then(function() {
	                    if (self.stompClient != null) {
	    					  counter++;
			 	              console.log("WebSocketService - subscribe to: " + path);
			 	              var subscriber = self.stompClient.subscribe(path, callback);
			 	              subscribers[path] = {
			 	            		  path : path,
			 	            		  callback: callback,
			 	            		  subscriber : subscriber
			 	              };
	                    }
                    }, null, null);
    			},
    			unsubscribe : function(path) {
                    if (self.stompClient != null) {
	 	               if (subscribers[path] && subscribers[path].subscriber) {
	 	            	   subscribers[path].subscriber.unsubscribe();
	 	            	   subscribers[path] = null;
		 	               console.log("WebSocketService - unsubscribe from: " + path + " - OK");
	 	               }
                    }
    			},
    			disconnect : function () {
                    if (self.stompClient != null) {
                    	self.stompClient.disconnect();
                    	self.stompClient = null;
                    }
    			},
    			connectIfNotConnected : function() {
    				if (self.stompClient == null || !self.stompClient.connected) {
    					connected = $q.defer();
    	            	methods.connect();
    	            }
    			}
    	};

    	return methods;
	});
	
})();