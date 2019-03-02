(function() {

	var MainController = function($scope, $location, $route, alertService, loginService, usersService) {
		$scope.$on('$routeChangeStart', function(scope, next, current){
			alertService.clearAlerts();
		});

		loginService.retrieveSession()
			.then(function (data) {
				if (data) {
					$scope.userId = data.userId;
					$scope.username = data.username;
					$scope.firstname = data.firstname;
					$scope.token = data.token;
					$route.reload();
				}
		  	}, function (error) {
	
		  	});

		$scope.attemptLogin = function (user, password) {
			loginService.attemptLogin(user, password)
				.then(function (data) {
					$scope.loginError = null;
					$scope.password = null;
					$scope.userId = data.userId;
					$scope.username = data.username;
					$scope.firstname = data.firstname;
					$scope.token = data.token;

					if ($location.path() == "/login") {
						$location.path("/home");
					}
			  	}, function (error) {
			  		$scope.loginError = error.data;
			  		$scope.password = null;
			  	});
		};

		$scope.logout = function () {
			loginService.logout()
				.then(function (response) {
					
				}, function (error) {
					
				});
			$scope.userId = null;
			$scope.username = null;
			$scope.firstname = null;
			$scope.token = null;
			$location.path("/home");
		};

		$scope.showUsersModal = function (userIds) {
			if (userIds && userIds.length > 0) {
				usersService.getUsernames(userIds)
					.then(function (data) {
						if (data) {
							$scope.modalUsers = data;
							if ($scope.userId) {
								usersService.getFollowingUserIds($scope.userId)
									.then(function (data) {
										$scope.modalFollowingUserIds = data;
									});
							}
							$('#usersModal').modal();
						}
					});
			}
		};
	};

	var LogoutController = function($scope) {
		$scope.logout();
	};
	
	var HomeController = function ($scope, postsService, alertService) {
		
		if ($scope.userId) {
			postsService.getFollowingPosts($scope.userId)
				.then(function (data) {
					$scope.posts = data;
			  	}, function (error) {
			  		alertService.error(error.data);
			  	});
		} else {
			postsService.getPosts("")
				.then(function (data) {
					$scope.posts = data;
			  	}, function (error) {
			  		alertService.error(error.data);
			  	});
		}
	};
	
	var PostController = function($scope, $routeParams, postsService, alertService) {
		
		var postId = $routeParams.postId;
		postsService.getPost(postId)
			.then(function(data) {
				$scope.entry = data;
		  	}, function(error) {
		  		alertService.error(error.data);
		  	});
	};

	var PostsController = function($scope, $location, postsService, alertService) {

		var queryParamsString = $location.url().split("?")[1];
		postsService.getPosts(queryParamsString)
			.then(function (data) {
				$scope.posts = data;
		  	}, function (error) {
		  		alertService.error(error.data);
		  	});
		
		$scope.goToPost = function (postId) {
			$location.path("/post/" + postId);
		};
	};

	var AddPostController = function($scope, $route, postsService, alertService) {
		
		$scope.addPost = function (userId, postText) {
			postsService.addPost(userId, postText, $scope.token)
				.then(function (data) {
					$route.reload();
				}, function (error) {
					alertService.error(error.data);
				});
		};
	};
	
	var EntryController = function($scope, $route, $location, postsService, alertService) {

		$scope.liked = function (entry) {
			return entry.likes.indexOf($scope.userId) > -1;
		}
		
		$scope.startEditing = function (entry) {
			entry.editing = true;
			$scope.editText = entry.text;
		};

		$scope.cancelEditing = function (entry) {
			entry.editing = false;
		};

		$scope.editEntry = function (postId, commentId, text) {
			
			if (commentId) {
				postsService.editComment(commentId, text, $scope.token)
					.then(function (data) {
						$route.reload();
					}, function (error) {
						alertService.error(error.data);
					});
				
			} else {
				postsService.editPost(postId, text, $scope.token)
					.then(function (data) {
						$route.reload();
					}, function (error) {
						alertService.error(error.data);
					});
			}
		};
		
		$scope.deleteEntry = function (postId, commentId) {
			
			if (commentId) {
				if (confirm("Are you sure you want to delete your comment?")) {
					postsService.deleteComment(commentId, $scope.token)
					.then(function (data) {
						$route.reload();
					}, function (error) {
						alertService.error(error.data);
					});
				}

			} else {
				if (confirm("Are you sure you want to delete your post?")) {
					postsService.deletePost(postId, $scope.token)
						.then(function (data) {
							$location.path("/posts");
						}, function (error) {
							alertService.error(error.data);
						});
				}
			}
		};
		
		let removeLikeFromEntry = function (entry) {
			let index = entry.likes.indexOf($scope.userId);
			if (index > -1) {
				entry.likes.splice(index, 1);
			}
		};
		
		$scope.likeEntry = function (entry) {
			
			if (entry.commentId) {
				postsService.likeComment(entry.commentId, $scope.userId, $scope.token)
					.then(function (data) {
						entry.likes.push($scope.userId);
					}, function (error) {
						alertService.error(error.data);
					});
			} else {
				postsService.likePost(entry.postId, $scope.userId, $scope.token)
					.then(function (data) {
						entry.likes.push($scope.userId);
					}, function (error) {
						alertService.error(error.data);
					});
			}
		};
		
		$scope.unlikeEntry = function (entry) {

			if (entry.commentId) {
				postsService.unlikeComment(entry.commentId, $scope.userId, $scope.token)
					.then(function (data) {
						removeLikeFromEntry(entry);
					}, function (error) {
						alertService.error(error.data);
					});
			} else {
				postsService.unlikePost(entry.postId, $scope.userId, $scope.token)
					.then(function (data) {
						removeLikeFromEntry(entry);
					}, function (error) {
						alertService.error(error.data);
					});
			}
		};
	};

	var AddCommentController = function($scope, $route, postsService, alertService) {
		
		$scope.addComment = function (userId, postId, commentText) {
			postsService.addComment(userId, postId, commentText, $scope.token)
				.then(function (data) {
					$route.reload();
				}, function (error) {
					alertService.error(error.data);
				});
		};
	};

	var EditPasswordController = function($scope, $location, usersService) {
		
		$scope.editPassword = function (currentPassword, newPassword1, newPassword2) {

			if (newPassword1 != newPassword2) {
				$scope.errorText = 'New Passwords Do Not Match';
			} else {
				usersService.editPassword($scope.userId, currentPassword, newPassword1, $scope.token)
					.then(function (data) {
						alert("Password Changed Successfully")
						$location.path("/home");
					}, function (error) {
						$scope.errorText = error.data;
					});
			}
		};
	};

	var UserPageController = function($scope, $location, $routeParams, usersService, postsService) {

		var username;
		if ($location.path() == "/myprofile") {
			username = $scope.username;
			if (!username) {
				$location.path("/home");
				return;
			}
		} else {
			username = $routeParams.username;
		}
		
		usersService.getUserPageInfo(username)
			.then(function (data) {
				$scope.user = data;
				postsService.getPostsByUserId(data.userId)
				.then(function (postData) {
					$scope.posts = postData;
				});
			});
		
		$scope.getLikedPosts = function () {
			if (!$scope.likedPosts) {
				postsService.getLikedPostsByUserId($scope.user.userId)
				.then(function (data) {
					$scope.likedPosts = data;
				});
			}
		};
		
		$scope.getCommentedPosts = function () {
			if (!$scope.commentedPosts) {
				postsService.getCommentedPostsByUserId($scope.user.userId)
				.then(function (data) {
					$scope.commentedPosts = data;
				});
			}
		};
		
		$scope.getComments = function () {
			if (!$scope.comments) {
				postsService.getCommentsByUserId($scope.user.userId)
				.then(function (data) {
					$scope.comments = data;
				});
			}
		};
		
		$scope.isFollowing = function () {
			return $scope.user && $scope.user.followersUserIds.indexOf($scope.userId) > -1;
		};
		
		$scope.followUser = function () {
			usersService.followUser($scope.userId, $scope.user.userId, $scope.token);
			$scope.user.followersUserIds.push($scope.userId);
		};
		
		$scope.unfollowUser = function () {
			usersService.unfollowUser($scope.userId, $scope.user.userId, $scope.token);
			let index = $scope.user.followersUserIds.indexOf($scope.userId);
			if (index > -1) {
				$scope.user.followersUserIds.splice(index, 1);
			}
		};
	};

	var UsersModalController = function($scope, $location, usersService) {
		$scope.isFollowing = function (userId) {
			return $scope.modalFollowingUserIds && $scope.modalFollowingUserIds.indexOf(userId) > -1;
		};
		
		$scope.followUser = function (userId) {
			usersService.followUser($scope.userId, userId, $scope.token);
			$scope.modalFollowingUserIds.push(userId);
		};
		
		$scope.unfollowUser = function (userId) {
			usersService.unfollowUser($scope.userId, userId, $scope.token);
			let index = $scope.modalFollowingUserIds.indexOf(userId);
			if (index > -1) {
				$scope.modalFollowingUserIds.splice(index, 1);
			}
		};
		
		$scope.goToUser = function (username) {
			$location.path("/user/" + username);
			$('#usersModal').modal('hide');
		};
	};

	angular.module("mysocialmedia")
		.controller("MainController", MainController)
		.controller("HomeController", HomeController)
		.controller("LogoutController", LogoutController)
		.controller("PostController", PostController)
		.controller("PostsController", PostsController)
		.controller("EntryController", EntryController)
		.controller("AddPostController", AddPostController)
		.controller("AddCommentController", AddCommentController)
		.controller("EditPasswordController", EditPasswordController)
		.controller("UserPageController", UserPageController)
		.controller("UsersModalController", UsersModalController);
	
}());