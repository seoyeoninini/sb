<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<script type="text/javascript">
function searchList() {
	const f = document.searchForm;
	f.submit();
}

function changeList() {
	const f = document.boardListForm;
	f.submit();
}
</script>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-subtract"></i> 답변형 게시판 </h3>
		</div>
		
		<div class="body-main">

	        <div class="row board-list-header">
	            <div class="col-auto me-auto align-self-end">
	            	${dataCount}개(${page}/${total_page} 페이지)
	            </div>
	            <div class="col-auto">
	            	<form action="${pageContext.request.contextPath}/replyBoard/list" name="boardListForm" method="post">
						<c:if test="${dataCount!=0 }">
							<select name="size" class="form-select" onchange="changeList();">
								<option value="10" ${size==10 ? "selected ":""}>10개씩 출력</option>
								<option value="20" ${size==20 ? "selected ":""}>20개씩 출력</option>
								<option value="30" ${size==30 ? "selected ":""}>30개씩 출력</option>
								<option value="40" ${size==40 ? "selected ":""}>40개씩 출력</option>
								<option value="50" ${size==50 ? "selected ":""}>50개씩 출력</option>
							</select>
						</c:if>
						<input type="hidden" name="schType" value="${schType}">
						<input type="hidden" name="kwd" value="${kwd}">
					</form>
				</div>
	        </div>
			
			<table class="table table-hover board-list">
				<thead class="table-light">
					<tr>
						<th width="60">번호</th>
						<th>제목</th>
						<th width="100">작성자</th>
						<th width="100">작성일</th>
						<th width="70">조회수</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="dto" items="${list}" varStatus="status">
						<tr>
							<td>${dataCount - (page-1) * size - status.index}</td>
							<td class="left">
								<c:forEach var="n" begin="1" end="${dto.depth}">&nbsp;&nbsp;</c:forEach>
								<c:if test="${dto.depth!=0}">└&nbsp;</c:if>
								<a href="${articleUrl}&boardNum=${dto.boardNum}" class="text-reset">${dto.subject}</a>
								<c:if test="${dto.gap < 1}">
									<span class="badge text-bg-info">New</span>
								</c:if>
							</td>
							<td>${dto.userName}</td>
							<td>${dto.reg_date}</td>
							<td>${dto.hitCount}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="page-navigation">
				${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
			</div>

			<div class="row board-list-footer">
				<div class="col">
					<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/replyBoard/list';" title="새로고침"><i class="bi bi-arrow-counterclockwise"></i></button>
				</div>
				<div class="col-6 text-center">
					<form class="row" name="searchForm" action="${pageContext.request.contextPath}/replyBoard/list" method="post">
						<div class="col-auto p-1">
							<select name="schType" class="form-select">
								<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
								<option value="userName" ${schType=="userName"?"selected":""}>작성자</option>
								<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
								<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
								<option value="content" ${schType=="content"?"selected":""}>내용</option>
							</select>
						</div>
						<div class="col-auto p-1">
							<input type="text" name="kwd" value="${kwd}" class="form-control">
							<input type="hidden" name="size" value="${size}">
						</div>
						<div class="col-auto p-1">
							<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
						</div>
					</form>
				</div>
				<div class="col text-end">
					<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/replyBoard/write';">글올리기</button>
				</div>
			</div>

		</div>
	</div>
</div>