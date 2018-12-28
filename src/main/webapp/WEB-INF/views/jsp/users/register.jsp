<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: v0s004a
  Date: 12/27/18
  Time: 8:27 PM
  To change this template use File | Settings | File Templates.
--%>
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

    <spring:url value="/registerUser" var="registerUrl" />
</head>
<body>

<div class="limiter">
    <div class="container-login100" style="background-image: url('/resources/login/images/eden.jpg');">
        <div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Get In
				</span>
            <form class="login100-form validate-form p-b-33 p-t-5" action="/registerUser" modelAttribute="registerForm" method="POST">

                <c:if test="${fn:length(registerErrorDetails) > 0}">
                    <h2 style="color:red;font-size:15px;text-decoration:none; text-align: center; font-family:Comic Sans MS"> Please fix the below error(s)..!!</h2>
                </c:if>
                <c:forEach var="registerErrorDetail" items="${registerErrorDetails}">
                    <c:if test="${not empty registerErrorDetail.errorMessage}" >
                        <h2 style="color:red;font-size:15px;text-decoration:none;text-align: center; font-family:Comic Sans MS"> *** ${registerErrorDetail.errorMessage} </h2>
                    </c:if>
                </c:forEach>

                <div class="wrap-input100 validate-input" data-validate = "Email">
                    <input class="input100" type="text" placeholder="Email" name="emailId">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate = "Confirm Email">
                    <input class="input100" type="text" placeholder="Confirm Email" name="confirmEmailId">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Enter password">
                    <input class="input100" type="password" placeholder="Password" name="password">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Confirm password">
                    <input class="input100" type="password" placeholder="Confirm Password" name="confirmPassword">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="First Name">
                    <input class="input100" type="text" placeholder="First Name" name="fName">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Last Name">
                    <input class="input100" type="text" placeholder="Last Name" name="lName">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Country">
                    <div align="center">
                        <select style="text-align: center; text-align-last: center;" class='input50' name="country">
                            <option>Select Country</option>
                            <option>India</option>
                            <option>United States</option>
                            <option>England</option>
                            <option>Other</option>
                        </select>
                    </div>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Security Question">
                    <div align="center">
                        <select style="text-align: center; text-align-last: center;" class='input50' name="securityQuestion">
                            <option> Security Question </option>
                            <option>What is your best friend name?</option>
                            <option>Who is your favourite Cricketer</option>
                            <option>What is the name of your first pet?</option>
                        </select>
                    </div>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Security Answer">
                    <input class="input100" type="password" placeholder="Security Answer" name="securityAnswer">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Secret Key">
                    <input class="input100" type="password" placeholder="Secret Key" name="security">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>

                <div class="container-login100-form-btn m-t-32">
                    <button class="login100-form-btn" type='submit' onclick="post('${registerUrl}')">Register</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>

