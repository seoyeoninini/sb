<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-frame {
	display: flex;
}
.body-left {
	width: 260px;
}
.body-right {
	padding-left: 30px;
	width: 100%;
}

.list-group {
	list-style: none;
	margin-top: 10px;
	padding: 0;
    margin-bottom: 20px;
    width: 210px;    
    min-height: 500px;
    border-radius: 4px;    
    box-shadow: 0 1px 2px rgba(0,0,0,.075);	
}

.list-group .list-header {
    color: #ffffff;    
    background-color: #337ab7;    
    border-color: #337ab7;
    font-family: "Malgun Gothic", "Nanum Gothic", "Dotum";
    font-size: 14px;
    font-weight: bold;    
}

.list-group .list-header i {
    background: #ffffff;
    display: inline-block;
    margin: 0 10px 0 3px;
    position: relative;
    top: 2px;
    width: 3px;
    height: 15px;    
}

.list-group-item {
	/* position: relative; */
    padding: 10px 15px;
    /* margin-bottom: -1px; */
    background-color: #ffffff;
    border: 1px solid #dddddd;  
}

.list-group li {
    color: #555555;
}

.list-group-item.active, .list-group-item.active:focus, .list-group-item.active:hover {
    color: #424951;
    font-weight: bold;
    background: none;
    background-color: #F6F6F6;
    border-color: #D5D5D5;
    text-shadow: 0 -1px 0 #286090;
}

li.list-subject:not(.active) {
	cursor: pointer;
}
li.list-subject:hover {
    background-color: #F6F6F6;
}

.list-group-item:first-child {
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
}
.list-group-item:last-child {
    margin-bottom: 0;
    border-bottom-right-radius: 4px;
    border-bottom-left-radius: 4px;
}
li.list-group-item, button.list-group-item {
    color: #555555;
}
.delete-file { cursor: pointer; }
.delete-file:hover { color: #0d58ba; }
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<div class="container body-container body-frame">
	<div class="body-left">
		<ul class="list-group">
			<li class="list-group-item list-header"><i></i>${lectureCategory.category}</li>
			<c:forEach var="vo" items="${listSubject}" varStatus="status">
				<li class="list-group-item list-subject ${status.index==0?'active':''}" data-lectureCode="${vo.lectureCode}" data-lectureSubCode="${vo.lectureSubCode}">${vo.subject}</li>
			</c:forEach>
		</ul>
	</div>
	<div class="body-right">
	
		<div class="body-title">
			<h3><i class="fa-solid fa-graduation-cap"></i> <label class="subject-title">${subject}</label> </h3>
		</div>
	
		<div class="body-main content-frame"></div>
		
	</div>
		
</div>

<!-- 검색 폼 -->
<form name="searchForm" method="post">
	<input type="hidden" name="schType" value="all">
	<input type="hidden" name="kwd" value="">
</form>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, query, dataType, fn) {
	$.ajax({
		type: method,
		url: url,
		data: query,
		dataType: dataType,
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
	    	} else if(jqXHR.status === 402) {
	    		alert('권한이 없습니다.');
	    		return false;
			} else if(jqXHR.status === 400) {
				alert('요청 처리가 실패 했습니다.');
				return false;
	    	} else if(jqXHR.status === 410) {
	    		alert('삭제된 게시물입니다.');
	    		return false;
	    	}
	    	
			console.log(jqXHR.responseText);
		}
	});
}

function ajaxFileFun(url, method, query, dataType, fn) {
	$.ajax({
		type: method,
		url: url,
		processData: false,  // file 전송시 필수. 서버로전송할 데이터를 쿼리문자열로 변환여부
		contentType: false,  // file 전송시 필수. 서버에전송할 데이터의 Content-Type. 기본:application/x-www-urlencoded
		data: query,
		dataType: dataType,
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
	    	} else if(jqXHR.status === 402) {
	    		alert('권한이 없습니다.');
	    		return false;
			} else if(jqXHR.status === 400) {
				alert('요청 처리가 실패 했습니다.');
				return false;
	    	} else if(jqXHR.status === 410) {
	    		alert('삭제된 게시물입니다.');
	    		return false;
	    	}
	    	
			console.log(jqXHR.responseText);
		}
	});
}

$(function(){
	listPage(1);
});

// 글리스트
function listPage(page) {
	let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
	
	let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/list";
	let query = "pageNo="+page;
	let search = $('form[name=searchForm]').serialize();
	query = query+"&"+search;
	let selector = ".content-frame";
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
}

$(".list-group").on("click", "li.list-subject:not(.active)", function(){
	// let lectureCode = $(this).attr("data-lectureCode");
	// let lectureSubCode = $(this).attr("data-lectureSubCode");
	let subject = $(this).text();
	$(".subject-title").text(subject);
	
	 $("li.list-subject").removeClass("active");
	 $(this).addClass("active");
	 
	 listPage(1);
});

// 새로고침
function reloadLecture() {
	const f = document.searchForm;
	f.schType.value = "all";
	f.kwd.value = "";
	
	listPage(1);
}

function searchList() {
	const f = document.searchForm;
	
	f.schType.value = $("#schType").val();
	f.kwd.value = $.trim($("#kwd").val());
	listPage(1);
}

// 게시글 보기
function articleLecture(num, page) {
	let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
	
	let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/article";
	let query = "num=" + num;
	let search = $("form[name=searchForm]").serialize();
	query = query + "&pageNo=" + page + "&" + search;
	let selector = ".content-frame";
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
}
</script>

<c:if test="${sessionScope.member.membership>30}">
	<script type="text/javascript">
		// 글쓰기 폼
		function insertForm() {
			let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
			
			let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/write";
			let query = "tmp="+new Date().getTime();
			let selector = ".content-frame";
			
			const fn = function(data){
				$(selector).html(data);
			};
			ajaxFun(url, "get", query, "html", fn);
		}
	
		// 글 수정폼
		function updateForm(num, page) {
			let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
			
			let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/update";
			let query = "num=" + num + "&pageNo=" + page;
	
			let selector = ".content-frame";
			const fn = function(data){
				$(selector).html(data);
			};
			ajaxFun(url, "get", query, "html", fn);
		}
	
		// 등록 완료, 수정 완료
		function sendOk(mode, page) {
		    const f = document.lectureForm;
		    let str;
		    
			str = f.title.value;
		    if(!str) {
		        alert("제목을 입력하세요. ");
		        f.title.focus();
		        return;
		    }
		
			str = f.content.value;
		    if(!str) {
		        alert("내용을 입력하세요. ");
		        f.content.focus();
		        return;
		    }
		    
			let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
			
			let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/"+mode;
		    let formData = new FormData(f);
		    
		    /*
			let formData = new FormData();
			const subjectIE = document.querySelector('#subject');
			const fileIE = document.querySelector('#selectFile');
			formData.append('subject', subjectIE);
			formData.append('selectFile', fileIE.files[0]);
			*/
			
			const fn = function(data){
				let state = data.state;
		        if(state === "false") {
		            alert("게시물을 추가(수정)하지 못했습니다. !!!");
		            return false;
		        }
		        
		    	if(! page) {
		    		page = "1";
		    	}
		    	
		    	listPage(page);
			};
			
			ajaxFileFun(url, "post", formData, "json", fn);
		}
		
		// 글쓰기 취소, 수정 취소
		function sendCancel(page) {
			if( ! page ) {
				page = "1";
			}
			
			listPage(page);
		}
		
		// 글 삭제
		function deleteOk(num, page) {
			let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
			
			let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/delete";
			let query = "num=" + num;
			
			if(! confirm("위 강좌를 삭제 하시 겠습니까 ? ")) {
				  return;
			}
			
			const fn = function(data){
				listPage(page);
			};
			ajaxFun(url, "post", query, "json", fn);
		}
		
		// 수정에서 파일 삭제
		$(".content-frame").on("click", ".delete-file", function(){
			if(! confirm('선택한 파일을 삭제 하시겠습니까 ? ')) {
				return false;
			}
			
			let lectureSubCode = $(".list-group li.active").attr("data-lectureSubCode");
			
			let $tr = $(this).closest('tr');
			let fileNum = $(this).attr('data-fileNum');
			let url = "${pageContext.request.contextPath}/lecture/"+lectureSubCode+"/deleteFile";
			
			$.post(url, {fileNum:fileNum}, function(data){
				$($tr).remove();
			}, 'json');
		});		
	</script>
</c:if>

<!-- 유튜브 보기 Modal -->
<div class="modal fade" id="myDialogModal" tabindex="-1" aria-labelledby="myDialogModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="myDialogModalLabel">유투브 동영상</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
        		<iframe id="youtubePlayer" width="465" height="310" style="border:none;"></iframe>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btnStop">중지</button>
				<button type="button" class="btn btn-secondary btnClose" data-bs-dismiss="modal">닫기</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
// 유투브 play
function youtubePlay(src) {
	if(! src) {
		return;
	}
	
	src = src.substring(src.lastIndexOf('/') +1); 
	if(src.indexOf("=") > 0) {
		src = src.substring(src.indexOf("=") + 1);
	}
	
	// let movSrc = 'https://www.youtube.com/embed/'+src+"?autoplay=1&enablejsapi=1&version=3&playerapiid=ytplayer";
	let movSrc = 'https://www.youtube.com/embed/'+src+"?enablejsapi=1&version=3&playerapiid=ytplayer";
	
	// document.getElementById("youtubePlayer").setAttribute("allow", "accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
	document.getElementById("youtubePlayer").setAttribute("allow", "accelerometer; encrypted-media; gyroscope; picture-in-picture");
	document.getElementById("youtubePlayer").setAttribute("allowfullscreen", 1);
	document.getElementById("youtubePlayer").setAttribute("src", movSrc);

	$("#myDialogModal").modal("show");
}

function youtubeStop() {
	const frame = document.getElementById("youtubePlayer");
    frame.contentWindow.postMessage('{"event":"command","func":"' + 'stopVideo' + '","args":""}', '*');
}

$(function(){
	$(".btnStop").click(function(){
		youtubeStop();
	});
});

$(function(){
	$(".btnClose").click(function(){
		$("#myDialogModal2").modal("hide");
	});
});

// 대화상자가 닫힐 때
const myModalEl = document.getElementById('myDialogModal')
myModalEl.addEventListener('hidden.bs.modal', event => {
	youtubeStop();
});
</script>

