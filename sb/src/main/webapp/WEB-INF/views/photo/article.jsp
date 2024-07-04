<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.membership>50}">
	<script type="text/javascript">
		function deleteOk() {
			let query = "num=${dto.num}&${query}&imageFilename=${dto.imageFilename}";
		    let url = "${pageContext.request.contextPath}/photo/delete?" + query;
		
		    if(confirm("위 자료를 삭제 하시 겠습니까 ? ")) {
		  	  location.href = url;
		    }
		}
	</script>
</c:if>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-image"></i> 포토 갤러리 </h3>
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
								<a href="${pageContext.request.contextPath}/photo/article?${query}&num=${prevDto.num}">${prevDto.subject}</a>
							</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							다음글 :
							<c:if test="${not empty nextDto}">
								<a href="${pageContext.request.contextPath}/photo/article?${query}&num=${nextDto.num}">${nextDto.subject}</a>
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
								<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/photo/update?num=${dto.num}&page=${page}';">수정</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn btn-light" disabled>수정</button>
							</c:otherwise>
						</c:choose>
				    	
						<c:choose>
				    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.membership>50}">
				    			<button type="button" class="btn btn-light" onclick="deleteOk();">삭제</button>
				    		</c:when>
				    		<c:otherwise>
				    			<button type="button" class="btn btn-light" disabled>삭제</button>
				    		</c:otherwise>
				    	</c:choose>
					</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/photo/list?${query}';">리스트</button>
					</td>
				</tr>
			</table>

		</div>
	</div>
</div>