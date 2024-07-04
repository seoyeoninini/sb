<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
.body-container {
	max-width: 800px;
}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boot-board.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dateUtil.js"></script>

<script type="text/javascript">
function sendOk() {
    const f = document.scheduleForm;
	let str;
	
	if( ! f.subject.value.trim() ) {
		f.subject.focus();
		return;
	}
	
	if(! f.categoryNum.value ) {
		f.categoryNum.focus();
		return;
	}
	
	if( ! f.sday.value ) {
		f.sday.focus();
		return;
	}
	
	if( f.eday.value ) {
		let s1 = f.sday.value.replace(/-/g, "");
		let s2 = f.eday.value.replace(/-/g, "");
		if(s1 > s2) {
			f.sday.focus();
			return;
		}
	}

	if( f.etime.value ) {
		let s1 = f.stime.value.replace(/:/g, "");
		let s2 = f.etime.value.replace(/:/g, "");
		if(s1 > s2) {
			f.stime.focus();
			return;
		}
	}
	
	if( ! f.repeat_cycle.value ) {
		f.repeat_cycle.value = "0";
	}
	
	if( f.repeat.value !=  "0" && ! /^(\d){1,2}$/g.test(f.repeat_cycle.value) ) {
		f.repeat_cycle.focus();
		return;
	}
	
	if( f.repeat.value !=  "0" && f.repeat_cycle.value < 1 ) {
		f.repeat_cycle.focus();
		return;
	}	

	// 종일일정의 경우 종료일자는 종료일자+1로 저장해서 불러와야 함
	if($("#form-eday").val() && $("#form-all_day").is(":checked")) {
		$("#form-eday").val(daysLater($("#form-eday").val(), 1));
	}
	
    f.action = "${pageContext.request.contextPath}/schedule/${mode}";
    f.submit();
}

$(function(){
	$("#form-all_day").click(function(){
		if(this.checked === true) {
			$("#form-stime").val("").hide();
			$("#form-etime").val("").hide();
		} else if(this.checked === false && $("#form-repeat").val() === "0"){
			$("#form-stime").val("00:00").show();
			$("#form-etime").val("00:00").show();
		}
	});

	$("#form-sday").change(function(){
		$("#form-eday").val($("#form-sday").val());
	});
	
	let repeat_cycle = "${dto.repeat_cycle}";
	if(repeat_cycle && repeat_cycle != "0") {
		$("#form-all_day").prop("checked", true);
		$("#form-all_day").prop("disabled", true);

		$("#form-stime").val("").hide();
		$("#form-etime").val("").hide();
		$("#form-eday").val("");
		$("#form-etime").val("");
		$("#form-eday").closest("tr").hide();
	}
	
	$("#form-repeat").change(function(){
		if($(this).val() === "0") {
			$("#form-repeat_cycle").val("0").hide();
			
			$("#form-all_day").prop("checked", true);
			$("#form-all_day").prop("disabled", false);
			
			$("#form-eday").val($("#form-sday").val());
			$("#form-eday").closest("tr").show();
		} else {
			$("#form-repeat_cycle").show();
			
			$("#form-all_day").prop("checked", true);
			$("#form-all_day").prop("disabled", true);

			$("#form-stime").val("").hide();
			$("#form-etime").val("").hide();
			$("#form-eday").val("");
			$("#form-etime").val("");
			$("#form-eday").closest("tr").hide();
		}
	});
});

$(function(){
	$("#form-color").css("background-color", $("#form-color").val());
	$("#form-color").css("color", "#fff");
	$("#form-color").change(function(){
		$(this).css("background-color", $(this).val());
	});
});
</script>

<div class="container">
	<div class="body-container">	
		<div class="body-title">
			<h3><i class="bi bi-calendar-event"></i> 일정관리 </h3>
		</div>
		
		<div class="body-main">
		
			<form name="scheduleForm" method="post">
				<table class="table mt-5 write-form">
					<tr>
						<td class="bg-light col-2" scope="row">제 목</td>
						<td>
							<div class="row">
								<div class="col">
									<input type="text" name="subject" id="form-subject" class="form-control" value="${dto.subject}">
								</div>
							</div>
							<small class="form-control-plaintext">* 제목은 필수 입니다.</small>
						</td>
					</tr>

					<tr>
						<td class="bg-light col-2" scope="row">일정분류</td>
						<td>
							<div class="row">
								<div class="col-5">
									<select name="categoryNum" id="form-categoryNum" class="form-select">
										<%-- <option value="0">설정하지 않음</option> --%>
										<c:forEach var="vo" items="${listCategory}">
											<option value="${vo.categoryNum}" ${dto.categoryNum == vo.categoryNum ? "selected":""}>${vo.category}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<small class="form-control-plaintext">* 카테고리는 일정 메인화면에서 설정합니다.</small>
						</td>
					</tr>

					<tr>
						<td class="bg-light col-2" scope="row">색 상</td>
						<td>
							<div class="row">
								<div class="col-5">
									<select name="color" id="form-color" class="form-select">
										<option value="blue" style="background:blue;" ${dto.color=="blue"?"selected":""}>파랑</option>
										<option value="green" style="background:green;" ${dto.color=="green"?"selected":""}>녹색</option>
										<option value="red" style="background:red;" ${dto.color=="red"?"selected":""}>빨강</option>
										<option value="orange" style="background:orange;" ${dto.color=="orange"?"selected":""}>주황</option>
										<option value="tomato" style="background:tomato;" ${dto.color=="chartreuse"?"selected":""}>토마토</option>
										<option value="magenta" style="background:magenta;" ${dto.color=="cyan"?"selected":""}>마젠타</option>
										<option value="purple" style="background:purple;" ${dto.color=="purple"?"selected":""}>보라</option>
										<option value="brown" style="background:brown;" ${dto.color=="brown"?"selected":""}>갈색</option>
										<option value="navy" style="background:navy;" ${dto.color=="navy"?"selected":""}>남색</option>
										<option value="gray" style="background:gray;" ${dto.color=="gray"?"selected":""}>회색</option>
										<option value="black" style="background:black;" ${dto.color=="black"?"selected":""}>검정</option>
									</select>
								</div>
							</div>
						</td>
					</tr>

					<tr>
						<td class="bg-light col-2" scope="row">종일일정</td>
						<td class="py-3">
							<div class="row">
								<div class="col">
									<input type="checkbox" name="all_day" id="form-all_day" class="form-check-input" 
										 value="1" ${dto.all_day == 1 ? "checked ":"" } >
									<label class="form-check-label" for="form-all_day"> 하루종일</label>
								</div>
							</div>
						</td>
					</tr>

 					<tr>
						<td class="bg-light col-2" scope="row">시작일자</td>
						<td>
							<div class="row">
								<div class="col-5 pe-0">
									<input type="date" name="sday" id="form-sday" class="form-control" value="${dto.sday}">
								</div>
								<div class="col-3">
									<input type="time" name="stime" id="form-stime" class="form-control" value="${dto.stime}"
										style="display: ${dto.all_day == 1 ? 'none;':'inline-block;'}">
								</div>
							</div>
							<small class="form-control-plaintext">* 시작날짜는 필수입니다.</small>
						</td>
					</tr>

 					<tr>
						<td class="bg-light col-2" scope="row">종료일자</td>
						<td>
							<div class="row">
								<div class="col-5 pe-0">
									<input type="date" name="eday" id="form-eday" class="form-control" value="${dto.eday}">
								</div>
								<div class="col-3">
									<input type="time" name="etime" id="form-etime" class="form-control" value="${dto.etime}"
										style="display: ${dto.all_day==1 ? 'none;':'inline-block;'}">
								</div>
							</div>
							<small class="form-control-plaintext">종료일자는 선택사항이며, 시작일자보다 작을 수 없습니다.</small>
						</td>
					</tr>
					
					<tr>
						<td class="bg-light col-2" scope="row">일정반복</td>
						<td>
							<div class="row">
								<div class="col-5 pe-0">
									<select name="repeat" id="form-repeat" class="form-select">
										<option value="0" ${dto.repeat=="0"?"selected":""}>반복안함</option>
										<option value="1" ${dto.repeat=="1"?"selected":""}>년반복</option>
									</select>
								</div>
								<div class="col-3">
									<input type="text" name="repeat_cycle" id="form-repeat_cycle" maxlength="2" class="form-control"
										style="display: ${dto.repeat==0 ? 'none;':'inline-block;'}"
										value="${dto.repeat_cycle}"
										placeholder="반복주기">
								</div>
							</div>
						</td>
					</tr>

					<tr>
						<td class="bg-light col-2" scope="row">메 모</td>
						<td>
							<textarea name="memo" id="form-memo" class="form-control" style="height: 90px;">${dto.memo}</textarea>
						</td>
					</tr>
				</table>
				
				<table class="table table-borderless">
 					<tr>
						<td class="text-center">
							<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=='update'?'수정완료':'일정등록'}&nbsp;<i class="bi bi-check2"></i></button>
							<button type="reset" class="btn btn-light">다시입력</button>
							<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/schedule/main';">${mode=='update'?'수정취소':'등록취소'}&nbsp;<i class="bi bi-x"></i></button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="num" value="${dto.num}">
							</c:if>
						</td>
					</tr>
				</table>
			</form>
		
		</div>
	</div>
</div>