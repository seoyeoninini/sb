<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}

.guest-form textarea { width: 100%; height: 75px; resize: none; }

.item-delete, .item-notify { cursor: pointer; padding-left: 5px; }
.item-delete:hover, .item-notify:hover { font-weight: 500; color: #2478FF; }

textarea::placeholder{
	opacity: 1; /* 파이어폭스에서 뿌옇게 나오는 현상 제거 */
	color: #333;
	text-align: center;
	line-height: 60px;
}
</style>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-journal-text"></i> 방명록 </h3>
		</div>
		
		<div class="body-main">

			<form name="guestForm" method="post">
				<div class="guest-form border border-secondary mt-5 p-3">
					<div class="p-1">
						<span class="fw-bold">방명록쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
					</div>
					<div class="p-1">
						<textarea name="content" id="content" class="form-control" placeholder="${empty sessionScope.member ? '로그인 후 등록 가능합니다.':''}"></textarea>
					</div>
					<div class="p-1 text-end">
						<button type="button" class="btnSend btn btn-dark" ${empty sessionScope.member ? "disabled":""}> 등록하기 <i class="bi bi-check2"></i> </button>
					</div>
				</div>
			</form>

			<div class="mt-4 mb-1 wrap-inner">
				<div class="py-2">
					<span class="fw-bold text-primary item-count">방명록 0개</span>
				</div>
				
				<div class="list-content" data-pageNo="0" data-totalPage="0"></div>
				
				<!-- 무한 스크롤 관찰용 -->
				<div class="sentinal" data-loading="false"></div>
			</div>

		</div>
	</div>
</div>

<script type="text/javascript">
function login() {
	location.href = '${pageContext.request.contextPath}/member/login';
}

function ajaxFun(url, method, formData, dataType, fn, file = false) {
	const sentinalNode = document.querySelector(".sentinal"); 
		
	const settings = {
			type: method, 
			data: formData,
			dataType:dataType,
			success:function(data) {
				fn(data);
			},
			beforeSend: function(jqXHR) {
				jqXHR.setRequestHeader('AJAX', true);
				sentinalNode.setAttribute("data-loading", "true");
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

const listNode = document.querySelector(".list-content");
const sentinalNode = document.querySelector(".sentinal");

function loadContent(page) {
	let url = '${pageContext.request.contextPath}/guest/list';
	let formData = 'pageNo=' + page;
	
	const fn = function(data) {
		addNewContent(data);
	};
	ajaxFun(url, 'get', formData, 'json', fn);
}

function addNewContent(data) {
	let dataCount = data.dataCount;
	let pageNo = data.pageNo;
	let total_page = data.total_page;
	
	listNode.setAttribute('data-pageNo', pageNo);
	listNode.setAttribute('data-totalPage', total_page);
	
	const itemCount = document.querySelector(".item-count");
	itemCount.innerHTML = '방명록 ' + dataCount + '개';
	
	sentinalNode.style.display = "none";
	
	if(parseInt(dataCount) === 0) {
		listNode.innerHTML = "";
		return;
	}
	
	let htmlText = '';
	for(let item of data.list) {
		let num = item.num;
		let userName = item.userName;
		let content = item.content;
		let reg_date = item.reg_date;
		let deletePermit = item.deletePermit;
		
		htmlText =  '<div class="item-content">';
		htmlText += '    <div class="row p-2 mx-0 border bg-light">';
		htmlText += '        <div class="col px-0"><i class="bi bi-person-circle text-muted"></i> ' + userName + '</div>';
		htmlText += '        <div class="col px-0 text-end">' + reg_date;
		if(deletePermit) {
			htmlText += '        |<span class="item-delete" data-num="' + num + '">삭제</span>';
		} else {
			htmlText += '        |<span class="item-notify" data-num="' + num + '">신고</span>';
		}
		htmlText += '        </div>';
		htmlText += '    </div>';
		htmlText += '    <div class="p-2 text-break">' + content + '</div>';
		htmlText += '</div>';

		// $(".list-content").append(htmlText);
		listNode.insertAdjacentHTML("beforeend", htmlText);
	}
	
	if(parseInt(pageNo) < parseInt(total_page)) {
		sentinalNode.setAttribute("data-loading", "false");
		sentinalNode.style.display = "block";
		
		// 관찰할 대상 등록
		io.observe(sentinalNode);
	}
}

// 무한스크롤
const ioCallback = (entries, io) => {
	entries.forEach((entry) => {
		if(entry.isIntersecting) { 
			// 관찰대상이 화면에 보이면
			let loading = sentinalNode.getAttribute("data-loading");
			if(loading !== "false") {
				return;
			}
			
			io.unobserve(entry.target); // 관찰대상을 관찰하지 않음
			
			let pageNo = parseInt(listNode.getAttribute("data-pageNo"));
			let total_page = parseInt(listNode.getAttribute("data-totalPage"));
			
			if(pageNo === 0 || pageNo < total_page) {
				pageNo++;
				loadContent(pageNo);
			}
			
		}
	});
};

const io = new IntersectionObserver(ioCallback);
io.observe(sentinalNode); // 관찰할 대상 등록

$(function(){
	$('.btnSend').click(function(){
		let content = $('#content').val().trim();
		if(! content) {
			$('#content').focus();
			return false;
		}
		
		let url = '${pageContext.request.contextPath}/guest/insert';
		let formData = {content:content};
		// let formData = 'content=' + encodeURIComponent(content);
		// let formData = $('form[name=guestForm]').serialize();
		
		const fn = function(data) {
			$('#content').val('');
			$('.list-content').empty();

			listNode.setAttribute("data-pageNo", "0");
			sentinalNode.style.display = "block";
			sentinalNode.setAttribute("data-loading", "false");
			io.observe(sentinalNode);
		};
		
		ajaxFun(url, 'post', formData, 'json', fn);
	});
});

$(function(){
	$('.list-content').on('click', '.item-delete', function(){
		if( ! confirm('게시글을 삭제 하시겠습니까 ? ')) {
			return false;
		}
		
		let num = $(this).attr('data-num');
		let url = '${pageContext.request.contextPath}/guest/delete';
		// let formData = 'num=' + num;
		let formData = {num: num};
		
		const fn = function(data) {
			$('.list-content').empty();

			listNode.setAttribute("data-pageNo", "0");
			sentinalNode.style.display = "block";
			sentinalNode.setAttribute("data-loading", "false");
			io.observe(sentinalNode);
		};
		ajaxFun(url, 'post', formData, 'json', fn);
	});
});

$(function(){
	$('.list-footer .more-btn').click(function(){
		let pageNo = $('.list-content').attr('data-pageNo');
		let total_page = $('.list-content').attr('data-totalPage');
		
		if(parseInt(pageNo) < parseInt(total_page)) {
			pageNo++;
			listPage(pageNo);
		}
	});
});
</script>