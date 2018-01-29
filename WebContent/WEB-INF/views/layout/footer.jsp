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