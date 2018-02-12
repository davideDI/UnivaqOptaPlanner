<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>

<c:set var="viewBookingDo" value="<%= PlannerConstants.URL_VIEW_BOOKING_DO %>" />

<section class="container">
	
	<security:authorize access="!isAuthenticated()">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
                <form name="loginform" name="login" method="POST" role="form" action="${pageContext.request.contextPath}/j_spring_security_check">
					<div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12">
							<label><spring:message code=".common.username" /></label>
							<input type="text" placeholder="<spring:message code=".common.username" />" name="j_username" class="form-control">
						</div>
					</div>
					<br>
					<div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12">
							<label><spring:message code=".common.password" /></label>
							<input type="password" placeholder="<spring:message code=".common.password" />" name="j_password" class="form-control" >
						</div>
					</div>
					<br>
					<input type="submit" class="btn" value="Signup">
				</form>
			</div>
			<div class="col-md-3"></div>
		</div>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<security:authorize access="hasAnyRole('admin dipartimento')">
			<div class="row">
		        <div class="col-xs-12 col-sm-12 col-md-12">
		            <p class="text-center"><b><spring:message code=".index.authorized" /></b></p>
		        </div>
		    </div>
			<div class="row">
				<c:forEach items="${sessionScope.groupList}"  var="group">
					<div class="col-xs-4 col-sm-4 col-md-4">
						<form name="optimizationFomr" align="center" method="POST" role="form" action="${pageContext.request.contextPath}${viewBookingDo}">
		             		<button class="buttonToLink" name="groupId" value="${group.id}" >
		             			<b><c:out value="${group.name}" /></b>
		             		</button>
		             	</form>
		                <p class="text-center"><c:out value="${group.description}" /></p>
		            </div>  
				</c:forEach>
		    </div>
		</security:authorize>
		<security:authorize access="!hasAnyRole('admin dipartimento')">
			<div class="row">
		        <div class="col-xs-12 col-sm-12 col-md-12">
		            <p class="text-center"><b><spring:message code=".index.not.authorized" /></b></p>
		        </div>
		    </div>
		</security:authorize>
	</security:authorize>
	
</section>