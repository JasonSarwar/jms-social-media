(function(){
	var convertDate = function (input) {
		var year = input.year;
		var month = input.month - 1;
		var day = input.day;
		var date = new Date(year, month, day);
		return date;
	};

	var convertTime = function (input) {
		var hour = input.hour;
		var minute = input.minute;
		var date = new Date(0, 0, 0, hour, minute);
		return date;
	};
  
	var dateForLink = function ($filter) {
		return function (input) {
			var date = convertDate(input);
			return $filter('date')(date, "MM-dd-yyyy");
		};
	};

	var dateForEntry = function ($filter) {
		return function (input) {
			var date = convertDate(input);
			return $filter('date')(date, "MMM dd");
		};
	};

	var dateWithMonthYear = function ($filter) {
		return function (input) {
			var date = convertDate(input.date);
			return $filter('date')(date, "MMM yyyy");
		};
	};
	  
	var timeForEntry = function($filter) {
		return function (input) {
			var date = convertTime(input);
			return $filter('date')(date, "shortTime");
		};
	};
	
	var entryText = function() {
		return function(postText) {
			var text = postText.replace(/(?<=\s|^)#([\w_-]*)(?=\s|$)/g, "<a href='#!/posts?tag=\$1'>\$&</a>")
								.replace(/(?<=\s|^)https?:\/\/(\S+\.\S+)/g, "<a href='$&'>\$1</a>")
								.replace(/(?<=\s|^)www\.\S+\.\S+/g, "<a href='http://$&'>\$&</a>")
								.replace(/(?<=\s|^)\S+\.[a-zA-Z]{2,3}(?=\s|$)/g, "<a href='http://$&'>\$&</a>");
			return text;
		};
	};
	
	angular.module("mysocialmedia")
	  	.filter("dateForLink", dateForLink)
	  	.filter("dateForEntry", dateForEntry)
	  	.filter("timeForEntry", timeForEntry)
	  	.filter("dateWithMonthYear", dateWithMonthYear)
	  	.filter("entryText", entryText);
}());