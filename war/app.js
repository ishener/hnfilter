var ctrlDown;

$(document).ready(function() {
    ctrlDown = false;
    var ctrlKey = 17;

    $(document).keydown(function(e) {
        if (e.keyCode == ctrlKey) ctrlDown = true;
        console.log("key down " + e.keyCode);
    }).keyup(function(e) {
        if (e.keyCode == ctrlKey) ctrlDown = false;
        console.log("key up " + e.keyCode);
    });

});

var app = angular.module("hnfilter", ['LocalStorageModule','infinite-scroll']).config(function($httpProvider){
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
});

app.filter('filterThem', function () {
    return function (items, filters) {
    	if (filters.length == 0) return items;
    	var filtered = [];
    	var filteredFound;
        for (var i=0; i < items.length; i++) {
        	filteredFound = false;
        	for (var j=0; j < filters.length; j++) {
            	if ( $.inArray(filters[j], items[i].words) !== -1 )
            		filteredFound = true
            }
        	if (!filteredFound)
        		filtered.push(items[i]);
        }
        return filtered;
    };
});

app.controller("MainCtrl", function($scope, $http, localStorageService) {
	$scope.items = [];
	$scope.filtered = localStorageService.get('hnfilter');
	$scope.cursor;
	$scope.scrollLoad = false;
	if ($scope.filtered == null) $scope.filtered = [];
	
	
	
	$scope.clicked = function(event, word) {
		if (ctrlDown) {
			if (event) event.preventDefault();
			$scope.filtered.push(word);
			return false;
		}
	};
	
	$scope.loadItems = function(url) {
		console.log("fired: " + url);
		$scope.scrollLoad = true;
		$http({method: 'GET', url: url}).
		  success(function(data, status, headers, config) {
			  if (data.items.length > 0) {
				  $scope.scrollLoad = false;
				  $scope.cursor = data.cursor;
				  for (var i=0; i < data.items.length; i++) {
					  data.items[i].words = data.items[i].title.split(' ');
				  }
				  $scope.items = $scope.items.concat(data.items);
				  console.log($scope.items);
			  }
		  });
	}
	
	$scope.$watch('filtered', function() {
		localStorageService.add('hnfilter',$scope.filtered);
	}, true);
	
	$scope.loadItems('/items');
});