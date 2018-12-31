<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<meta http-equiv="Refresh" content="2;/">
<jsp:include page="fragments/header.jsp" />

<body>

<div class="w3-main" style="margin-left:300px;margin-top:43px;">
	<h1>Error Page</h1>

	<p>${exception.message}</p>
	<!-- Exception: ${exception.message}.
		  	<c:forEach items="${exception.stackTrace}" var="stackTrace">
				${stackTrace}
			</c:forEach>
	  	-->
</div>

<jsp:include page="fragments/footer.jsp" />

</body>
</html>