(function() {
	angular.module("mysocialmedia", ["ngRoute", "ngSanitize", "ngMessages", "ngCookies"])
		.config(function($routeProvider) {
			$routeProvider
				.when("/home", {
					templateUrl: "partials/home.html",
					controller: "HomeController"
				})
				.when("/login", {
					templateUrl: "partials/login.html",
					controller: "LoginController"
				})
				.when("/logout", {
					templateUrl: "partials/logout.html",
					controller: "LogoutController"
				})
				.when("/signup", {
					templateUrl: "partials/signup.html",
					controller: "SignupController"
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
	
		}).directive("username", function ($q, signupService) {
			return {
				require: "ngModel",
				link: function (scope, element, attrs, ctrl) {
					ctrl.$asyncValidators.taken = function (modelValue, viewValue) {
						var defer = $q.defer();
						signupService.isUsernameTaken(modelValue)
							.then(function (data) {
								if (data === "false") {
									defer.resolve("Available");
								} else {
									defer.reject("Taken");
								}
							}, function (error) {
								defer.reject("Error");
							});
						
						return defer.promise;
					};
				}
			};
		}).directive("email", function ($q, signupService) {
			return {
				require: "ngModel",
				link: function (scope, element, attrs, ctrl) {
					ctrl.$asyncValidators.taken = function (modelValue, viewValue) {
						var defer = $q.defer();
						signupService.isEmailTaken(modelValue)
							.then(function (data) {
								if (data === "false") {
									defer.resolve("Available");
								} else {
									defer.reject("Taken");
								}
							}, function (error) {
								defer.reject("Error");
							});
						
						return defer.promise;
					};
				}
			};
		}).directive("repeatPassword", function () {
			return {
				require: "ngModel",
				link: function (scope, element, attrs, ctrl) {
					ctrl.$validators.match = function (modelValue, viewValue) {
						return scope.signupForm.password1.$viewValue === modelValue;
					};
				}
			};
		}).directive("validatableField", function () {
			return function (scope, element, attrs) {
				let form = element[0].parentElement.parentElement.name;
				let field = attrs.name;
				attrs.ngClass = `{'is-invalid':(${form}.${field}.$invalid && (${form}.${field}.$dirty || ${form}.${field}.$touched)) || ${form}.${field}.$pending.taken, 'is-valid':${form}.${field}.$valid}`;
			};
		});
}());