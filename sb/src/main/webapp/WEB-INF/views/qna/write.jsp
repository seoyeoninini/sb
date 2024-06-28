<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 870px;
}

</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<script type="text/javascript">
function sendOk() {
    const f = document.qnaForm;
	let str;
	
	if(!f.lectureSubCode.value) {
        alert("과목을 선택하세요. ");
        f.title.focus();
        return;
	}
	
    str = f.title.value.trim();
    if(!str) {
        alert("제목을 입력하세요. ");
        f.title.focus();
        return;
    }

    str = f.content.value.trim();
    if(!str) {
        alert("내용을 입력하세요. ");
        f.content.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/qna/${mode}";
    f.submit();
}
</script>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-exclamation-square"></i> 강좌 질문과 답변 </h3>
		</div>
		
		<div class="body-main">
		
			<form name="qnaForm" method="post">
				<table class="table mt-5 write-form">
					<tr>
						<td class="bg-light col-sm-2" scope="row">유&nbsp;&nbsp;&nbsp;&nbsp;형</td>
						<td>
							<div class="row">
								<div class="col-sm-4 pe-1">
									<select name="lectureCode" class="form-select">
										<c:forEach var="vo" items="${listCategory}">
											<option value="${vo.lectureCode}" ${vo.lectureCode==dto.lectureCode?"selected":""}>${vo.category}</option>
										</c:forEach>
									</select>
								</div>
									
								<div class="col-sm-4 ps-1">
									<select name="lectureSubCode" class="form-select">
										<option value="">:: 과목 선택 ::</option>
										<c:forEach var="vo" items="${listSubject}">
											<option value="${vo.lectureSubCode}" ${vo.lectureSubCode==dto.lectureSubCode?"selected":""}>${vo.subject}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</td>
					</tr>
				
					<tr>
						<td class="bg-light col-sm-2" scope="row">제 목</td>
						<td>
							<input type="text" name="title" class="form-control" value="${dto.title}">
						</td>
					</tr>
					
					<tr>
						<td class="bg-light col-sm-2" scope="row">작성자명</td>
 						<td>
							<p class="form-control-plaintext">${sessionScope.member.userName}</p>
						</td>
					</tr>
		
					<tr>
						<td class="bg-light col-sm-2" scope="row">공개여부</td>
						<td class="py-3"> 
							<input type="radio" name="secret" id="secret1" class="form-check-input" 
								value="0" ${empty dto || dto.secret==0?"checked":"" }>
							<label class="form-check-label" for="secret1">공개</label>
							<input type="radio" name="secret" id="secret2" class="form-check-input"
								value="1" ${dto.secret==1?"checked":"" }>
							<label class="form-check-label" for="secret2">비공개</label>
						</td>
					</tr>

					<tr>
						<td class="bg-light col-sm-2" scope="row">내 용</td>
						<td>
							<textarea name="content" class="form-control">${dto.content}</textarea>
						</td>
					</tr>
					
				</table>
				
				<table class="table table-borderless">
 					<tr>
						<td class="text-center">
							<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i class="bi bi-check2"></i></button>
							<button type="reset" class="btn btn-light">다시입력</button>
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/qna/list';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="num" value="${dto.num}">
								<input type="hidden" name="page" value="${page}">
							</c:if>
						</td>
					</tr>
				</table>
			</form>
		
		</div>
	</div>
</div>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file = false) {
	const settings = {
			type: method, 
			data: formData,
			success:function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function () {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					login();
					return false;
				} else if(jqXHR.status === 400) {
					alert('요청 처리가 실패 했습니다.');
					return false;
		    	}
		    	
				console.log(jqXHR.responseText);
			}
	};
	
	if(file) {
		settings.processData = false;  // file 전송시 필수. 서버로전송할 데이터를 쿼리문자열로 변환여부
		settings.contentType = false;  // file 전송시 필수. 서버에전송할 데이터의 Content-Type. 기본:application/x-www-urlencoded
	}
	
	$.ajax(url, settings);
}

$(function(){
	$('form select[name=lectureCode]').change(function(){
		let lectureCode = $(this).val();
		if(! lectureCode) {
			return false;
		}
		
		$('form select[name=lectureSubCode]').find('option')
			.remove().end()
			.append('<option value="">:: 과목 선택 ::</option>');		
		
		let url = '${pageContext.request.contextPath}/qna/listSubject';
		let query = 'lectureCode='+lectureCode;
		
		const fn = function(data) {
			$.each(data.listSubject, function(index, item){
				let lectureSubCode = item.lectureSubCode;
				let subject = item.subject;
				let s = '<option value="'+lectureSubCode+'">'+subject+'</option>';
				$('form select[name=lectureSubCode]').append(s);
			});
		};
		ajaxFun(url, 'get', query, 'json', fn);
	});
});
</script>
