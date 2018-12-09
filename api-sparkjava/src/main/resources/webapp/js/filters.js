(function(){
  var convertDate = function(input) {
	var year = input.year;
	var month = input.month - 1;
	var day = input.day;
	var date = new Date(year, month, day);
	return date;
  };

  var convertTime = function(input) {
	var hour = input.hour;
	var minute = input.minute;
	var date = new Date(0, 0, 0, hour, minute);
	return date;
  };
  
  var dateForLink = function($filter) {
	return function(input) {
		var date = convertDate(input);
		return $filter('date')(date, "MM-dd-yyyy");
	};
  };  

  var dateForPost = function($filter) {
	return function(input) {
		var date = convertDate(input);
		return $filter('date')(date, "MMM dd");
	};
  };

  var timeForPost = function($filter) {
	return function(input) {
		var date = convertTime(input);
		return $filter('date')(date, "shortTime");
	};
  };

  var postText = function() {
	return function(postText) {
		var text = postText.replace(/#(\S+)/g, "<a href='#!/posts?tag=\$1'>\$&</a>")
							.replace(/https?:\/\/(\S+)/g, "<a href='$&'>\$&</a>");
		return text;
	};
  };

  angular.module("mytwitter")
  	.filter("dateForLink", dateForLink)
  	.filter("dateForPost", dateForPost)
  	.filter("timeForPost", timeForPost)
  	.filter("postText", postText);
}());