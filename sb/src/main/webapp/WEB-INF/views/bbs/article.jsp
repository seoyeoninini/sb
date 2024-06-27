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

</script>

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
							제목 입니다.
						</td>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td width="50%">
							이름 : 홍길동
						</td>
						<td align="right">
							2022-05-01 | 조회 1
						</td>
					</tr>
					
					<tr>
						<td colspan="2" valign="top" height="200" style="border-bottom: none;">
							내용 입니다.
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
								<p class="border text-secondary my-1 p-2">
									<i class="bi bi-folder2-open"></i>

								</p>
						</td>
					</tr>

					<tr>
						<td colspan="2">
							이전글 :
						</td>
					</tr>
					<tr>
						<td colspan="2">
							다음글 :
						</td>
					</tr>

				</tbody>
			</table>
			
			<table class="table table-borderless">
				<tr>
					<td width="50%">
								<button type="button" class="btn btn-light">수정</button>
				    			<button type="button" class="btn btn-light">삭제</button>
					</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/';">리스트</button>
					</td>
				</tr>
			</table>
			
		</div>
	</div>
</div>