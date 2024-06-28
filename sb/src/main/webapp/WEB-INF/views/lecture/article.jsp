<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<table class="table mt-5 mb-0 board-article">
	<thead>
		<tr>
			<td colspan="2" align="center">
				${dto.title}
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
			<td colspan="2" valign="top" height="200" style="${not empty dto.youtube || listFile.size() != 0 ? 'border-bottom: none;' : ''}">
				${dto.content}
			</td>
		</tr>
		<c:if test="${not empty dto.youtube}">
			<tr>
				<td colspan="2" class="text-center p-3" style="${listFile.size() != 0 ? 'border-bottom: none;' : ''}">
					<label style="cursor: pointer;" class="fs-3" onclick="youtubePlay('${dto.youtube}');" title="유투브"><i class="bi bi-youtube"></i></label>
				</td>
			</tr>		
		</c:if>
		
		<c:if test="${listFile.size() != 0}">
			<tr>
				<td colspan="2">
					<p class="border text-secondary mb-1 p-2">
						<i class="bi bi-folder2-open"></i>
						<c:forEach var="vo" items="${listFile}" varStatus="status">
							<a href="${pageContext.request.contextPath}/lecture/attachment/download?fileNum=${vo.fileNum}" class="text-reset">${vo.originalFilename}</a>
							<c:if test="${not status.last}">|&nbsp;</c:if>
						</c:forEach>
					</p>
					<p class="border text-secondary mb-1 p-2">
						<i class="bi bi-folder2-open"></i>
						<a href="${pageContext.request.contextPath}/lecture/attachment/zipdownload?num=${dto.num}" class="text-reset" title="압축 다운로드">파일 전체 압축 다운로드(zip)</a>
					</p>
				</td>
			</tr>
		</c:if>
		
		<tr>
			<td colspan="2">
				이전글 :
				<c:if test="${not empty prevDto}">
					<a href="javascript:articleLecture('${prevDto.num}', '${pageNo}');">${prevDto.title}</a>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				다음글 :
				<c:if test="${not empty nextDto}">
					<a href="javascript:articleLecture('${nextDto.num}', '${pageNo}');">${nextDto.title}</a>
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
					<button type="button" class="btn btn-light" onclick="updateForm('${dto.num}', '${pageNo}');">수정</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-light" disabled>수정</button>
				</c:otherwise>
			</c:choose>
	    	
			<c:choose>
	    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.membership>50}">
	    			<button type="button" class="btn btn-light" onclick="deleteOk('${dto.num}', '${pageNo}');">삭제</button>
	    		</c:when>
	    		<c:otherwise>
	    			<button type="button" class="btn btn-light" disabled>삭제</button>
	    		</c:otherwise>
	    	</c:choose>
		</td>
		<td class="text-end">
			<button type="button" class="btn btn-light" onclick="listPage('${pageNo}');">리스트</button>
		</td>
	</tr>
</table>
