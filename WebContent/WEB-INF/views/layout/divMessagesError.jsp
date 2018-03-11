<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty messageError}">
	<div class="row">
		<div class="col-md-3"></div>
		<div class="col-md-6">
			<div class="alert alert-danger" role="alert">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <spring:message code=".generic.message.error" />
            </div>
		</div>
		<div class="col-md-3"></div>
	</div>
</c:if>
<c:if test="${not empty messageErrorLogin}">
	<div class="row">
		<div class="col-md-3"></div>
		<div class="col-md-6">
			<div class="alert alert-danger" role="alert">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <spring:message code=".generic.message.error.login" />
            </div>
		</div>
		<div class="col-md-3"></div>
	</div>
</c:if>