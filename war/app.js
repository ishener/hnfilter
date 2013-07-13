
var app = angular.module("hnfilter", ['LocalStorageModule','infinite-scroll']).config(function($httpProvider){
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
});

app.filter('filterThem', function () {
    return function (items, filters) {
    	if (filters.length == 0) return items;
    	for (var j=0; j < filters.length; j++) 
    		filters[j][1] = 0; 
    	var filtered = [];
    	var filteredFound, filteredFoundInWord;
        for (var i=0; i < items.length; i++) { // loop 1: iterate items
        	filteredFound = 0;
        	for (var j=0; j < filters.length; j++) { // loop 2: iterate filter words
        		filteredFoundInWord = false;
        		for ( var k = 0; k < items[i].words.length; k++) { // loop 3: iterate words in item
					if ( filters[j][0].toLowerCase() == sanitizeWord(items[i].words[k]).toLowerCase() )
						filteredFoundInWord = true;
				}
            	if ( filteredFoundInWord ) {
            		filteredFound++;
            		filters[j][1]++;
            	}
            }
        	if (filteredFound == 0)
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
		$scope.filtered.push([sanitizeWord(word), 0]);
		return false;
	};
	
	$scope.loadItems = function(url) {
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
			  }
		  });
	}
	
	$scope.$watch('filtered', function() {
		localStorageService.add('hnfilter',$scope.filtered);
	}, true);
	
	$scope.loadItems('/items');
});


app.directive('ngRightClick', function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
});


function sanitizeWord(word) {
	return word.replace(/(^[^a-z0-9\s]*)|([^a-z0-9\s]*$)|(('s)$)/gi, '')
}