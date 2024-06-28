<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="row board-list-header">
    <div class="col-auto me-auto">
    	${dataCount}개(${pageNo}/${total_page} 페이지)
    </div>
    <div class="col-auto">&nbsp;</div>
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
				<td>${dataCount - (pageNo-1) * size - status.index}</td>
				<td class="left">
					<a href="javascript:articleLecture('${dto.num}', '${pageNo}');" class="text-reset">${dto.title}</a>
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
		<button type="button" class="btn btn-light" onclick="reloadLecture();" title="새로고침"><i class="bi bi-arrow-counterclockwise"></i></button>
	</div>
	<div class="col-6 text-center">
		<div class="row">
			<div class="col-auto p-1">
				<select name="schType" id="schType" class="form-select">
					<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
					<option value="userName" ${schType=="userName"?"selected":""}>작성자</option>
					<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
					<option value="title" ${schType=="title"?"selected":""}>제목</option>
					<option value="content" ${schType=="content"?"selected":""}>내용</option>
				</select>
			</div>
			<div class="col-auto p-1">
				<input type="text" name="kwd" id="kwd" value="${kwd}" class="form-control">
			</div>
			<div class="col-auto p-1">
				<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
			</div>
		</div>
	</div>
	<div class="col text-end">
		<c:if test="${sessionScope.member.membership>30}">
			<button type="button" class="btn btn-light" onclick="insertForm();">강좌등록</button>
		</c:if>
	</div>
</div>
