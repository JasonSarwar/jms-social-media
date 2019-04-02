(function(){
  
  var postsService = function($http, $location) {

    var getPost = function (postId) {
    	return $http.get("/api/post/" + postId)
              .then(function (response) {
            	  return response.data;
              });
    };

    var getComments = function (postId) {
    	return $http.get("/api/post/" + postId + "/comments")
              .then(function (response) {
            	  return response.data;
              });
    };

    var getPosts = function (queryParams) {
    	let queryParamString = "?sortBy=postId&order=desc";
    	if (queryParams) {
    		if (queryParams.username) {
    			queryParamString = queryParamString + "&username=" + queryParams.username;
    		}
    		if (queryParams.tag) {
    			queryParamString = queryParamString + "&tag=" + queryParams.tag;
    		}
    		if (queryParams.on) {
    			queryParamString = queryParamString + "&on=" + queryParams.on;
    		}
    	}
    	return $http.get("/api/posts" + queryParamString)
              .then(function(response) {
            	  return response.data;
              });
    };

    var getFollowingPosts = function (userId) {
    	return $http.get("/api/user/" + userId + "/following/posts")
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

    var likePost = function (postId, userId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.post("/api/post/" + postId + "/like/" + userId, null, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var unlikePost = function (postId, userId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.delete("/api/post/" + postId + "/unlike/" + userId, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var getPostsByUserId = function (userId) {
    	return $http.get("/api/user/" + userId + "/posts")
              .then(function(response) {
            	  return response.data;
              });
    };
    
    var getLikedPostsByUserId = function (userId) {
    	return $http.get("/api/user/" + userId + "/likedposts")
              .then(function(response) {
            	  return response.data;
              });
    };

    var getCommentedPostsByUserId = function (userId) {
    	return $http.get("/api/user/" + userId + "/commentedposts")
              .then(function(response) {
            	  return response.data;
              });
    };

    var getCommentsByUserId = function (userId) {
    	return $http.get("/api/user/" + userId + "/comments")
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

    var likeComment = function (commentId, userId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.post("/api/comment/" + commentId + "/like/" + userId, null, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    var unlikeComment = function (commentId, userId, token) {

    	var configs = {
    		headers: {
    			"Authorization": "Bearer " + token
    		}
    	};

    	return $http.delete("/api/comment/" + commentId + "/unlike/" + userId, configs)
        	.then(function(response) {
        		return response.data;
        });
    };

    return {
    	getPost: getPost,
    	getComments: getComments,
    	getPosts: getPosts,
    	getFollowingPosts: getFollowingPosts,
    	addPost: addPost,
    	editPost: editPost,
    	deletePost: deletePost,
    	likePost: likePost,
    	unlikePost: unlikePost,
    	getPostsByUserId: getPostsByUserId,
    	getLikedPostsByUserId: getLikedPostsByUserId,
    	getCommentedPostsByUserId: getCommentedPostsByUserId,
    	getCommentsByUserId: getCommentsByUserId,
    	addComment: addComment,
    	editComment: editComment,
    	deleteComment: deleteComment,
    	likeComment: likeComment,
    	unlikeComment: unlikeComment
    };
  };

  	var usersService = function($http) {
	  
		var getUserPageInfo = function (username) {
			return $http.get("/api/user/" + username + "/pageinfo")
				.then(function (response) {
					return response.data;
				});
		};
		
		var getUsernames = function (userIds) {
			return $http.get("/api/users?ids=" + userIds.join())
				.then(function (response) {
					return response.data;
				});
		}
		
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
	
	    var followUser = function (followerUserId, followingUserId, token) {
	    	var configs = {
	    		headers: {
	    			"Authorization": "Bearer " + token
	    		}
	    	};
	    	
	    	return $http.post("/api/user/follow", {followerUserId: followerUserId, followingUserId: followingUserId}, configs)
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
    
	    var unfollowUser = function (followerUserId, followingUserId, token) {
	    	var configs = {
	    		headers: {
	    			"Authorization": "Bearer " + token
	    		}
	    	};

	    	return $http.post("/api/user/unfollow", {followerUserId: followerUserId, followingUserId: followingUserId}, configs)
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
	    
	    var getFollowingUserIds = function (userId) {
	    	return $http.get("/api/user/" + userId + "/following")
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
	
		return {
			getUserPageInfo: getUserPageInfo,
			getUsernames: getUsernames,
	    	editPassword: editPassword,
	    	followUser: followUser,
	    	unfollowUser: unfollowUser,
	    	getFollowingUserIds: getFollowingUserIds
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

  	var signupService = function ($http) {
		
		var isUsernameTaken = function (username) {
			return $http.get("/api/users/isUsernameTaken/" + username)
		    	.then(function (response) {
		    		return response.data;
		    	});
		};
		
		var isEmailTaken = function (email) {
			return $http.get("/api/users/isEmailTaken/" + email)
		    	.then(function (response) {
		    		return response.data;
		    	});
		};

		var addUser = function (newUser) {
			return $http.post("/api/user/add", newUser)
				.then(function (response) {
		    		return response.data;
		    	});
		};

		return {
			isUsernameTaken: isUsernameTaken,
			isEmailTaken: isEmailTaken,
			addUser: addUser
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

	  
  angular.module("mysocialmedia")
  	.factory("postsService", postsService)
  	.factory("usersService", usersService)
  	.factory("loginService", loginService)
  	.factory("signupService", signupService)
  	.factory("alertService", alertService);
}());