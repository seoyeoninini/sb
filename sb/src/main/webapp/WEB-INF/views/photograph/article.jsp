<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div style="max-width: 800px;">
	<table class="table mt-3 mb-0 board-article">
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
					${dto.reg_date}
				</td>
			</tr>
	
			<tr>
				<td colspan="2" style="border-bottom: none;">
					<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" 
						class="img-fluid img-thumbnail w-100 h-auto">
				</td>
			</tr>
									
			<tr>
				<td colspan="2">
					${dto.content}
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					이전글 :
					<c:if test="${not empty prevDto}">
						<span class="item-view" data-num="${prevDto.num}">${prevDto.subject}</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					다음글 :
					<c:if test="${not empty nextDto}">
						<span class="item-view" data-num="${nextDto.num}">${nextDto.subject}</span>
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
						<button type="button" class="btn btn-light btnUpdateForm" data-num="${dto.num}">수정</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-light" disabled>수정</button>
					</c:otherwise>
				</c:choose>
		    	
				<c:choose>
		    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.membership>50}">
		    			<button type="button" class="btn btn-light btnDeleteOk" data-num="${dto.num}" 
		    					data-imageFilename="${dto.imageFilename}">삭제</button>
		    		</c:when>
		    		<c:otherwise>
		    			<button type="button" class="btn btn-light" disabled>삭제</button>
		    		</c:otherwise>
		    	</c:choose>
			</td>
			<td class="text-end">
				<button type="button" class="btn btn-light btnModalClose">닫기</button>
			</td>
		</tr>
	</table>
</div>