package it.univaq.planner.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import it.univaq.planner.business.BookingService;
import it.univaq.planner.business.GroupService;
import it.univaq.planner.business.ResourceService;
import it.univaq.planner.common.spring.PlannerConstants;

public abstract class ABaseController implements PlannerConstants  {

	protected org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected GroupService groupService;
	
	@Autowired
	protected ResourceService resourceService;
	
	@Autowired
	protected BookingService bookingService;
	
	protected void manageGenericError(ModelAndView mav, Exception ex) {
		
		logger.error("Error: " + mav.getViewName() + " - " + ex.getMessage());
		ex.printStackTrace();
		mav.setViewName(VIEW_COMMON_INDEX);
		mav.addObject(MESSAGE_ERROR, PARAMETER_MESSAGE_ERROR);
		
	}
	
}
