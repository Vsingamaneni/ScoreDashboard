<%--
  Created by IntelliJ IDEA.
  User: v0s004a
  Date: 12/28/18
  Time: 11:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contact Admin</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="icon" type="image/png" href="/resources/login/images/icons/cricket.ico"/>
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

<c:if test="${not empty session}">
    <c:set var="user_name" value="${session.firstName}"/>
    <c:set var="role" value="${session.role}"/>
</c:if>

<c:if test="${empty session}">
    <c:set var="user_name" value="User"/>
</c:if>

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s4">
            <img src="/resources/core/images/avatar2.png" class="w3-circle w3-margin-right" style="width:46px">
        </div>
        <div class="w3-col s8 w3-bar">
            <span>Welcome, <strong>${user_name}</strong></span><br>
            <a href="/" class="w3-bar-item w3-button"><i class="fa fa-home"></i></a>
            <a href="/schedule" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bell fa-fw"></i></a>
            <a href="/logout" style="text-decoration : none;" style="text-decoration : none;"class="w3-bar-item w3-button w3-padding"><i class="fa fa-power-off"></i></a>
        </div>
    </div>
    <hr>
    <div class="w3-container">
        <h5>Dashboard</h5>
    </div>
    <div class="w3-bar-block">
        <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onclick="w3_close()" title="close menu"><i class="fa fa-remove fa-fw"></i>&nbsp; Close Menu</a>
        <c:if test="${ not user_name.equalsIgnoreCase('user')}">
            <a href="/" class="w3-bar-item w3-button w3-padding"><i class="fa fa-home fa-fw"></i>&nbsp; Home</a>
            <a href="/account" class="w3-bar-item w3-button w3-padding"><i class="fa fa-hand-o-right"></i>&nbsp; Account</a>
            <a href="/predictions" class="w3-bar-item w3-button w3-padding"><i class="fa fa-plus"></i>&nbsp; Predictions</a>
            <a href="/currentPredictions" class="w3-bar-item w3-button w3-padding"><i class="fa fa-clock-o"></i>&nbsp; MatchDay Predictions</a>
            <c:if test="${role.equalsIgnoreCase('admin')}">
                <a href="/saveResult" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Update Result</a>
                <a href="/results" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Quota</a>
<%--                <a href="/showReviews" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Show Reviews</a>--%>
            </c:if>
            <a href="/history" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>&nbsp; Results</a>
            <a href="/standings" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bar-chart"></i>&nbsp; Standings</a>
            <a href="/showAllUsers" class="w3-bar-item w3-button w3-padding"><i class="fa fa-child"></i>&nbsp; Users</a>
            <a href="/statistics" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-pie-chart"></i>&nbsp; Stats</a>
            <%--<a href="/userReview" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-heart"></i>&nbsp; Review</a>--%>
            <a href="/rules" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Rules</a>
            <a href="/logout" class="w3-bar-item w3-button w3-padding"><i class="fa fa-power-off"></i>&nbsp; Logout</a>
        </c:if>
        <c:if test="${user_name.equalsIgnoreCase('user')}">
            <a href="/" class="w3-bar-item w3-button w3-padding"><i class="fa fa-power-off"></i>&nbsp; Login</a>
        </c:if>
    </div>
</nav>
<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

    <!-- Header -->
    <header class="w3-container" style="padding-top:22px">
        <h5><b><i class="fa fa-dashboard"></i> My Dashboard</b></h5>
    </header>

    <div class="w3-row-padding w3-margin-bottom">
            <div class="w3-container w3-red w3-padding-16">
                <div class="w3-left"><i class="fa fa-comment w3-xxxlarge"></i></div>
                <div class="w3-right">
                </div>
                <div class="w3-clear"></div>
                <h4>Hello ${user_name}, Please Contact Admin to activate your account !!</h4>
            </div>
    </div>

    <div class="w3-row-padding w3-margin-bottom">
        <div class="w3-container w3-blue w3-text-white w3-padding-16">
            <div class="w3-left"><i class="fa fa-eye w3-xxxlarge"></i></div>
            <div class="w3-right">
            </div>
            <div class="w3-clear"></div>
            <h4>Feel free to check the upcoming matches and sort out your preferences !!</h4>
        </div>
    </div>

    <div class="w3-row-padding w3-margin-bottom">
        <div class="w3-container w3-orange w3-text-white w3-padding-16">
            <div class="w3-left"><i class="fa fa-users w3-xxxlarge"></i></div>
            <div class="w3-right">
            </div>
            <div class="w3-clear"></div>
            <h4>You will be joining with ${session.memberId -1} other members !! </h4>
        </div>
    </div>

    <hr>
    <hr>

    <br>
    <br>
    <br>
    <br>
    <br>

    <!-- Footer -->
    <footer class="w3-container w3-padding-16 w3-light-grey">
        <p>&copy; All rights Reserved @<b>Vamsi Singamaneni</b></p>
    </footer>

    <!-- End page content -->
</div>

<script>
    // Get the Sidebar
    var mySidebar = document.getElementById("mySidebar");

    // Get the DIV with overlay effect
    var overlayBg = document.getElementById("myOverlay");

    // Toggle between showing and hiding the sidebar, and add overlay effect
    function w3_open() {
        if (mySidebar.style.display === 'block') {
            mySidebar.style.display = 'none';
            overlayBg.style.display = "none";
        } else {
            mySidebar.style.display = 'block';
            overlayBg.style.display = "block";
        }
    }

    // Close the sidebar with the close button
    function w3_close() {
        mySidebar.style.display = "none";
        overlayBg.style.display = "none";
    }
</script>

</body>
</html>