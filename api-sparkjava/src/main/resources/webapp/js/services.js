(function(){
  
  var postsService = function($http, $location) {

    var getPost = function (postId) {
    	return $http.get("/api/post/" + postId + "/full")
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
    
    var addPost = function (userId, text, token) {
    	var data = {
			userId: userId,
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.post("/api/post/add", data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var editPost = function (postId, text, token) {
    	var data = {
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.put("/api/post/" + postId, data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var deletePost = function (postId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.delete("/api/post/" + postId, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var addComment = function (userId, postId, text, token) {
    	var data = {
			userId: userId,
			postId: postId,
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.post("/api/comment/add", data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var editComment = function (commentId, text, token) {
    	var data = {
			text: text
    	};

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.put("/api/comment/" + commentId, data, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var deleteComment = function (commentId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.delete("/api/comment/" + commentId, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    return {
    	getPost: getPost,
    	getPosts: getPosts,
    	addPost: addPost,
    	editPost: editPost,
    	deletePost: deletePost,
    	addComment: addComment,
    	editComment: editComment,
    	deleteComment: deleteComment
    };
  };

  var usersService = function($http) {

    var editPassword = function (userId, oldPassword, newPassword, token) {
    	var data = {
    		userId: userId,
    		oldPassword: oldPassword,
    		newPassword: newPassword
    	};
    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};
    	return $http.put("/api/user/password", data, configs)
        	.then(function (response) {
        		return response.data;
        });
    };

    
	return {
    	editPassword: editPassword
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
  	.factory("postsService", postsService)
  	.factory("usersService", usersService)
  	.factory("loginService", loginService)
  	.factory("alertService", alertService);
}());