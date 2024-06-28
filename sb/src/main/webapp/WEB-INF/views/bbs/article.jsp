<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<c:if test="${sessionScope.member.membership>50 || dto.userId == sessionScope.member.userId}">
	<script type="text/javascript">
	function deleteBoard() {
		if(confirm("게시글을 삭제하시겠습니까 ? ")) {
			let url = "${pageContext.request.contextPath}/bbs/delete";
			let query = "num=${dto.num}&${query}";
			location.href = url + "?" + query;
		}
	}
	</script>
</c:if>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-app"></i> 게시판 </h3>
		</div>
		
		<div class="body-main">

			<table class="table mt-5 mb-0 board-article">
				<thead>
					<tr>
						<td colspan="2" align="center">
							${dto.subject}
						</td>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td width="50%">
							이름 : ${dto.userName}
						</td>
						<td align="right">
							${dto.reg_date} | 조회 ${dto.hitCount}
						</td>
					</tr>
					
					<tr>
						<td colspan="2" valign="top" height="200" style="border-bottom: none;">
							${dto.content}
						</td>
					</tr>
					
					<tr>
						<td colspan="2" class="text-center p-3" style="border-bottom: none;">
							<button type="button" class="btn btn-outline-secondary btnSendBoardLike" title="좋아요">
								<i class="bi ${userBoardLiked ? 'bi-hand-thumbs-up-fill':'bi-hand-thumbs-up'}"></i>&nbsp;&nbsp;<span id="boardLikeCount">${dto.boardLikeCount}</span>
							</button>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<c:if test="${not empty dto.saveFilename}">
								<p class="border text-secondary my-1 p-2">
									<i class="bi bi-folder2-open"></i>
									<a href="${pageContext.request.contextPath}/bbs/download?num=${dto.num}">${dto.originalFilename}</a>
								</p>
							</c:if>
						</td>
					</tr>

					<tr>
						<td colspan="2">
							이전글 :
							<c:if test="${not empty prevDto}">
								<a href="${pageContext.request.contextPath}/bbs/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							다음글 :
							<c:if test="${not empty nextDto}">
								<a href="${pageContext.request.contextPath}/bbs/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
							</c:if>
						</td>
					</tr>

				</tbody>
			</table>
			
			<table class="table table-borderless">
				<tr>
					<td width="50%">
						<c:choose>
							<c:when test="${sessionScope.member.userId==dto.userId}">
								<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/bbs/update?num=${dto.num}&page=${page}';">수정</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn btn-light" disabled>수정</button>
							</c:otherwise>
						</c:choose>
						
						<c:choose>
							<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.membership>50}">
								<button type="button" class="btn btn-light" onclick="deleteBoard();">삭제</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn btn-light" disabled>삭제</button>
							</c:otherwise>
						</c:choose>
				    			
					</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/bbs/list?${query}';">리스트</button>
					</td>
				</tr>
			</table>
			
			<div class="reply">
				<form name="replyForm" method="post">
					<div class='form-header'>
						<span class="bold">댓글</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
					</div>
					
					<table class="table table-borderless reply-form">
						<tr>
							<td>
								<textarea class='form-control' name="content"></textarea>
							</td>
						</tr>
						<tr>
						   <td align='right'>
						        <button type='button' class='btn btn-light btnSendReply'>댓글 등록</button>
						    </td>
						 </tr>
					</table>
				</form>
				
				<div id="listReply"></div>
			</div>
			
		</div>
	</div>
</div>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file = false) {
	const settings = {
			type: method, 
			data: formData,
			dataType:dataType,
			success:function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function () {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					login();
					return false;
				} else if(jqXHR.status === 400) {
					alert('요청 처리가 실패 했습니다.');
					return false;
		    	}
		    	
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false;  // file 전송시 필수. 서버로전송할 데이터를 쿼리문자열로 변환여부
		settings.contentType = false;  // file 전송시 필수. 서버에전송할 데이터의 Content-Type. 기본:application/x-www-urlencoded
	}
	
	$.ajax(url, settings);
}

// 게시글 공감
$(function(){
	$(".btnSendBoardLike").click(function(){
		const $i = $(this).find("i");
		let userLiked = $i.hasClass("bi-hand-thumbs-up-fill");
		let msg = userLiked ? "게시글 공감을 취소하시겠습니까 ? " : "게시글에 동감하시겠습니까 ?";
		
		if(! confirm(msg)) {
			return false;
		}
		
		let url = "${pageContext.request.contextPath}/bbs/insertBoardLike";
		let num = "${dto.num}";
		let query = "num=" + num + "&userLiked=" + userLiked;
		
		const fn = function(data) {
			let state = data.state;
			if(state === "true") {
				if(userLiked) {
					$i.removeClass("bi-hand-thumbs-up-fill")
						.addClass("bi-hand-thumbs-up")
				} else {
					$i.removeClass("bi-hand-thumbs-up")
					.addClass("bi-hand-thumbs-up-fill")
				}
				
				let count = data.boardLikeCount;
				$("#boardLikeCount").text(count);
			} else if(state === "liked") {
				alert("게시글 공감은 한번만 가능합니다.");
			} else {
				alert("게시글 공감 처리가 실패했습니다.");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
		
	});
});

</script>

