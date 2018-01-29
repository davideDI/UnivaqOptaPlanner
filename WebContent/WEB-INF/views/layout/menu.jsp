<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<nav class="navbar navbar-inverse" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" 
				data-target="#menu" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<div class="container-fluid">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">
				<img src="${pageContext.request.contextPath}/resources/images/Logo_Top_Left.png" height="45" width="60" alt="Univaq" >
			</a>
			</div>
		</div>
		<div class="navbar-collapse collapse" id="menu">
			
			<ul class="nav navbar-nav navbar-right">
			 	<li><a href="?lang=en"><img src="${pageContext.request.contextPath}/resources/images/en.png" /></a></li>
   				<li><a href="?lang=it"><img src="${pageContext.request.contextPath}/resources/images/it.png" /></a></li>
								
				<security:authorize access="isAuthenticated()">
			
					<!-- TODO -->
					<%-- <security:authorize access="hasAnyRole('parent')">
						<li class="active"><a href="${pageContext.request.contextPath}/parent/index"><spring:message code="user.genitore" /></a></li>
					</security:authorize> --%>
										
					<li><a href="${pageContext.request.contextPath}/j_spring_security_logout"><spring:message code=".common.logout" /></a></li>
					
				</security:authorize>		
			</ul>
		</div>
	</div>
</nav>