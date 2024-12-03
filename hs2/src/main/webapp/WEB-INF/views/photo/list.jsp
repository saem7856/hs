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

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
<style type="text/css">
.item {cursor: pointer; }
.item img { display: block; width: 100%; height: 200px; border-radius: 5px; }
.item img:hover { scale: 100.7%; }
.item .item-title {
	font-size: 14px;
	font-weight: 500;
	padding: 10px 2px 0;
	
	width: 175px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
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
			<div class="body-title">
				<h3><i class="bi bi-image"></i> 포토 갤러리 </h3>
			</div>
			
			<div class="body-main">
		        <div class="row mb-2 list-header">
		            <div class="col-auto me-auto">
		            	<p class="form-control-plaintext">
		            		15개(1/2 페이지)
		            	</p>
		            </div>
		            <div class="col-auto">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/photo/write';">사진올리기</button>
					</div>
		        </div>				
				
				 <div class="row row-cols-4 px-1 py-1 g-2">

				 </div>
				
				<div class="page-navigation">
					1 2 3
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