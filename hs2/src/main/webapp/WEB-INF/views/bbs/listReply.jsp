<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="reply-info">
	<span class="reply-count">댓글 17개</span>
	<span>[목록, 1/3 페이지]</span>
</div>

<table class="table table-borderless">

		<tr class="border table-light">
			<td width="50%">
				<div class="row reply-writer">
					<div class="col-auto align-self-center">
						<div class="name">이자바</div>
					</div>
				</div>
			</td>
			<td width="50%" align="right" class="align-middle">
				<span>2024-12-25</span> | 
				<span class="deleteReply" data-replyNum="15" data-pageNo="1">삭제</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" valign="top">내용입니다.</td>
		</tr>

		<tr>
			<td>
				<button type="button" class="btn btn-light btnReplyAnswerLayout" data-replyNum="15">답글 <span id="answerCount15">3</span></button>
			</td>
			<td align="right">
				<button type="button" class="btn btn-light btnSendReplyLike" data-replyNum="15" data-replyLike="1" title="좋아요"><i class="bi bi-hand-thumbs-up"></i> <span>7</span></button>
				<button type="button" class="btn btn-light btnSendReplyLike" data-replyNum="15" data-replyLike="0" title="싫어요"><i class="bi bi-hand-thumbs-down"></i> <span>2</span></button>	        
			</td>
		</tr>
	
		<tr class="reply-answer">
			<td colspan="2">
				<div class="border rounded">
					<div id="listReplyAnswer15" class="answer-list"></div>
					<div>
						<textarea class="form-control m-2"></textarea>
					</div>
					<div class="text-end pe-2 pb-1">
						<button type="button" class="btn btn-light btnSendReplyAnswer" data-replyNum="15">답글 등록</button>
					</div>
				</div>
			</td>
		</tr>

</table>

<div class="page-navigation">
	1 2 3
</div>			
