(function() {
	var app = angular.module("mysocialmedia", ["ngRoute", "ngSanitize"])
		.config(function($routeProvider) {
			$routeProvider
				.when("/home", {
					templateUrl: "partials/home.html"
				})
				.when("/login", {
					templateUrl: "partials/login.html"
				})
				.when("/logout", {
					templateUrl: "partials/logout.html",
					controller: "LogoutController"
				})
				.when("/signup", {
					templateUrl: "partials/signup.html"
				})
				.when("/post/:postId", {
					templateUrl: "partials/post.html",
					controller: "PostController"
				})
				.when("/posts", {
					templateUrl: "partials/posts.html",
					controller: "PostsController"
				})
				.when("/user/:username", {
					templateUrl: "partials/user-page.html",
					controller: "UserPageController"
				})
				.when("/editpassword", {
					templateUrl: "partials/edit-password.html",
					controller: "EditPasswordController"
				})
				.otherwise({redirectTo: "/home"});
	});
}());