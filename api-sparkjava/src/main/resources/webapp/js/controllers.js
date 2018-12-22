(function() {

	var MainController = function($scope, $location, alertService, loginService) {
		$scope.$on('$routeChangeStart', function(scope, next, current){
			alertService.clearAlerts();
		});
		
		$scope.attemptLogin = function (user, password) {
			loginService.attemptLogin(user, password)
				.then(function (data) {
					$scope.loginError = null;
					$scope.password = null;
					$scope.userId = data.userId;
					$scope.firstname = data.firstname;
					$scope.jwt = data.jwt;

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
			$scope.firstname = null;
			$scope.jwt = null;
			$location.path("/home");
		};
		
		loginService.retrieveSession()
			.then(function (data) {
				if (data) {
					$scope.userId = data.userId;
					$scope.firstname = data.firstname;
					$scope.jwt = data.jwt;
				}
		  	}, function (error) {

		  	});
	};

	var PostController = function($scope, $routeParams, dataService, alertService) {
		
		var postId = $routeParams.postId;
		dataService.getPost(postId)
			.then(function(data) {
				$scope.post = data;
		  	}, function(error) {
		  		alertService.error(error.data);
		  	});
	};

	var PostsController = function($scope, $location, dataService, alertService) {

		var queryParamsString = $location.url().split("?")[1];
		dataService.getPosts(queryParamsString)
			.then(function (data) {
				$scope.posts = data;
		  	}, function (error) {
		  		alertService.error(error.data);
		  	});
		
		$scope.goToPost = function (postId) {
			$location.path("/post/" + postId);
		};
	};

	var AddPostController = function($scope, $location, dataService, alertService) {
		
		$scope.addPost = function (postText) {
			dataService.addPost($scope.userId, postText, $scope.jwt)
				.then(function (data) {
					$location.path("/home");
				}, function (error) {
					alertService.error(error.data);
				});
		};
	};
	
	angular.module("mytwitter")
		.controller("MainController", MainController)
		.controller("PostController", PostController)
		.controller("PostsController", PostsController)
		.controller("AddPostController", AddPostController);
	
}());