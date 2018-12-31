<%--
  Created by IntelliJ IDEA.
  User: v0s004a
  Date: 12/26/18
  Time: 10:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Score Dashboard</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="/resources/login/images/icons/cricket.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/resources/login/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/resources/login/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/resources/login/css/util.css">
    <link rel="stylesheet" type="text/css" href="/resources/login/css/main.css">
    <!--===============================================================================================-->

    <spring:url value="/loginUser" var="loginUrl" />
    <spring:url value="/registerUser" var="registerUrl" />
    <spring:url value="/forgot" var="forgotUrl" />
</head>
<body>

<div class="limiter">
    <div class="container-login100" style="background-image: url('/resources/login/images/eden.jpg');">
        <div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Account Login
				</span>
            <form class="login100-form validate-form p-b-33 p-t-5" action="/loginUser" modelAttribute="userLogin" method="POST">
                <c:if test="${not empty loginErrorDetails}">
                    <h2 style="color:red;font-size:15px;text-decoration:none;text-align: center;font-family:Comic Sans MS"> Please fix the below errors..!!</h2>
                </c:if>
                <c:forEach var="loginErrorDetails" items="${loginErrorDetails}">
                    <c:if test="${not empty loginErrorDetails.errorMessage}" >
                        <h2 style="color:red;font-size:15px;text-decoration:none;text-align: center;font-family:Comic Sans MS"> *** ${loginErrorDetails.errorMessage} </h2>
                    </c:if>
                </c:forEach>

                <div class="wrap-input100 validate-input" data-validate = "Enter username">
                    <input class="input100" type="text" placeholder="User name" name="email">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Enter password">
                    <input class="input100" type="password" placeholder="Password" name="password">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="container-login100-form-btn m-t-32">
                    <button class="login100-form-btn" type='submit' onclick="post('${loginUrl}')">Login</button>
                </div>

                <div class="container-login100-form-btn m-t-32">
                    <button class="login100-form-btn">
                        Forgot ?
                    </button>
                    <button class="login100-form-btn">
                        Sign Up
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

</body>
</html>

