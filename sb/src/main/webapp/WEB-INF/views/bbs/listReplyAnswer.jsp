<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<div class='border-bottom m-1'>
		<div class='row p-1'>
			<div class='col-auto'>
				<div class='row reply-writer'>
					<div class='col-1'><i class='bi bi-person-circle text-muted icon'></i></div>
					<div class='col ms-2 align-self-center'>
						<div class='name'>김자바</div>
						<div class='date'>2022-05-01</div>
					</div>
				</div>
			</div>
			<div class='col align-self-center text-end'>
				<span class='reply-dropdown'><i class='bi bi-three-dots-vertical'></i></span>
				<div class='reply-menu'>
							<div class='deleteReplyAnswer reply-menu-item' data-replyNum='3' data-answer='7'>삭제</div>
							<div class='hideReplyAnswer reply-menu-item'>숨김</div>
				</div>
			</div>
		</div>

		<div class='p-2'>
			답글 내용 입니다.
		</div>
	</div>
