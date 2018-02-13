<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>

<c:set var="viewBookingDo" value="<%= PlannerConstants.URL_VIEW_BOOKING_DO %>" />

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
             	<form name="optimizationFomr" method="POST" role="form" action="${pageContext.request.contextPath}/admin/optimization.do">
             		<button class="btn" name="optimizationSubmit" value="${firstResource.id}" >
             			<spring:message code=".calendar.optimize" />
             		</button>
             	</form>
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
              right: 'month,basicWeek,basicDay,listDay,agendaWeek'
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
	                        color 		: '#0000FF'
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