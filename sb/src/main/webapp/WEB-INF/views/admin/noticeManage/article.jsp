<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board.css" type="text/css">
<style type="text/css">
.body-main {
	max-width: 900px;
}

.table-article img { max-width: 650px; }
.file-item { padding: 7px; margin-bottom: 3px; border: 1px solid #ced4da; color: #777777; }
</style>

<script type="text/javascript">
	function deleteOk() {
		let query = 'num=${dto.num}&${query}';
		let url = '${pageContext.request.contextPath}/admin/noticeManage/delete?' + query;
	
		if(confirm('위 자료를 삭제 하시 겠습니까 ? ')) {
			location.href = url;
		}
	}
</script>

<div class="body-container">
    <div class="body-title">
		<h2><i class="fas fa-clipboard-list"></i> 공지사항 </h2>
    </div>
    
    <div class="body-main">

		<table class="table table-border table-article">
			<thead>
				<tr>
					<td colspan="2" align="center">
						${dto.subject}${dto.showNotice == 1 ? "" : " [숨김]" }
					</td>
				</tr>
			</thead>
			
			<tbody>
				<tr>
					<td width="50%" align="left">
						이름 : ${dto.userName}
					</td>
					<td width="50%" align="right">
						${dto.reg_date} | 조회 ${dto.hitCount}
					</td>
				</tr>
				
				<tr style="border-bottom:none;">
					<td colspan="2" valign="top" height="200">
						${dto.content}
					</td>
				</tr>
				
				<tr>
					<td colspan="2">
						<c:forEach var="vo" items="${listFile}" varStatus="status">
							<p class="file-item">
								<i class="fa-regular fa-folder-open"></i>
								<a href="${pageContext.request.contextPath}/admin/noticeManage/download?fileNum=${vo.fileNum}">${vo.originalFilename}</a>
								[${vo.fileSize} byte]
							</p>
						</c:forEach>
					</td>
				</tr>				
				
				<tr>
					<td colspan="2">
						이전글 :
						<c:if test="${not empty prevDto}">
							<a href="${pageContext.request.contextPath}/admin/noticeManage/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
						</c:if>
					</td>
				</tr>
				
				<tr>
					<td colspan="2">
						다음글 :
						<c:if test="${not empty nextDto}">
							<a href="${pageContext.request.contextPath}/admin/noticeManage/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
			
		<table class="table">
			<tr>
				<td width="50%" align="left">
					<c:choose>
						<c:when test="${sessionScope.member.userId == dto.userId}">
			    			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/noticeManage/update?num=${dto.num}&page=${page}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled>수정</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
	    			<button type="button" class="btn" onclick="deleteOk();">삭제</button>
				</td>
			
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/noticeManage/list?${query}';">리스트</button>
				</td>
			</tr>
		</table>
		
	</div>
</div>
