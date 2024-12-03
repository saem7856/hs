﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>

</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>

<main>
	<div class="container">
		<div class="body-container">	

	        <div class="row justify-content-md-center mt-5">
	            <div class="col-md-8">
	                <div class="border rounded mt-5">
	                	<div class="border-bottom p-3">
                        	<h4 class="text-center fw-bold text-danger mb-0">경고!</h4>
                        </div>
		                <div class="pt-3">
							<p class="alert text-center lh-base fs-6" >해당 정보를 접근 할 수 있는 권한 이 없습니다.<br>메인화면으로 이동하시기 바랍니다.</p>
		                </div>
	                </div>
	            </div>
	        </div>
	        
	        <div class="row justify-content-md-center mt-1">
	        	<div class="col-md-8 d-grid">
	        		<button type="button" class="btn btn-lg btn-primary" onclick="location.href='${pageContext.request.contextPath}/';">메인화면으로 이동 <i class="bi bi-arrow-counterclockwise"></i> </button>
	        	</div>
	        </div>

		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>