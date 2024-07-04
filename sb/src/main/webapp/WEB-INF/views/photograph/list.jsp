<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}

.item {cursor: pointer; }
.item img { display: block; width: 100%; height: 200px; border-radius: 5px; }
.item img:hover { scale: 100.7%; }
.item .item-title {
	font-size: 14px;
	font-weight: 500;
	padding: 10px 2px 0;
	
	width: 175px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.write-form .img-viewer {
	cursor: pointer;
	border: 1px solid #ccc;
	width: 45px;
	height: 45px;
	border-radius: 45px;
	background-image: url("${pageContext.request.contextPath}/resources/images/add_photo.png");
	position: relative;
	z-index: 9999;
	background-repeat : no-repeat;
	background-size : cover;
}

span.item-view { cursor: pointer; }
span.item-view { color: #0d58ba; }

</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-image"></i> 포토 갤러리 </h3>
		</div>
		
		<div class="body-main">
	        <div class="row mb-2 list-header">
	            <div class="col-auto me-auto">
	            	<div class="input-group">
	            		<input type="text" id="searchInput" class="form-control" placeholder="검색어를 입력하세요" aria-describedby="basic-addon1">
						<span class="input-group-text btnSearch" id="basic-addon1"><i class="bi bi-search"></i></span>
						<input type="hidden" id="searchWord">
					</div>
	            </div>
	            <div class="col-auto">
	            	<button type="button" class="btn btn-light btnInsertForm">글올리기</button>
				</div>
	        </div>
	        
			<div class="row row-cols-4 px-1 py-1 g-2 list-content"
					data-pageNo="0" data-totalPage="0"></div>
			
			<div class="sentinel" data-loading="false"></div> <!-- 센터널 노드 : 마지막 무한 스크롤 관찰용 -->

		</div>
	</div>
	
	<!-- Modal Dialog -->
	<div class="modal fade" id="myDialogModal" tabindex="-1" aria-labelledby="myDialogModalLabel" 
			aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="myDialogModalLabel">포토 갤러리 </h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body pt-1"></div>
			</div>
		</div>
	</div>	
</div>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file = false) {
	const sentinelNode = document.querySelector('.sentinel');
	
	const settings = {
			type: method, 
			data: formData,
			dataType:dataType,
			success:function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				sentinelNode.setAttribute('data-loading', 'true');
				
				jqXHR.setRequestHeader('AJAX', true);
			},
			complete: function () {
			},
			error: function(jqXHR) {
				if(jqXHR.status === 403) {
					login();
					return false;
		    	} else if(jqXHR.status === 401) {
		    		return false;
		    	} else if(jqXHR.status === 402) {
		    		alert('권한이 없습니다.');
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

function loadContent(page) {
	let kwd = $('#searchWord').val();
	
	let url = '${pageContext.request.contextPath}/photograph/list';
	let query = 'pageNo=' + page + '&kwd=' + encodeURIComponent(kwd)
	
	const fn = function(data) {
		addNewContent(data);
	};
	ajaxFun(url, 'get', query, 'json', fn);
}

function addNewContent(data) {
	const listNode = document.querySelector('.list-content');
	const sentinelNode = document.querySelector('.sentinel'); // 센터널 노드(화면의 마지막인지 감시할 마지막 노드)
	
	let dataCount = data.dataCount;
	let pageNo = data.pageNo;
	let total_page = data.total_page;
	
	listNode.setAttribute('data-pageNo', pageNo);
	listNode.setAttribute('data-totalPage', total_page);
	
	sentinelNode.style.display = 'none';
	
	if(parseInt(dataCount) === 0) {
		listNode.innerHTML = '';
		return;
	}
	
	let htmlText;
	for(let item of data.list) {
		let num = item.num;
		let userName = item.userName;
		let subject = item.subject;
		let reg_date = item.reg_date;
		let imageFilename = item.imageFilename;
		
		htmlText =  '<div>';
		htmlText += '    <div class="col border rounded p-1 item item-view" data-num="' + num + '">';
		htmlText += '        <img src="${pageContext.request.contextPath}/uploads/photo/'+ imageFilename + '">';
		htmlText += '        <p class="item-title">'+ subject + '</p>';
		htmlText += '     </div>';
		htmlText += '</div>';
		
		// 인써트 어제이슨트 함수로 마지막에 HTML 추가
		listNode.insertAdjacentHTML('beforeend', htmlText);
	}
	
	if(pageNo < total_page) {
		sentinelNode.setAttribute('data-loading', 'false');
		sentinelNode.style.display = 'block';
		
		io.observe(sentinelNode); // 관찰할 대상(요소) 등록
	}	
}

// 인터섹션 업저버를 이용한 무한 스크롤
const sentinelNode = document.querySelector('.sentinel'); // 센터널 노드(화면의 마지막인지 감시할 마지막 노드)
const ioCallback = (entries, io) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) { // 관찰 대상의 교차(겹치는)상태(Boolean) : 화면에 보이면
    	
    	// 현재 페이지가 로딩중이면 빠져 나감
    	let loading = sentinelNode.getAttribute('data-loading');
    
    	if(loading !== 'false') {
    		return;
    	}

    	io.unobserve(entry.target); // 기존 관찰하던 요소는 더 이상 관찰하지 않음
    
    	const listNode = document.querySelector('.list-content');
		let pageNo = parseInt(listNode.getAttribute('data-pageNo'));
		let total_page = parseInt(listNode.getAttribute('data-totalPage'));
		
		if(pageNo === 0 || pageNo < total_page) {
			pageNo++;
			loadContent(pageNo);
		}
    }
  });
};

const io = new IntersectionObserver(ioCallback); // 관찰자 초기화
io.observe(sentinelNode); // 관찰할 대상(요소) 등록

$(function(){
	$('.btnSearch').click(function() {
		// 검색
		let kwd = $('#searchInput').val().trim();
		$('#searchWord').val(kwd);
		
		$('.list-content').empty();
    	loadContent(1);
	});
});

$(function(){
	$('.btnInsertForm').on('click', function(){
		// 글 등록 폼
		$('#myDialogModal .modal-body').empty();
		
		let url = '${pageContext.request.contextPath}/photograph/write';
		$.ajaxSetup({ beforeSend: function(e) { e.setRequestHeader('AJAX', true); } });
		$.get(url, function(data){
			$('#myDialogModal .modal-body').html(data);
			$('#myDialogModal').modal('show');
		}).fail(function(){
			alert('error');
		});
	});
});

$(function(){
	$('.container').on('click', '.btnSendOk', function() {
		//  글 등록 완료 및 수정 완료
		const f = document.photoForm;
		
		let str;
			
		str = f.subject.value.trim();
		if(!str) {
			alert('제목을 입력하세요. ');
			f.subject.focus();
			return false;
		}

		str = f.content.value.trim();
	    if(!str) {
	        alert('내용을 입력하세요. ');
	        f.content.focus();
	        return;
	    }
	    
	    let mode = f.mode.value;
	    if( (mode === 'write') && (!f.selectFile.value) ) {
	        alert('이미지 파일을 추가 하세요. ');
	        f.selectFile.focus();
	        return;
		}
		    
		let url = "${pageContext.request.contextPath}/photograph/"+mode;
	    let formData = new FormData(f);

		const fn = function(data){
			let state = data.state;
	        if(state === "false") {
	            alert("게시물을 추가(수정)하지 못했습니다. !!!");
	            return false;
	        }

	        $('#searchInput').val('');
	        $('#searchWord').val('');
	        
			$('.list-content').empty();
	    	loadContent(1);
	    	
	    	$('#myDialogModal').modal('hide');
		};
		
		ajaxFun(url, 'post', formData, 'json', fn, true);		
	});
});

$(function(){
	$('.container').on('click', '.item-view', function() {
		// 글보기
		$('#myDialogModal .modal-body').empty();
		
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/photograph/article/' + num;
		let kwd = $('#searchWord').val();
		$.ajaxSetup({ beforeSend: function(e) { e.setRequestHeader('AJAX', true); } });
		$.get(url, {kwd: kwd}, function(data){
			$('#myDialogModal .modal-body').html(data);
			$('#myDialogModal').modal('show');
		}).fail(function() {
			alert('error');
		});
	});
});

$(function(){
	$('.container').on('click', '.btnUpdateForm', function() {
		//  글 수정 폼
		$('#myDialogModal .modal-body').empty();
		
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/photograph/update';
		$.ajaxSetup({ beforeSend: function(e) { e.setRequestHeader('AJAX', true); } });
		$.get(url, {num:num}, function(data){
			$('#myDialogModal .modal-body').html(data);
			// $('#myDialogModal').modal('show');
		}).fail(function() {
			alert('error');
		});		
	});
});

$(function(){
	$('.container').on('click', '.btnDeleteOk', function() {
		//  글 삭제
		let num = $(this).attr('data-num');
		let imageFilename = $(this).attr('data-imageFilename');
		
		let url = '${pageContext.request.contextPath}/photograph/delete';
		let query = 'num=' + num + '&imageFilename=' + imageFilename;
		
		if(! confirm("게시글을 삭제 하시 겠습니까 ? ")) {
			  return;
		}
		
		const fn = function(data){
	        $('#searchInput').val('');
	        $('#searchWord').val('');
			
			$('.list-content').empty();
			loadContent(1);
			
			$('#myDialogModal').modal('hide');
		};
		ajaxFun(url, 'post', query, 'json', fn);
	});
});

$(function(){
	$('.container').on('click', '.btnModalClose', function() {
		//  대화상자 닫기
		$('#myDialogModal').modal('hide');
	});
});

</script>

