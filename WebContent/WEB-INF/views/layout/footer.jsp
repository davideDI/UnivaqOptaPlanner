<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<footer id="footer">	
	<div class="footer2">
		<div class="container">
			<div class="row">
				<div class="col-md-4 panel"></div>
				<div class="col-md-4 panel">
					<div class="panel-body">
						<p class="text-center">
							<spring:message code=".common.title" />
						</p>
					</div>
				</div>
				<div class="col-md-4 panel"></div>
			</div>
		</div>
	</div>
</footer>

<!-- JavaScript libs are placed at the end of the document so the pages load faster -->
<script src="${pageContext.request.contextPath}/resources/js/modernizr-latest.js"></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.cslider.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>

<!-- Select2 -->
<link href="${pageContext.request.contextPath}/resources/select2-4.0.3/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/resources/select2-4.0.3/dist/js/select2.min.js"></script>

<!-- Full Calendar Javascript -->
<link rel='stylesheet' href="${pageContext.request.contextPath}/resources/fullcalendar-3.1.0/fullcalendar.min.css" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/resources/fullcalendar-3.1.0/fullcalendar.print.min.css" media='print' />
<!-- Libreria già presente (in caso di non compatibilità di versioni valutare caricamento unica libreria) </script>-->
<!-- <script src='lib/jquery.min.js'></script> -->
<script src="${pageContext.request.contextPath}/resources/fullcalendar-3.1.0/lib/moment.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/fullcalendar-3.1.0/fullcalendar.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/fullcalendar-3.1.0/locale-all.js"></script>