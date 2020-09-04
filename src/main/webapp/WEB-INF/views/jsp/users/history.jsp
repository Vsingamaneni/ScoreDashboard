<%--
  Created by IntelliJ IDEA.
  User: v0s004a
  Date: 2/11/19
  Time: 6:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>History</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="/resources/login/images/icons/cricket.ico"/>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.0/css/all.css">
    <link rel="stylesheet" href="/resources/core/css/table.css"/>
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
    <c:set var="isActivated" value="${session.isAdminActivated}"/>
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
                <%--<a href="/showReviews" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Show Reviews</a>
                <a href="/addSettlement" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Add Settlement</a>--%>
            </c:if>
            <a href="/history" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>&nbsp; Results</a>
            <a href="/standings" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bar-chart"></i>&nbsp; Standings</a>
            <%--<a href="/displaySettlement" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bar-chart"></i>&nbsp; Settlement History</a>--%>
            <a href="/showAllUsers" class="w3-bar-item w3-button w3-padding"><i class="fa fa-child"></i>&nbsp; Users</a>
            <a href="/statistics" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-pie-chart"></i>&nbsp; Stats</a>
            <%--<a href="/settlement" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bar-chart"></i>&nbsp; Settlement</a>
            <a href="/userReview" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-heart"></i>&nbsp; Review</a>
            --%><a href="/rules" style="text-decoration : none;" class="w3-bar-item w3-button w3-padding"><i class="fa fa-legal"></i>&nbsp; Rules</a>
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
    <c:if test="${not empty msg}">
        <div class="alert alert-${css} alert-dismissible" style="text-align:center;color:#204d74;" role="alert">
            <h4><strong>${msg}</strong></h4>
        </div>
    </c:if>


    <div class="w3-panel">
        <div class="w3-row-padding" style="margin:0 auto">
            <div style="width:90%">
                <br /><br />
                <h1 style="text-align: center;">Hello ${user_name}, Below is your summary ..! </h1>
                <br />

                <table class="w3-table w3-striped w3-white" style="text-align: center; align:center; align-content: center">
                    <thead>
                    <tr>
                        <th></th>
                        <th>#</th>
                        <th>Fixture</th>
                        <th>Selected</th>
                        <th>Winner</th>
                        <th>Won</th>
                        <th>Lost</th>
                        <th>Net</th>
                    </tr>
                    </thead>

                    <c:if test="${not empty standingsList}">
                        <c:forEach var="standings" items="${standingsList}">
                            <tr style="color:black;font-size:20px;text-decoration:none;font-family:Comic Sans MS">
                                <c:choose>
                                    <c:when test="${standings.result.equalsIgnoreCase('win')}">
                                        <td style="text-align:left;"> <a href="#" style="text-decoration : none;font-size:20px;color:green;"  class="w3-bar-item w3-button w3-padding"><i class="fa fa-thumbs-up"></i></a></td>
                                    </c:when>
                                    <c:when test="${standings.result.equalsIgnoreCase('loss')}">
                                        <td style="text-align:left;"> <a href="#" style="text-decoration : none;font-size:20px;color:red;"  class="w3-bar-item w3-button w3-padding"><i class="fa fa-thumbs-down"></i></a></td>
                                    </c:when>
                                    <c:when test="${standings.result.equalsIgnoreCase('draw')}">
                                        <td style="text-align:left;"> <a href="#" style="text-decoration : none;font-size:20px;color:blue;"  class="w3-bar-item w3-button w3-padding"><i class="fa fa-hand-grab-o"></i></a></td>
                                    </c:when>
                                </c:choose>
                                <td style="text-align:left;"> ${standings.matchNumber}</td>
                                <td style="text-align:left;"> ${standings.homeTeam} vs ${standings.awayTeam}</td>
                                <td style="text-align:left;"> ${standings.selected}</td>
                                <td style="text-align:left;"> ${standings.winner}</td>
                                <td style="text-align:left;">${standings.wonAmount}</td>
                                <td style="text-align:left;">${standings.lostAmount}</td>
                                <td style="text-align:left;">${standings.netAmount} </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <br /><br /><br />
                <hr>
            </div>
        </div>
    </div>


    <hr>

    <br>

    <!-- Footer -->
    <footer class="w3-container w3-padding-16 w3-light-grey">
        <p>&copy; All rights Reserved @<b>Vamsi Krishna Singamaneni</b></p>
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