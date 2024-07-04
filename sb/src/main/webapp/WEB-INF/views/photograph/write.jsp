<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div style="max-width: 800px;">
	<form name="photoForm" method="post" enctype="multipart/form-data">
		<table class="table mt-3 write-form">
			<tr>
				<td class="bg-light col-sm-2" scope="row">제 목</td>
				<td>
					<input type="text" name="subject" class="form-control" value="${dto.subject}">
				</td>
			</tr>
			
			<tr>
				<td class="bg-light col-sm-2" scope="row">작성자명</td>
					<td>
					<p class="form-control-plaintext">${sessionScope.member.userName}</p>
				</td>
			</tr>
	
			<tr>
				<td class="bg-light col-sm-2" scope="row">내 용</td>
				<td>
					<textarea name="content" class="form-control">${dto.content}</textarea>
				</td>
			</tr>
			
			<tr>
				<td class="bg-light col-sm-2" scope="row">이미지</td>
				<td>
					<div class="img-viewer"></div>
					<input type="file" name="selectFile" accept="image/*" class="form-control" style="display: none;">
				</td>
			</tr>
	
		</table>
		
		<table class="table table-borderless">
				<tr>
				<td class="text-center">
					<button type="button" class="btn btn-dark btnSendOk">${mode=='update'?'수정완료':'등록하기'}&nbsp;<i class="bi bi-check2"></i></button>
					<button type="reset" class="btn btn-light">다시입력</button>
					<button type="button" class="btn btn-light btnModalClose">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
					<input type="hidden" name="mode" value="${mode}">
					<c:if test="${mode=='update'}">
						<input type="hidden" name="num" value="${dto.num}">
						<input type="hidden" name="imageFilename" value="${dto.imageFilename}">
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
$(function() {
	let img = "${dto.imageFilename}";
	if( img ) { // 수정인 경우
		img = "${pageContext.request.contextPath}/uploads/photo/" + img;
		$(".write-form .img-viewer").empty();
		$(".write-form .img-viewer").css("background-image", "url("+img+")");
	}
	
	$(".write-form .img-viewer").click(function(){
		$("form[name=photoForm] input[name=selectFile]").trigger("click"); 
	});
	
	$("form[name=photoForm] input[name=selectFile]").change(function(){
		let file = this.files[0];
		if(! file) {
			$(".write-form .img-viewer").empty();
			if( img ) {
				img = "${pageContext.request.contextPath}/uploads/photo/" + img;
			} else {
				img = "${pageContext.request.contextPath}/resources/images/add_photo.png";
			}
			$(".write-form .img-viewer").css("background-image", "url("+img+")");
			
			return false;
		}
		
		if(! file.type.match("image.*")) {
			this.focus();
			return false;
		}
		
		let reader = new FileReader();
		reader.onload = function(e) {
			$(".write-form .img-viewer").empty();
			$(".write-form .img-viewer").css("background-image", "url("+e.target.result+")");
		}
		reader.readAsDataURL(file);
	});
});
</script>
