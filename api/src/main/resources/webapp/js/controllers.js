(function() {

	var MainController = function($scope, $location, $route, alertService, loginService, usersService) {
		$scope.$on('$routeChangeStart', function(scope, next, current){
			alertService.clearAlerts();
		});

		loginService.retrieveSession()
			.then(function (data) {
				if (data) {
					$scope.createSession(data);
					$route.reload();
				} else {
					$location.path("/posts");
				}
		  	});

		$scope.createSession = function (data) {
			$scope.loginError = undefined;
			$scope.password = undefined;
			$scope.userId = data.userId;
			$scope.username = data.username;
			$scope.firstname = data.firstname;
		};

		$scope.logout = function () {
			loginService.logout()

			$scope.userId = null;
			$scope.username = null;
			$scope.firstname = null;
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
							angular.element('#usersModal').modal();
						}
					});
			}
		};
	};

	var LoginController = function($scope, $location, loginService) {
		$scope.attemptLogin = function (user, password) {
			loginService.attemptLogin(user, password)
				.then(function (data) {
					$scope.createSession(data);

					if ($location.path() == "/login") {
						$location.path("/home");
					}
			  	}, function (error) {
			  		$scope.loginError = error.data;
			  		$scope.password = null;
			  	});
		};
	};

	var LogoutController = function($scope) {
		$scope.logout();
	};
	
	var HomeController = function ($scope, postsService, alertService) {
		
		if ($scope.userId) {
			postsService.getFeedPosts($scope.userId)
				.then(function (data) {
					$scope.posts = data;
			  	}, function (error) {
			  		alertService.error(error.data);
			  	});
		} else {
			postsService.getPosts()
				.then(function (data) {
					$scope.posts = data;
			  	}, function (error) {
			  		alertService.error(error.data);
			  	});
		}
	};
	
	var PostController = function($scope, $routeParams, postsService, alertService) {
		let comments;
		var postId = $routeParams.postId;
		postsService.getPost(postId)
			.then(function (data) {
				$scope.entry = data;
				if (comments) {
					$scope.entry.comments = comments;
				}
		  	}, function (error) {
		  		alertService.error(error.data);
		  	});
		postsService.getComments(postId)
			.then(function (data) {
				if ($scope.entry) {
					$scope.entry.comments = data;
				} else {
					comments = data;
				}
		  	}, function (error) {
		  		alertService.error(error.data);
		  	});
	};

	var PostsController = function($scope, $location, postsService, alertService) {
		let locationSearch = $location.search();
		let params = {
				username: locationSearch.username,
				tag: locationSearch.tag,
				on: locationSearch.on
		}
		$location.search({});
		$location.search(params);

		postsService.getPosts(params)
			.then(function (data) {
				$scope.posts = data;
		  	}, function (error) {
		  		alertService.error(error.data);
		  	});

		$scope.tagName = $location.search().tag;
		
		$scope.goToPost = function (postId) {
			$location.path("/post/" + postId);
		};
	};

	var AddPostController = function($scope, $route, postsService, alertService) {
		
		$scope.addPost = function (userId, postText) {
			postsService.addPost(userId, postText)
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
				postsService.editComment(commentId, text)
					.then(function (data) {
						$route.reload();
					}, function (error) {
						alertService.error(error.data);
					});
				
			} else {
				postsService.editPost(postId, text)
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
					postsService.deleteComment(commentId)
					.then(function (data) {
						$route.reload();
					}, function (error) {
						alertService.error(error.data);
					});
				}

			} else {
				if (confirm("Are you sure you want to delete your post?")) {
					postsService.deletePost(postId)
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
				postsService.likeComment(entry.commentId, $scope.userId)
					.then(function (data) {
						entry.likes.push($scope.userId);
					}, function (error) {
						alertService.error(error.data);
					});
			} else {
				postsService.likePost(entry.postId, $scope.userId)
					.then(function (data) {
						entry.likes.push($scope.userId);
					}, function (error) {
						alertService.error(error.data);
					});
			}
		};
		
		$scope.unlikeEntry = function (entry) {

			if (entry.commentId) {
				postsService.unlikeComment(entry.commentId, $scope.userId)
					.then(function (data) {
						removeLikeFromEntry(entry);
					}, function (error) {
						alertService.error(error.data);
					});
			} else {
				postsService.unlikePost(entry.postId, $scope.userId)
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
			postsService.addComment(userId, postId, commentText)
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
				usersService.editPassword($scope.userId, currentPassword, newPassword1)
					.then(function (data) {
						alert("Password Changed Successfully");
						$location.path("/home");
					}, function (error) {
						$scope.errorText = error.data;
					});
			}
		};
	};

	var UserPageController = function($scope, $routeParams, usersService, postsService, alertService) {

		var username = $routeParams.username;

		usersService.getUserPageInfo(username)
			.then(function (data) {
				$scope.user = data;
				postsService.getPostsByUserId(data.userId)
				.then(function (postData) {
					$scope.posts = postData;
				});
			}, function (error) {
				alertService.error(error.data);
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
			usersService.followUser($scope.userId, userId);
			$scope.modalFollowingUserIds.push(userId);
		};
		
		$scope.unfollowUser = function (userId) {
			usersService.unfollowUser($scope.userId, userId);
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

	var SignupController = function($scope, $location, signupService) {
		
		if ($scope.userId) {
			$location.path("/home");
		}
		
		$scope.months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
		let monthsWith31Days = ["January", "March", "May", "July", "August", "October", "December"];
		let monthsWith30Days = ["April", "June", "September", "November"];
		
		$scope.days = [];
		for (let i = 1; i <= 31; i++) {
			$scope.days.push(i);
		}

		$scope.years = [];
		let lastYear = new Date().getFullYear() - 1;
		for (let i = lastYear - 150; i <= lastYear; i++) {
			$scope.years.push(i);
		}

		let adjustDays = function(noOfDays) {
			while ($scope.days.length < noOfDays) {
				$scope.days.push($scope.days.length + 1);
			}
			while ($scope.days.length > noOfDays) {
				$scope.days.splice($scope.days.length -1, 1);
			}
		};
		
		let adjustIfFebruary = function() {
			if ($scope.newUserMonth == "February") {
				let noOfDays = 29;
				if ($scope.newUserYear % 4 != 0) {
					noOfDays = 28;
				} else if ($scope.newUserYear % 100 != 0) {
					noOfDays = 29
				} else if ($scope.newUserYear % 400 != 0) {
					noOfDays = 28
				}
				adjustDays(noOfDays);
			}
		};
		
		$scope.changeMonth = function() {
			adjustIfFebruary();
			if (monthsWith31Days.indexOf($scope.newUserMonth) > -1) {
				adjustDays(31);
			} else if (monthsWith30Days.indexOf($scope.newUserMonth) > -1) {
				adjustDays(30);
			}
		};
		
		$scope.changeYear = function() {
			adjustIfFebruary();
		};

		$scope.passwordStrength = function (field) {

			if (!field.$error || angular.equals(field.$error, {})) {
				let password = field.$viewValue;

				let score = Math.floor(password.length / 10);
				if (password.match(/\d/)) {
					score++;
				}
				if (password.match(/[a-z]/)) {
					score++;
				}
				if (password.match(/[A-Z]/)) {
					score++;
				}
				if (password.match(/[~`!@#\$%\^&\*()\-_+=\[\]{}|\\:";',\.\/<>?]/g)) {
					score += 2;
				}

				if (score > 6) {
					$scope.passwordFeedback = "Strong";
				} else if (score > 4) {
					$scope.passwordFeedback = "Good";
				} else if (score > 2) {
					$scope.passwordFeedback = "Average";
				} else {
					$scope.passwordFeedback = "Weak";
				}
			};
		};

		$scope.signup = function() {
			$scope.signupError = undefined;
			if ($scope.signupForm.$valid) {
				$scope.newUser.birthDate = $scope.newUserMonth + " " + $scope.newUserDay + ", " + $scope.newUserYear;
				signupService.addUser($scope.newUser)
					.then(function (data) {
						$scope.createSession(data);
						alert("Your account has been created!");
						$location.path("/user/" + data.username);
					}, function (error) {
						$scope.signupError = error.data;
					});
			}
		};
	};

	angular.module("mysocialmedia")
		.controller("MainController", MainController)
		.controller("HomeController", HomeController)
		.controller("LoginController", LoginController)
		.controller("LogoutController", LogoutController)
		.controller("PostController", PostController)
		.controller("PostsController", PostsController)
		.controller("EntryController", EntryController)
		.controller("AddPostController", AddPostController)
		.controller("AddCommentController", AddCommentController)
		.controller("EditPasswordController", EditPasswordController)
		.controller("UserPageController", UserPageController)
		.controller("UsersModalController", UsersModalController)
		.controller("SignupController", SignupController);
	
}());