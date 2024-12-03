<%@ page contentType="text/html; charset=UTF-8" %>
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

	<script type="text/javascript">
		function deleteOk() {
		    if(confirm('게시글을 삭제 하시 겠습니까 ? ')) {

		    }
		}
	</script>

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
				
				<table class="table board-article">
					<thead>
						<tr>
							<td colspan="2" align="center">
								제목 입니다.
							</td>
						</tr>
					</thead>
					
					<tbody>
						<tr>
							<td width="50%">
								이름 : 스프링
							</td>
							<td align="right">
								2024-12-25 | 조회 10
							</td>
						</tr>
						
						<tr>
							<td colspan="2" valign="top" height="200" style="border-bottom: none;">
								내용 입니다.
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<p class="border text-secondary my-1 p-2">
									<i class="bi bi-folder2-open"></i>
									<a href="${pageContext.request.contextPath}/">파일다운</a>
								</p>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								이전글 :
								
							</td>
						</tr>
						<tr>
							<td colspan="2">
								다음글 :
								
							</td>
						</tr>
					</tbody>
				</table>
				
				<table class="table table-borderless">
					<tr>
						<td width="50%">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/';">수정</button>
							<button type="button" class="btn btn-light" onclick="deleteOk();">삭제</button>
						</td>
						<td class="text-end">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/';">리스트</button>
						</td>
					</tr>
				</table>
				
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