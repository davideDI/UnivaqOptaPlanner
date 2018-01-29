<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="container">
	<div class="row">
		<div class="col-md-2"></div>
		<div class="col-md-5">
			<h3><spring:message	code="error.serverError" /></h3>

			<h2>
				<a href="${pageContext.request.contextPath}"><spring:message	code="error.link" /></a>
			</h2>
		</div>
	</div>

	<div class="row">
		<div class="col-md-2"></div>
		<div class="col-md-5">

			<img
				src="${pageContext.request.contextPath}/resources/images/erroreInterno.jpg"
				height="100" width="100" />
		</div>
	</div>
</div>