<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}

.nav-tabs .nav-link {
	min-width: 170px;
	background: #f3f5f7;
	border-radius: 0;
	border-right: 1px solid #dbdddf;
	color: #333;
	font-weight: 600;
}
.nav-tabs .nav-link.active {
	background: #3d3d4f;
	color: #fff;
}
.tab-pane { min-height: 300px; }

.table .ellipsis {
	position: relative;
	min-width: 200px;
}
.table .ellipsis span {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	position: absolute;
	left: 9px;
	right: 9px;
	cursor: pointer;
}
.table .ellipsis:before {
	content: '';
	display: inline-block;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<script type="text/javascript">
$(function(){
	let menu = "${menuItem}";
	$("#tab-"+menu).addClass("active");
	
    $("button[role='tab']").on("click", function(e){
		const tab = $(this).attr("data-tab");
		let url = "${pageContext.request.contextPath}/note/"+tab+"/list";
		location.href=url;
    });
});

function searchList() {
	const f = document.searchForm;
	f.submit();
}

$(function() {
    $("#chkAll").click(function() {
	   if($(this).is(":checked")) {
		   $("input[name=nums]").prop("checked", true);
        } else {
		   $("input[name=nums]").prop("checked", false);
        }
    });
 
    $(".btnDelete").click(function(){
		let cnt = $("input[name=nums]:checked").length;

		if (cnt === 0) {
			alert("삭제할 쪽지를 먼저 선택 하세요 !!!");
			return;
		}
         
		if(confirm("선택한 쪽지를 삭제 하시 겠습니까 ? ")) {
			const f = document.listForm;
			f.action = "${pageContext.request.contextPath}/note/${menuItem}/delete";
			f.submit();
		}
	});
});
</script>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-messenger"></i> 쪽지함 </h3>
		</div>
		
		<div class="body-main">

			<ul class="nav nav-tabs" id="myTab" role="tablist">
				<li class="nav-item" role="presentation">
					<button class="nav-link" id="tab-receive" data-bs-toggle="tab" data-bs-target="#nav-content" type="button" role="tab" aria-controls="receive" aria-selected="true" data-tab="receive">받은 쪽지함</button>
				</li>
				<li class="nav-item" role="presentation">
					<button class="nav-link" id="tab-send" data-bs-toggle="tab" data-bs-target="#nav-content" type="button" role="tab" aria-controls="send" aria-selected="true" data-tab="send">보낸 쪽지함</button>
				</li>
			</ul>
			
			<div class="tab-content pt-2" id="nav-tabContent">
				<div class="tab-pane fade show active mt-3" id="nav-content" role="tabpanel" aria-labelledby="nav-tab-content">
				
					<table class="table table-borderless mb-0">
						<tr>
							<td align="left" width="50%">
								<button type="button" class="btn btnDelete p-1" title="삭제"><i class="bi bi-trash"></i></button>
							</td>
							<td align="right">
								<button type="button" class="btn btn-light" onclick="javascript:location.href='${pageContext.request.contextPath}/note/write';">쪽지 쓰기</button>
							</td>
						</tr>
					</table>
					
					<form name="listForm" method="post">
						<table class="table table-hover board-list">
							<thead class="table-light">
								<tr>
									<th width="40"><input type="checkbox" name="chkAll" id="chkAll" class="form-check-input"></th>
									<th>내용</th>
									<th width="100">${menuItem=="receive"?"보낸사람":"받는사람"}</th>
									<th width="150">${menuItem=="receive"?"받은날짜":"보낸날짜"}</th>
									<th width="150">읽은날짜</th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach var="dto" items="${list}">
									<tr>
										<td><input type="checkbox" name="nums" value="${dto.num}" class="form-check-input"></td>
										<td class="left ellipsis">
											<span>
												<a href="${articleUrl}&num=${dto.num}" class="text-reset">${dto.content}</a>
											</span>
										</td>
										<td>${menuItem=="receive"?dto.senderName:dto.receiverName}</td>
										<td>${dto.sendDay}</td>
										<td>${dto.identifyDay}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						
						<input type="hidden" name="page" value="${page}">
						<input type="hidden" name="schType" value="${schType}">
						<input type="hidden" name="kwd" value="${kwd}">						
					</form>
					
					<div class="page-navigation">
						${dataCount == 0 ? "등록된 쪽지가 없습니다." : paging}
					</div>				
				
					<div class="row board-list-footer">
						<div class="col">
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/note/${menuItem}/list';" title="새로고침"><i class="bi bi-arrow-counterclockwise"></i></button>
							
						</div>
						<div class="col-6 text-center">
							<form class="row" name="searchForm" action="${pageContext.request.contextPath}/note/${menuItem}/list" method="post">
								<div class="col-auto p-1">
									<select name="schType" class="form-select">
										<option value="content" ${schType=="content"?"selected":""}>내용</option>
										<c:choose>
											<c:when test="${menuItem=='receive'}">
												<option value="senderName" ${schType=="senderName"?"selected":""}>보낸사람</option>
												<c:if test="${sessionScope.member.membership>50}">
													<option value="senderId" ${schType=="senderId"?"selected":""}>아이디</option>
												</c:if>
												<option value="sendDay" ${schType=="sendDay"?"selected":""}>받은날짜</option>
											</c:when>
											<c:otherwise>
												<option value="receiverName" ${schType=="receiverName"?"selected":""}>받는사람</option>
												<c:if test="${sessionScope.member.membership>50}">
													<option value="receiverId" ${schType=="receiverId"?"selected":""}>아이디</option>
												</c:if>
												<option value="sendDay" ${schType=="sendDay"?"selected":""}>보낸날짜</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
								<div class="col-auto p-1">
									<input type="text" name="kwd" value="${kwd}" class="form-control">
								</div>
								<div class="col-auto p-1">
									<button type="button" class="btn btn-light" onclick="searchList()"> <i class="bi bi-search"></i> </button>
								</div>
							</form>
						</div>
						<div class="col text-end">
							&nbsp;
						</div>
					</div>
				
				</div>
			</div>

		</div>
	</div>
</div>