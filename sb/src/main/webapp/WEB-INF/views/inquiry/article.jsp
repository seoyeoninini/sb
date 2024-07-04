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
function deleteInquiry() {
	if(confirm("문의를 삭제 하시겠습니까 ?")) {
		let query = "num=${dto.num}&${query}";
		let url = "${pageContext.request.contextPath}/inquiry/delete?"+query;
		location.href = url;
	}
}
</script>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-question-diamond"></i> 1:1 문의 </h3>
		</div>
		
		<div class="body-main">

			<table class="table mt-5 mb-0">
				<tbody>
					<tr>
						<td colspan="2" class="px-0 pb-0">
							<div class="row gx-0">
								<div class="col-sm-1 bg-primary me-1">
									<p class="form-control-plaintext text-white text-center">Q.</p>
								</div>
								<div class="col bg-primary">
									<p class="form-control-plaintext text-white ps-2">${dto.subject}</p>
								</div>
							</div>
						</td>
					</tr>				
				
					<tr>
						<td width="50%">
							카테고리 : ${dto.category}
						</td>
						<td align="right">
							작성자 : ${dto.userName}
						</td>
					</tr>

					<tr>
						<td width="50%">
							문의일자 : ${dto.reg_date}
						</td>
						<td align="right">
							처리결과 : ${(empty dto.answer_date)?"답변대기":"답변완료"}
						</td>
					</tr>
					
					<tr>
						<td colspan="2" valign="top" height="200">
							${dto.content}
						</td>
					</tr>
				</tbody>
			</table>
			
			<c:if test="${not empty dto.answer}">
				<table class="table mb-0">
					<tbody>
						<tr>
							<td colspan="2" class="p-0">
								<div class="row gx-0">
									<div class="col-sm-1 bg-success me-1">
										<p class="form-control-plaintext text-white text-center">A.</p>
									</div>
									<div class="col bg-success">
										<p class="form-control-plaintext text-white ps-2">[답변] ${dto.subject}</p>
									</div>
								</div>
							</td>
						</tr>
					
						<tr>
							<td width="50%">
								담당자 : 관리자
							</td>
							<td align="right">
								답변일자 : ${dto.answer_date}
							</td>
						</tr>
						
						<tr>
							<td colspan="2" valign="top" height="150">
								${dto.answer}
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			
			<table class="table table-borderless mb-2">
				<tr>
					<td width="50%">
						<button type="button" class="btn btn-light" onclick="deleteInquiry();">질문삭제</button>
					</td>
					<td class="text-end">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/inquiry/list?${query}';">리스트</button>
					</td>
				</tr>
			</table>

		</div>
	</div>
</div>