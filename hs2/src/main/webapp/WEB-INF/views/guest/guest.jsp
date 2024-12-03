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

.guest-form textarea { width: 100%; height: 75px; resize: none; }

.item-delete, .item-notify { cursor: pointer; padding-left: 5px; }
.item-delete:hover, .item-notify:hover { font-weight: 500; color: #2478FF; }

textarea::placeholder{
	opacity: 1; /* 파이어폭스에서 뿌옇게 나오는 현상 제거 */
	color: #333;
	text-align: center;
	line-height: 60px;
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
				<h3><i class="bi bi-pencil-square"></i> 방명록 </h3>
			</div>
			
			<div class="body-main">
				
				<form name="guestForm" method="post">
					<div class="guest-form border border-secondary mt-5 p-3">
						<div class="p-1">
							<span class="fw-bold">방명록쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
						</div>
						<div class="p-1">
							<textarea name="content" id="content" class="form-control" placeholder="${empty sessionScope.member ? '로그인 후 등록 가능합니다.':''}"></textarea>
						</div>
						<div class="p-1 text-end">
							<button type="button" class="btnSend btn btn-dark" 
								${empty sessionScope.member ? "disabled":""}> 등록하기 <i class="bi bi-check2"></i> </button>
						</div>
					</div>
				</form>


				<div class="mt-4 mb-1 p-3 wrap-inner">
					<div class="row py-2">
						<div class="col ps-0 fw-bold text-primary item-count">방명록 0개</div>
					</div>
					
					<div class="list-content" data-pageNo="0" data-totalPage="0"></div>

					<div class="list-footer mt-2 text-end">
						<span class="more-btn btn btn-light">&nbsp;더보기&nbsp;<i class="bi bi-chevron-down"></i>&nbsp;</span>
					</div>
				</div>

			</div>
		</div>
	</div>
</main>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file=false) {
	const settings = {
			type: method,
			data: formData,
			dataType: dataType,
			success: function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);				
			},
			complete:function() {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					login();
					return false;
				} else if(jqXHR.status === 406) {
					alert('요청 처리가 실패했습니다.');
					return false;
				}
				
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false; // 파일 전송시 필수. 서버로 보낼 데이터를 쿼리문자열로 변환 여부
		settings.contentType = false; // 파일 전송시 필수. contentType. 기본은 application/x-www-urlencoded
	}
	
	$.ajax(url, settings);
}


// -- 방명록 등록
$(function(){
	$('.btnSend').click(function(){
		let content = $('#content').val().trim();
		
		if(! content) {
			$('#content').focus();
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/guest/insert';
		let formData = {content: content}; // 객체로 처리할때는 인코딩하면 안됨
		
		const fn = function(data) {
			alert(data.state);
		};
		
		ajaxFun(url, 'post', formData, 'json', fn);
		
	});
});

</script>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>