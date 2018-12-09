(function(){
  
  var dataService = function($http, $location) {

    var getPost = function (postId) {
    	return $http.get("/api/post/" + postId)
              .then(function(response) {
            	  return response.data;
              });
    };
    
    var getPosts = function (queryParamString) {
    	return $http.get("/api/posts?" + queryParamString)
              .then(function(response) {
            	  return response.data;
              });
    };
    
    var addPost = function (userId, text, jwt) {
    	var data = {
			userId: userId,
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + jwt
    		}
    	};

    	return $http.post("/api/post/add", data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    return {
    	getPost: getPost,
    	getPosts: getPosts,
    	addPost: addPost
    };
  };

  var loginService = function($http, $rootScope) {

    var attemptLogin = function (user, password) {
    	var data = {
    		user: user,
    		password: password
    	};
    	return $http.post("/api/login", data)
        	.then(function(response) {
        		return response.data;
        });
    };
	
    var startUserSession = function (data) {
    	$rootScope.userId = data.userId;
    	$rootScope.firstname = data.firstname;
    	$rootScope.jwt = data.jwt;
    };
    
    var endUserSession = function () {
    	$rootScope.userId = null;
    	$rootScope.firstname = null;
    	$rootScope.jwt = null;
    };
    
	return {
    	attemptLogin: attemptLogin,
    	startUserSession: startUserSession,
    	endUserSession: endUserSession
	};
  };

  var alertService = function($rootScope) {
	
	var error = function(errorText){
		$rootScope.alertType = 'danger';
		$rootScope.alertText = errorText;
	};
	
	var clearAlerts = function(){
		$rootScope.alertText = null;
	};
	
	return {
		error: error,
		clearAlerts, clearAlerts
	};
  };

	  
  angular.module("mytwitter")
  	.factory("dataService", dataService)
  	.factory("loginService", loginService)
  	.factory("alertService", alertService);
}());