<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}

.table-article img { max-width: 650px; }
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-clipboard"></i> 공지사항 </h3>
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
							이름 : 관리자
						</td>
						<td align="right">
							${dto.reg_date} | 조회 ${dto.hitCount}
						</td>
					</tr>
					
					<tr>
						<td colspan="2" valign="top" height="200" style="border-bottom:none;">
							${dto.content}
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<c:forEach var="vo" items="${listFile}" varStatus="status">
								<p class="border text-secondary mb-1 p-2">
									<i class="bi bi-folder2-open"></i>
									<a href="${pageContext.request.contextPath}/notice/download?fileNum=${vo.fileNum}">${vo.originalFilename}</a>
									[${vo.fileSize} byte]
								</p>
							</c:forEach>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							이전글 :
							<c:if test="${not empty prevDto}">
								<a href="${pageContext.request.contextPath}/notice/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							다음글 :
							<c:if test="${not empty nextDto}">
								<a href="${pageContext.request.contextPath}/notice/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table class="table table-borderless">
				<tr>
					<td width="50%">&nbsp;</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/notice/list?${query}';">리스트</button>
					</td>
				</tr>
			</table>

		</div>
	</div>
</div>