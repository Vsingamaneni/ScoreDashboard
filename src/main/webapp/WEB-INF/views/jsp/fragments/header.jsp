<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
<<title>Score Finder</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<link rel="shortcut icon" href="../images/cricket.ico" />
	<style>
		html,body,h1,h2,h3,h4,h5 {font-family: "Raleway", sans-serif}
	</style>
</head>

<body class="w3-light-grey">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">
	<button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onclick="w3_open();"><i class="fa fa-bars"></i> &nbsp;Menu</button>
	<span class="w3-bar-item w3-right">Score Finder</span>
</div>

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
	<div class="w3-container w3-row">
		<div class="w3-col s4">
			<img src="/resources/core/images/avatar2.png" class="w3-circle w3-margin-right" style="width:46px">
		</div>
		<div class="w3-col s8 w3-bar">
			<span>Welcome, <strong>User</strong></span><br>
			<a href="#" class="w3-bar-item w3-button"><i class="fa fa-home"></i></a>
			<a href="#" class="w3-bar-item w3-button"><i class="fa fa-lock"></i></a>
			<a href="#" class="w3-bar-item w3-button"><i class="fa fa-user-plus"></i></a>
		</div>
	</div>
	<hr>
	<div class="w3-container">
		<h5>Dashboard</h5>
	</div>
	<div class="w3-bar-block">
		<a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onclick="w3_close()" title="close menu"><i class="fa fa-remove fa-fw"></i>&nbsp; Close Menu</a>
		<a href="#" class="w3-bar-item w3-button w3-padding w3-blue"><i class="fa fa-users fa-fw"></i>&nbsp; Home</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bell fa-fw"></i>&nbsp; Schedule</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-users fa-fw"></i>&nbsp; Results</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-lock fa-fw"></i>&nbsp; login</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-user-plus fa-fw"></i>&nbsp; Register</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>&nbsp; History</a>
		<a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bank fa-fw"></i>&nbsp; Standings</a>
	</div>
</nav>