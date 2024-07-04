<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<div class="container-fluid bg-light header-top">
		<div class="container">
			<div class="row">
				<div class="col">
					<div class="p-2">
						<i class="bi bi-telephone-inbound-fill"></i> +82-1234-1234
					</div>
				</div>
				<div class="col">
					<div class="d-flex justify-content-end">
						<c:choose>
							<c:when test="${empty sessionScope.member}">
								<div class="p-2">
									<a href="javascript:dialogLogin();" title="로그인"><i class="bi bi-lock"></i></a>
								</div>
								<div class="p-2">
									<a href="${pageContext.request.contextPath}/member/member" title="회원가입"><i class="bi bi-person-plus"></i></a>
								</div>	
							</c:when>
							<c:otherwise>
								<div class="p-2">
									<a href="${pageContext.request.contextPath}/member/logout" title="로그아웃"><i class="bi bi-unlock"></i></a>
								</div>					
								<div class="p-2">
									<a href="${pageContext.request.contextPath}/note/receive/list" title="쪽지" class="position-relative">
										<i class="bi bi-bell"></i>
										<span class="new-noteCount position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary" style="font-size: 6px;"></span>
									</a>
								</div>
								<c:if test="${sessionScope.member.membership>50}">
									<div class="p-2">
										<a href="${pageContext.request.contextPath}/admin" title="관리자"><i class="bi bi-gear"></i></a>
									</div>					
								</c:if>
							</c:otherwise>
						</c:choose>
					</div>
					
				</div>
			</div>
		</div>
	</div>
	
	<nav class="navbar navbar-expand-lg navbar-light">
		<div class="container">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/"><i class="bi bi-app-indicator"></i></a>
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
				
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ms-auto flex-nowrap">
					<li class="nav-item">
						<a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/">홈</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#">가볼만한곳</a>
					</li>
	
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
							커뮤니티
						</a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/guest/main">방명록</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/bbs/list">게시판</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/replyBoard/list">답변형 게시판</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/photograph/main">포토갤러리</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/chatting/main">채팅</a></li>
						</ul>
					</li>
	
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
							강좌
						</a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lecture/L100001">프로그래밍</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lecture/L100002">데이터베이스</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lecture/L100003">웹 프로그래밍</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lecture/L100004">데이터분석 및 AI</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/lecture/L100005">클라우드 및 기타</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/qna/list">질문과 답변</a></li>
						</ul>
					</li>
					
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
							고객센터
						</a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/faq/main">자주하는질문</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/notice/list">공지사항</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/inquiry/list">1:1문의</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/event/progress/list">이벤트</a></li>
						</ul>
					</li>
					
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
							마이페이지
						</a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/schedule/main">일정관리</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/album/list">사진첩</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/note/receive/list">쪽지함</a></li>
							<li><a class="dropdown-item" href="#">친구관리</a></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/mail/send">메일</a></li>
							<li><hr class="dropdown-divider"></li>
							<li><a class="dropdown-item" href="${pageContext.request.contextPath}/member/pwd">정보수정</a></li>
						</ul>
					</li>
					
				</ul>
			</div>
			
		</div>
	</nav>
	
	<script type="text/javascript">
		$(function(){
			var isLogin = "${not empty sessionScope.member ? 'true':'false'}";
			var timer = null;
			
			if(isLogin === "true") {
				newNoteCount();
				// timer = setInterval("newNoteCount();", 1000 * 60 * 10); // 10분 후, 10분에 한번씩 실행
			}
			
			function newNoteCount() {
				var url = "${pageContext.request.contextPath}/note/newNoteCount";
				var query = "tmp=" + new Date().getTime();
				
				$.ajax({
					type:"get"
					,url:url
					,data:query
					,dataType:"json"
					,success:function(data) {
						var newCount = parseInt(data.newCount);
						if(newCount === 0) {
							$(".new-noteCount").hide();
							return false;
						}
						if(newCount >= 10) {
							$(".new-noteCount").text("9+");
						} else {
							$(".new-noteCount").text(newCount);
						}
					}
					,error:function(jqXHR) {
						if(timer != null) {
							clearInterval(timer);
							timer = null;
						}
						console.log(jqXHR.responseText);
					}
				});
			}
		});
	</script>
		
