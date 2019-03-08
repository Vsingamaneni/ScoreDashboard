<%--
  Created by IntelliJ IDEA.
  User: v0s004a
  Date: 1/25/19
  Time: 4:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reset</title>
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
    <spring:url value="/resetPassword" var="resetPasswordUrl" />

</head>
<body>

<div class="limiter">
    <div class="container-login100" style="background-image: url('/resources/login/images/eden.jpg');">
        <div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Reset Your Account
				</span>
            <form action="/resetPassword" modelAttribute="registerForm" method="POST" class="login100-form validate-form p-b-33 p-t-5" role='form'>
                <c:if test="${not empty loginErrorDetails}">
                    <h2 style="color:red;font-size:15px;text-decoration:none;text-align: center;font-family:Comic Sans MS"> Please fix the below errors..!!</h2>
                </c:if>
                <c:forEach var="errorDetails" items="${errorDetailsList}">
                    <c:if test="${not empty errorDetails.errorMessage}" >
                        <h2 style="color:red;font-size:15px;text-decoration:none;text-align: center;font-family:Comic Sans MS"> *** ${errorDetails.errorMessage} </h2>
                    </c:if>
                </c:forEach>

                <div class="wrap-input100 validate-input" data-validate = "Enter EmailID">
                    <input class="input100" type="text" placeholder="Email Id" name="emailId">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Confirm EmailId">
                    <input class="input100" type="text" placeholder="Confirm Email" name="confirmEmailId">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="container-login100-form-btn m-t-32">
                    <button class="login100-form-btn" type='submit' onclick="post('${resetPasswordUrl}')">Retrieve</button>
                    &nbsp;<button class="login100-form-btn" type='submit' formaction="/">Cancel</button>
                </div>

            </form>
        </div>
    </div>
</div>

</body>
</html>

