<!DOCTYPE html>
<html ng-app="hnfilter">
	<head>
		<meta charset='utf-8'> 
		<title>HN Filter</title>
		<meta name="description" content="HN Filter">
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700,600' rel='stylesheet' type='text/css'>
		<link href='style.css' rel='stylesheet' type='text/css'>
		
	</head>
	
	<body ng-controller="MainCtrl">
	<div  id="wrapper">
		<h1>HN Filter <span> - filter HN articles by right-clicking a word in the title</span></h1>
		<form method="post">
			<input type="text" style="width: 500px" name="title" value="${item.title}" />
			<input type="submit" />
		</form>
	</div>
	</body>
</html>