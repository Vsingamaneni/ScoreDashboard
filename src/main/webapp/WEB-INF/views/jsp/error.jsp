<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<meta http-equiv="Refresh" content="5;/">
<jsp:include page="fragments/header.jsp" />

<body>
<c:if test="${not empty session}">
	<c:set var="user_name" value="${session.firstName}"/>
	<c:set var="role" value="${session.role}"/>
	<c:set var="isActivated" value="${session.isAdminActivated}"/>
</c:if>

<div class="w3-main" style="margin-left:300px;margin-top:43px;">
	<h1>Error Page</h1>

	<div class="w3-row-padding w3-margin-bottom">
		<div class="w3-container w3-red w3-padding-16">
			<div class="w3-left"><i class="fa fa-comment w3-xxxlarge"></i></div>
			<div class="w3-right">
			</div>
			<div class="w3-clear"></div>
			<h4>Hello ${user_name},There is an error processing your request, please retry !!</h4>
			<h4>If the error persist, please contact the admin !!</h4>
		</div>
	</div>

</div>

<jsp:include page="fragments/footer.jsp" />

</body>
</html>