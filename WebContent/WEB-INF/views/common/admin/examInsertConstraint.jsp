<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>

<c:set var="urlAdmin" value="<%= PlannerConstants.URL_ADMIN %>" />
<c:set var="optimizationExamDo" value="<%= PlannerConstants.URL_OPTIMIZATION_EXAM_DO %>" />

<form name="optimizationExamFomr" method="POST" role="form" action="${pageContext.request.contextPath}${urlAdmin}${optimizationExamDo}">

	<h3><spring:message code=".optimize.constraint.text" /></h3>
	
	<br>
	
	<div class="row">
	
		<div class="col-md-2">
		
			<c:forEach items="${teacherList}" var="teacher">
				<c:if test="${teacher ne 'N.D.'}">
					<c:out value="${teacher}"></c:out><br>
				</c:if>
			</c:forEach>
			
		</div>
		
		<div class="col-md-4">
			
			<c:forEach items="${teacherList}" var="teacher">
				<c:if test="${teacher ne 'N.D.'}">
					<c:forEach items="${timeslotList}" var="timeslotTemp" varStatus="loop">
						<input type="checkbox" value="${loop.index}" name="${teacher}" /><c:out value="${timeslotTemp}" />
					</c:forEach>
					<br>
				</c:if>
			</c:forEach>
			
		</div>
		
		<div class="col-md-4">
			
			<c:forEach items="${teacherList}" var="teacher">
				<c:if test="${teacher ne 'N.D.'}">
					<c:forEach items="${dayWeekList}" var="dayWeekListTemp" varStatus="loop">
						<input type="checkbox" value="${dayWeekListTemp.key}" name="${teacher}_day" /><c:out value="${dayWeekListTemp.value}" />
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
	
	<br>
	
	<div class="row">
	
		<div class="col-md-12">
			
     		<button class="btn" name="optimizationExamSubmit" value="${idResource}" id="optSubmit" >
     			<spring:message code=".calendar.optimize.exam" />
     		</button>
		
		</div>
	
	</div>

</form>

<script type="text/javascript">

	$(document).ready(
		function() {
			$("#optSubmit").click(function() {
				
				$('#wait').show();
		        $('#modale').fadeIn('fast');
				
			});
		});

</script>