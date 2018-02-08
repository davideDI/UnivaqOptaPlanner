<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

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

	<!-- icone -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fonts/font-awesome.min.css">

	<link rel="favicon" href="${pageContext.request.contextPath}/resources/images/favicon.png">
	<link rel="stylesheet" media="screen" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
	<!-- Custom styles for our template -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-theme.css" media="screen">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/fancybox/jquery.fancybox.css" type="text/css" media="screen" />

	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/da-slider.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/js/html5shiv.js"></script>
	<script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
	<![endif]-->
	
	<link rel="stylesheet"	href="${pageContext.request.contextPath}/resources/jquery-ui/css/overcast/jquery-ui.min.css" />
	
	<link rel="stylesheet"	href="${pageContext.request.contextPath}/resources/datatables/css/bootstap.dataTables.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/datatables/css/jquery.dataTables.min.css" />
	
	
	<!-- vecchio url per datatable -->
	<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script> -->
	
	<script	src="${pageContext.request.contextPath}/resources/jquery/jquery-2.1.3.min.js"></script>
	<script	src="${pageContext.request.contextPath}/resources/jquery-ui/js/jquery-ui.js"></script>
	 
	
	<script	src="${pageContext.request.contextPath}/resources/datatables/my.js"></script>
	<script	src="${pageContext.request.contextPath}/resources/datatables/js/jquery.dataTables.min.js"></script>
	<script	src="${pageContext.request.contextPath}/resources/datatables/js/boostrap.dataTables.js"></script>
	
	
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/1.0.7/css/responsive.dataTables.min.css" />

	<script type="text/javascript" src="https://cdn.datatables.net/responsive/1.0.7/js/dataTables.responsive.min.js"></script>
</head>

<body>
	<div class="container container-fluid">
		<tiles:insertAttribute name="menu" />
		
		<div>
			<tiles:insertAttribute name="body" />
			<hr>
			<tiles:insertAttribute name="footer" />
		</div>
	
	</div>
</body>

</html>