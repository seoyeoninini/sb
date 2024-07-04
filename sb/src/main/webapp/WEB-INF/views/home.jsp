<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container body-container">

	<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
		<div class="carousel-indicators">
			<c:forEach var="vo" items="${listPhoto}" varStatus="status">
		    	<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="${status.index}" class="${status.index == 0 ? 'active':'' }" aria-current="true" aria-label="${vo.subject}"></button>
			</c:forEach>
		</div>
	  
		<div class="carousel-inner">
			<c:forEach var="vo" items="${listPhoto}" varStatus="status">
				<div class="carousel-item ${status.index==0?'active':'' }">
					<a href="${pageContext.request.contextPath}/photo/article?num=${vo.num}&page=1">
						<img src="${pageContext.request.contextPath}/uploads/photo/${vo.imageFilename}"
							class="d-block w-100" alt="${vo.subject}" style="max-height: 450px;">
					</a>
				</div>
			</c:forEach>
			<c:if test="${listPhoto.size()==0}">
				<img src="${pageContext.request.contextPath}/resources/images/bg.png"
					class="d-block w-100" style="max-height: 450px;">
			</c:if>
		</div>
	  
		<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
			<span class="carousel-control-prev-icon" aria-hidden="true"></span>
			<span class="visually-hidden">Previous</span>
		</button>
		<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
			<span class="carousel-control-next-icon" aria-hidden="true"></span>
			<span class="visually-hidden">Next</span>
		</button>
	</div>

</div>