(function() {
	var app = angular.module("mytwitter", ["ngRoute", "ngSanitize"])
		.config(function($routeProvider) {
			$routeProvider
				.when("/home", {
					templateUrl: "partials/home.html"
				})
				.when("/login", {
					templateUrl: "partials/login.html"
				})
				.when("/post/:postId", {
					templateUrl: "partials/post.html",
					controller: "PostController"
				})
				.when("/posts", {
					templateUrl: "partials/posts.html",
					controller: "PostsController"
				})
				.otherwise({redirectTo: "/home"});
	});
}());