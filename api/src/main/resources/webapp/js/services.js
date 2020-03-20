(function(){

	let setUpApiPath = function($http) {
		$http.apiPath =  "/api";
		$http.get("config.json")
	        .then(function (response) {
	        	$http.apiPath = response.data.apiPath;
	        });
	}
	
	var postsService = function($http, $location) {

	    var getPost = function (postId) {
	    	return $http.get($http.apiPath + "/post/" + postId)
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var getComments = function (postId) {
	    	return $http.get($http.apiPath + "/post/" + postId + "/comments")
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
	    	return $http.get($http.apiPath + "/posts" + queryParamString)
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var getFeedPosts = function (username) {
	    	return $http.get($http.apiPath + "/user/" + username + "/feed")
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var addPost = function (userId, text) {
	    	return $http.post($http.apiPath + "/post/add", {userId: userId, text: text})
	        	.then(function (response) {
	        		return response.data;
	        });
	    };

	    var editPost = function (postId, text) {
	    	return $http.put($http.apiPath + "/post/" + postId, {text: text})
	        	.then(function (response) {
	        		return response.data;
	        });
	    };

	    var deletePost = function (postId) {
	    	return $http.delete($http.apiPath + "/post/" + postId)
	        	.then(function (response) {
	        		return response.data;
	        });
	    };

	    var likePost = function (postId, userId) {
	    	return $http.post($http.apiPath + "/post/" + postId + "/like/" + userId, null)
	        	.then(function (response) {
	        		return response.data;
	        });
	    };

	    var unlikePost = function (postId, userId) {
	    	return $http.delete($http.apiPath + "/post/" + postId + "/unlike/" + userId)
	        	.then(function (response) {
	        		return response.data;
	        });
	    };

	    var getPostsByUserId = function (userId) {
	    	return $http.get($http.apiPath + "/user/" + userId + "/posts")
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var getLikedPostsByUserId = function (userId) {
	    	return $http.get($http.apiPath + "/user/" + userId + "/likedposts")
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var getCommentedPostsByUserId = function (userId) {
	    	return $http.get($http.apiPath + "/user/" + userId + "/commentedposts")
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var getCommentsByUserId = function (userId) {
	    	return $http.get($http.apiPath + "/user/" + userId + "/comments")
	              .then(function (response) {
	            	  return response.data;
	              });
	    };

	    var addComment = function (userId, postId, text) {
	    	var data = {
				userId: userId,
				postId: postId,
				text: text
	    	};

	    	return $http.post($http.apiPath + "/comment/add", data)
	        	.then(function(response) {
	        		return response.data;
	        });
	    };

	    var editComment = function (commentId, text) {
	    	return $http.put($http.apiPath + "/comment/" + commentId, {text: text})
	        	.then(function(response) {
	        		return response.data;
	        });
	    };

	    var deleteComment = function (commentId) {
	    	return $http.delete($http.apiPath + "/comment/" + commentId)
	        	.then(function(response) {
	        		return response.data;
	        });
	    };

	    var likeComment = function (commentId, userId) {
	    	return $http.post($http.apiPath + "/comment/" + commentId + "/like/" + userId, null)
	        	.then(function(response) {
	        		return response.data;
	        });
	    };

	    var unlikeComment = function (commentId, userId) {
	    	return $http.delete($http.apiPath + "/comment/" + commentId + "/unlike/" + userId)
	        	.then(function(response) {
	        		return response.data;
	        });
	    };

	    return {
	    	getPost: getPost,
	    	getComments: getComments,
	    	getPosts: getPosts,
	    	getFeedPosts: getFeedPosts,
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
			return $http.get($http.apiPath + "/user/" + username + "/pageinfo")
				.then(function (response) {
					return response.data;
				});
		};

		var getUsersToFollow = function (username, max) {
			let url = $http.apiPath + "/user/" + username + "/userstofollow";
			if (max) {
				url = url + "?max=" + max
			}
			return $http.get(url)
				.then(function (response) {
					return response.data;
				});
		};

	    var editPassword = function (userId, oldPassword, newPassword1, newPassword2) {
	    	var data = {
	    		userId: userId,
	    		oldPassword: oldPassword,
	    		newPassword1: newPassword1,
	    		newPassword2: newPassword2
	    	};

	    	return $http.put($http.apiPath + "/user/password", data)
	        	.then(function (response) {
	        		return response.data;
	        });
	    };
	
	    var followUser = function (followerUsername, followingUsername) {
	    	return $http.post($http.apiPath + "/user/follow", {followerUsername: followerUsername, followingUsername: followingUsername})
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
    
	    var unfollowUser = function (followerUsername, followingUsername) {
	    	return $http.post($http.apiPath + "/user/unfollow", {followerUsername: followerUsername, followingUsername: followingUsername})
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
	    
	    var getFollowingUsernames = function (username) {
	    	return $http.get($http.apiPath + "/user/" + username + "/following")
		    	.then(function (response) {
		    		return response.data;
		    	});
	    };
	
		return {
			getUserPageInfo: getUserPageInfo,
			getUsersToFollow: getUsersToFollow,
	    	editPassword: editPassword,
	    	followUser: followUser,
	    	unfollowUser: unfollowUser,
	    	getFollowingUsernames: getFollowingUsernames
		};
    };
	
    var loginService = function ($http, $cookies) {

		setUpApiPath($http);

		let sessionCookie = "jms-social-media-session";

	    var attemptLogin = function (usernameOrEmail, password) {
	    	return $http.post($http.apiPath + "/login?createSession", {usernameOrEmail: usernameOrEmail, password: password})
	        	.then(function (response) {
	        		$http.defaults.headers.common.Authorization = "Bearer " + response.data.token;
	        		return response.data;
	        });
	    };
	
	    var logout = function () {
	    	$http.defaults.headers.common.Authorization = undefined;
	    	$cookies.remove(sessionCookie);
	    	return $http.post($http.apiPath + "/logout");
	    };
	    
	    var retrieveSession = function () {
	    	return $http.post($http.apiPath + "/retrieveSession")
	        	.then(function (response) {
	        		if (response.data) {
	        			$http.defaults.headers.common.Authorization = "Bearer " + response.data.token;
	        		} else {
	        			$cookies.remove(sessionCookie);
	        		}
	        		return response.data;
	        	}, function (error) {
	        		$cookies.remove(sessionCookie);
	        		return null;
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
			return $http.get($http.apiPath + "/users/isUsernameTaken/" + username)
		    	.then(function (response) {
		    		return response.data;
		    	});
		};
		
		var isEmailTaken = function (email) {
			return $http.get($http.apiPath + "/users/isEmailTaken/" + email)
		    	.then(function (response) {
		    		return response.data;
		    	});
		};

		var addUser = function (newUser) {
			return $http.post($http.apiPath + "/user/add?createSession", newUser)
				.then(function (response) {
					if (response.data) {
						$http.defaults.headers.common.Authorization = "Bearer " + response.data.token;
					}
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
	
		var error = function (errorText) {
			$rootScope.alertType = 'danger';
			$rootScope.alertText = errorText;
		};
	
		var clearAlerts = function() {
			$rootScope.alertText = null;
		};
		
		return {
			error: error,
			clearAlerts: clearAlerts
		};
	};

	angular.module("mysocialmedia")
		.factory("postsService", postsService)
		.factory("usersService", usersService)
		.factory("loginService", loginService)
		.factory("signupService", signupService)
		.factory("alertService", alertService);
}());