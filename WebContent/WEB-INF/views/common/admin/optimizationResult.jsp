<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="it.univaq.planner.common.spring.PlannerConstants" %>

<c:set var="viewBookingDo" value="<%= PlannerConstants.URL_VIEW_BOOKING_DO %>" />
<c:set var="urlAdmin" value="<%= PlannerConstants.URL_ADMIN %>" />
<c:set var="examInsertConstraint" value="<%= PlannerConstants.URL_EXAM_INSERT_CONSTRAINT_DO %>" />
<c:set var="courseInsertConstraint" value="<%= PlannerConstants.URL_COURSE_INSERT_CONSTRAINT_DO %>" />

<div class="row">
	
	<div class="col-md-2">
		<div class="row">
             <div class="col-md-12">

                 <legend><c:out value="${selectedGroup.name}" /></legend>

             </div>
        </div>
        
        <br>
        
        <c:forEach items="${resourceList}" var="resourceTemp">
			<div class="row">
				<div class="col-md-2"></div>
	            <div class="col-md-8" >
	            	<div class="row">
	            		<div class="col-md-6" ><p><c:out value="${resourceTemp.name}" /></p></div>
						<div class="col-md-6"><p class="circle" id="${resourceTemp.id}"></p></div>
		            </div>
	            </div>
	            <div class="col-md-2"></div>
	        </div>
        </c:forEach>
        
        <br>
        
        <div class="row">
			<div class="col-md-2"></div>
            <div class="col-md-8" >
				<span><b>${resultScore}</b></span>
			</div>
			<div class="col-md-2"></div>
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

    $(document).ready(function() {
    	
    	function getRandomColor() {
    		  var letters = '0123456789ABCDEF';
    		  var color = '#';
    		  for (var i = 0; i < 6; i++) {
    		    color += letters[Math.floor(Math.random() * 16)];
    		  }
    		  return color;
    		}
    	
    	<c:forEach items="${resourceList}" var="resourceTemp">
    		var _${resourceTemp.id} = '#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
    		$("#"+${resourceTemp.id}).css('background-color', _${resourceTemp.id});
    	</c:forEach>
    	
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
	        	  			id         		: '${booking.id}',
	                        title      		: "${booking.name}",
	                        description		: '${booking.description}',
	                        start      		: moment('${repeat.eventDateStart}').format("YYYY-MM-DD HH:mm:ss"),
	                        end        		: moment('${repeat.eventDateEnd}').format("YYYY-MM-DD HH:mm:ss"),
	                        color 			: _${booking.resource.id},
	                        resource_name   : '${booking.resource.name}'
        	  			},
                 	</c:forEach>
  				</c:forEach>
        	  			
          ],
          
          drop: function(date, jsEvent, ui) {
          },

          eventReceive: function( event ) {
          },

          eventMouseover: function( event, jsEvent, view ) {	
        	  $(this).attr("data-toggle", "popover");
              $(this).attr("data-placement", "right");
              $(this).attr("data-content", event.resource_name);
              $(this).attr("data-container", "body");
              //Visualizzo il "popover"
              $(this).popover('show');
          },

          eventMouseout: function( event, jsEvent, view ) {
        	  $(this).popover('hide');
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