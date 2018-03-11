<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>
<%@ page import="it.univaq.planner.business.model.TipEvent" %>

<c:set var="viewBookingDo" value="<%= PlannerConstants.URL_VIEW_BOOKING_DO %>" />
<c:set var="urlAdmin" value="<%= PlannerConstants.URL_ADMIN %>" />
<c:set var="examInsertConstraint" value="<%= PlannerConstants.URL_EXAM_INSERT_CONSTRAINT_DO %>" />
<c:set var="courseInsertConstraint" value="<%= PlannerConstants.URL_COURSE_INSERT_CONSTRAINT_DO %>" />

<div class="row">
	
	<div class="col-md-2">
		<div class="row">
             <div class="col-md-12">

                 <legend><c:out value="${selectedGroup.name}" /></legend>
                 <select id="resourceSelect"
                         onChange="submitFormBookingDo(this.value)"
                         class="listOfResources"
                         style="width: 90%">
                     <option></option>
                     <c:forEach items="${resourceList}" var="resource">
                     	<option value="${resource.id}">
                             <c:out value="${resource.name}" /> (<c:out value="${resource.capacity}" /> <spring:message code=".calendar.seats" />)
                         </option>
                     </c:forEach>
                 </select>

             </div>
        </div>
        <br>
        <div class="row">
             <div class="col-md-12">
             	<form name="optimizationCourseFomr" method="POST" role="form" action="${pageContext.request.contextPath}${urlAdmin}${courseInsertConstraint}">
             		<button class="btn" name="optimizationCourseSubmit" value="${firstResource.id}" >
             			<spring:message code=".calendar.optimize.course" />
             		</button>
             	</form>
             </div>
        </div>
        <br>
        <div class="row">
             <div class="col-md-12">
             	<form name="optimizationExamFomr" method="POST" role="form" action="${pageContext.request.contextPath}${urlAdmin}${examInsertConstraint}">
             		<button class="btn" name="optimizationExamSubmit" value="${firstResource.id}" >
             			<spring:message code=".calendar.optimize.exam" />
             		</button>
             	</form>
             </div>
        </div>
        
        <br>
        <div class="row">
             <div class="col-md-12">
             	<legend><spring:message code=".calendar.tip.events" /></legend>	
             	<p><c:out value="<%= TipEvent.Esame.getName() %>" />&nbsp;&nbsp;
                	<img width="17" height="17" class="img-circle" src="${pageContext.request.contextPath}/resources/images/palla_verde.jpg" />
                </p>
                <p><c:out value="<%= TipEvent.Lezione.getName() %>" />&nbsp;&nbsp;
                	<img width="17" height="17" class="img-circle" src="${pageContext.request.contextPath}/resources/images/palla_rossa.jpg" />
                </p>
                <p><c:out value="<%= TipEvent.Seminario.getName() %>" />&nbsp;&nbsp;
                    <img width="17" height="17" class="img-circle" src="${pageContext.request.contextPath}/resources/images/palla_gialla.jpg" />
                </p>
             	<p><c:out value="<%= TipEvent.Generico.getName() %>" />&nbsp;&nbsp;
                    <img width="17" height="17" class="img-circle" src="${pageContext.request.contextPath}/resources/images/palla_blu.jpg" />
                </p>
             </div>
        </div>
        
	</div>

    <!-- Div principale -->
    <div class="col-md-10">

        <!-- Div contenitore calendario -->
        <div class="row">
            <div class="col-md-12" id="calendar"></div>
        </div>

    </div>
</div>

<script type="text/javascript">

	function submitFormBookingDo(id) {
		$('<form method="POST" action="${pageContext.request.contextPath}${viewBookingDo}"><input type="hidden" name="resourceId" value="'+id+'" /></form>').appendTo('body').submit().remove();
		
	}

    $(document).ready(function() {
    	
      $(".listOfResources").select2({
          placeholder: "${firstResource.name} (${firstResource.capacity} <spring:message code='.calendar.seats' />) "
      });
      
      $('#calendar').fullCalendar({

          // Definizione opzioni calendario
          header: {
              left: 'prev,next today',
              center: 'title',
              right: 'month,agendaWeek,basicDay,listDay'
                  },
          minTime: "08:00:00", //Definizione orari min
          maxTime: "20:30:00", //Definizione orari max
          //defaultDate: '2016-12-12', Se non impostata la data di default viene presa la data odierna
          navLinks: true, // can click day/week names to navigate views
          editable: true, // onclick sull'evento
          locale: '${localizationCookie}',
          eventDroppableEditable: false, //disabilitato il drop dell'evento
          eventDurationEditable: false,  //disabilitato il resize dell'evento
          defaultView: 'agendaWeek', //Vista di default
          eventLimit: true, // Quando ci sono più eventi per una data compare il link view more
          allDaySlot: false,
          contentHeight: 'auto',
          
          //Caricamento eventi
          events: [
        	  
        	  	<c:forEach items="${bookingList}" var="booking">
        	  		<c:forEach items="${booking.repeatList}" var="repeat">
        	  			{
	        	  			id         	: '${booking.id}',
	                        title      	: "${booking.name}",
	                        description	: '${booking.description}',
	                        start      	: moment('${repeat.eventDateStart}').format("YYYY-MM-DD HH:mm:ss"),
	                        end        	: moment('${repeat.eventDateEnd}').format("YYYY-MM-DD HH:mm:ss"),
	                        <c:choose>
	                        	<c:when test="${booking.idTipEvent eq 1}">
	                        		color : '#00FF00'
	                        	</c:when>
	                        	<c:when test="${booking.idTipEvent eq 2}">
	                        		color : '#FF0000'
	                        	</c:when>
	                        	<c:when test="${booking.idTipEvent eq 3}">
	                        		color : '#FFFF00'
	                        	</c:when>
	                        	<c:otherwise>
	                        		color : '#0000FF'
	                        	</c:otherwise>
	                        </c:choose>
        	  			},
                 	</c:forEach>
  				</c:forEach>
        	  			
          ],
          
          drop: function(date, jsEvent, ui) {
          },

          eventReceive: function( event ) {
          },

          eventMouseover: function( event, jsEvent, view ) {
          },

          eventMouseout: function( event, jsEvent, view ) {
          },

          eventDrop: function( calEvent, dayDelta, minuteDelta, allDay,
	 			revertFunc, jsEvent, ui, view ) {
          },

          eventDragStart: function( event, jsEvent, ui, view ) {
          },

          eventDragStop: function( event, jsEvent, ui, view ) {
          },

          eventResize: function( event, delta, revertFunc, jsEvent, ui, view ) {
          },

          viewRender: function() {
          },

          eventClick: function(calEvent, jsEvent, view) {
          }
          
      });
      
    });
</script>