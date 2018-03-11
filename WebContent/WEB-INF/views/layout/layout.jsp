<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<tiles:importAttribute scope="request" />

<!DOCTYPE html>

<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="Davide De Innocentis">
		
		<title>
			<spring:message code=".common.title" />
		</title>
	
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fonts/font-awesome.min.css">
	
		<link rel="favicon" href="${pageContext.request.contextPath}/resources/images/favicon.png">
		<link rel="stylesheet" media="screen" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-theme.css" media="screen">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/da-slider.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
		<link rel="stylesheet"	href="${pageContext.request.contextPath}/resources/jquery-ui/css/overcast/jquery-ui.min.css" />
		
		<script	src="${pageContext.request.contextPath}/resources/jquery/jquery-2.1.3.min.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/jquery-ui/js/jquery-ui.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/datatables/my.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/datatables/js/jquery.dataTables.min.js"></script>
		<script	src="${pageContext.request.contextPath}/resources/datatables/js/boostrap.dataTables.js"></script>
		
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/1.0.7/css/responsive.dataTables.min.css" />
	
		<script type="text/javascript" src="https://cdn.datatables.net/responsive/1.0.7/js/dataTables.responsive.min.js"></script>
	</head>

	<body>
	
		<div class="container-liquid" style="min-height: 100%;  height: 100%; margin: 0 auto -70px; ">
			
			<tiles:insertAttribute name="menu" />
			
			<div class="container-fluid">
				
				<div class="row">
                     <div class="col-md-1"></div>
                     <div class="col-md-10">
                     
                     	<tiles:insertAttribute name="divGif" />

						<tiles:insertAttribute name="divMessagesError" />
                     
						<tiles:insertAttribute name="body" />
				
					</div>
                    <div class="col-md-1"></div>
                </div>
                
			</div>
		
		</div>
		
		<tiles:insertAttribute name="footer" />
		
	</body>

</html>