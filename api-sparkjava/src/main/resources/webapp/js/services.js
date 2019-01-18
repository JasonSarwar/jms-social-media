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

    var addComment = function (userId, postId, text, jwt) {
    	var data = {
			userId: userId,
			postId: postId,
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + jwt
    		}
    	};

    	return $http.post("/api/comment/add", data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    return {
    	getPost: getPost,
    	getPosts: getPosts,
    	addPost: addPost,
    	addComment: addComment
    };
  };

  var loginService = function($http) {

    var attemptLogin = function (user, password) {
    	var data = {
    		user: user,
    		password: password
    	};
    	return $http.post("/api/login", data)
        	.then(function (response) {
        		return response.data;
        });
    };

    var logout = function () {
    	return $http.post("/api/logout");
    };
    
    var retrieveSession = function () {
    	return $http.post("/api/retrieveSession")
        	.then(function (response) {
        		return response.data;
        });
    };
    
	return {
    	attemptLogin: attemptLogin,
    	logout: logout,
    	retrieveSession: retrieveSession
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