<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>

<c:set var="urlAdmin" value="<%= PlannerConstants.URL_ADMIN %>" />
<c:set var="optimizationExamDo" value="<%= PlannerConstants.URL_OPTIMIZATION_EXAM_DO %>" />

<form name="optimizationExamFomr" method="POST" role="form" action="${pageContext.request.contextPath}${urlAdmin}${optimizationExamDo}">

	<h3><spring:message code=".optimize.constraint.text" /></h3>
	
	<br>
	
	<div class="row">
	
		<div class="col-md-3">
		
			<c:forEach items="${teacherList}" var="teacher">
				<c:if test="${teacher ne 'N.D.'}">
					<c:out value="${teacher}"></c:out><br>
				</c:if>
			</c:forEach>
			
		</div>
		
		<div class="col-md-9">
			
			<c:forEach items="${teacherList}" var="teacher">
				<c:if test="${teacher ne 'N.D.'}">
					<c:forEach items="${timeslotList}" var="timeslotTemp">
						<c:out value="${timeslotTemp}" />
					</c:forEach>
					<br>
				</c:if>
			</c:forEach>
			
		</div>
		
	</div>
	
	<br>
	
	<div class="row">
	
		<div class="col-md-12">
		
			<spring:message code=".optimize.second.limit" /> <input type="number" name="secondLimit" />
			
		</div>
		
	</div>
	
	<div class="row">
	
		<div class="col-md-12">
			
     		<button class="btn" name="optimizationExamSubmit" value="${idResource}" >
     			<spring:message code=".calendar.optimize.course" />
     		</button>
		
		</div>
	
	</div>

</form>
