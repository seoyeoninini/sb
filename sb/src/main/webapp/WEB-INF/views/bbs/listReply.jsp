<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class='reply-info'>
	<span class='reply-count'>댓글 7개</span>
	<span>[목록, 1/2 페이지]</span>
</div>

<table class='table table-borderless'>

		<tr class='border table-light'>
			<td width='50%'>
				<div class='row reply-writer'>
					<div class='col-1'><i class='bi bi-person-circle text-muted icon'></i></div>
					<div class='col-auto align-self-center'>
						<div class='name'>이자바</div>
						<div class='date'>2022-05-01</div>
					</div>
				</div>
			</td>
			<td width='50%' align='right' class='align-middle'>
				<span class='reply-dropdown'><i class='bi bi-three-dots-vertical'></i></span>
				<div class="reply-menu">
							<div class='deleteReply reply-menu-item' data-replyNum='3' data-pageNo='1'>삭제</div>
							<div class='hideReply reply-menu-item'>숨김</div>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top'>댓글 내용 입니다.</td>
		</tr>

		<tr>
			<td>
				<button type='button' class='btn btn-light btnReplyAnswerLayout' data-replyNum='3'>답글 <span id="answerCount3">2</span></button>
			</td>
			<td align='right'>
				<button type='button' class='btn btn-light btnSendReplyLike' data-replyNum='3' data-replyLike='1' title="좋아요"><i class="bi bi-hand-thumbs-up"></i> <span>2</span></button>
				<button type='button' class='btn btn-light btnSendReplyLike' data-replyNum='3' data-replyLike='0' title="싫어요"><i class="bi bi-hand-thumbs-down"></i> <span>1</span></button>	        
			</td>
		</tr>
	
	    <tr class='reply-answer'>
	        <td colspan='2'>
	        	<div class='border rounded'>
		            <div id='listReplyAnswer3' class='answer-list'></div>
		            <div>
		                <textarea class="form-control m-2"></textarea>
		            </div>
					<div class='text-end pe-2 pb-1'>
						<button type='button' class='btn btn-light btnSendReplyAnswer' data-replyNum='3'>답글 등록</button>
		            </div>
	            </div>
			</td>
	    </tr>

</table>

<div class="page-navigation">
	1 2 3
</div>			
