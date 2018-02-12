package it.univaq.planner.presentation;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.Group;
import it.univaq.planner.business.model.Resource;

@Controller
public class HomeController extends ABaseController {

	@RequestMapping(value=URL_LOGIN_DO, method=RequestMethod.GET)
	public String getHomePage(HttpServletRequest request, HttpServletResponse response) {
		
		try {
		
			request.getSession().setAttribute(GROUP_LIST, groupService.getAllGroups());
			response.addCookie(new Cookie(LOCALIZATION_COOKIE, DEFAULT_VALUE_COOKIE));
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return REDIRECT_HOME;
		
	}
	
	@RequestMapping(value=URL_VIEW_BOOKING_DO, method=RequestMethod.POST)
	public ModelAndView getResourceByGroup( HttpServletRequest request, 
											@CookieValue(value = LOCALIZATION_COOKIE) String localizationCookie) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_CALENDAR_RESOURCE);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		
		try {
			
			String resourceId = (String) request.getParameter(PARAMETER_RESOURCE_ID);
			String groupId = (String) request.getParameter(PARAMETER_GROUP_ID);
			
			//Case 1 : id group
			if(groupId != null && !groupId.isEmpty()) {
				Group group = groupService.getGroupById(new Long(groupId));
				mav.addObject(SELECTED_GROUP, group);
				List<Resource> resourceList = resourceService.getResourcesByIdGroup(new Long(groupId));
				mav.addObject(RESOURCE_LIST, resourceList);
				
				if(resourceList != null && !resourceList.isEmpty()) {
					Resource selectedResource = resourceList.get(0);
					mav.addObject(FIRST_RESOURCE, selectedResource);
					List<Booking> bookingList = bookingService.getAllBookingsByIdResource(selectedResource.getId());
					System.out.println(bookingList);
					mav.addObject(BOOKING_LIST, bookingList);
				}
			}
			
			//Case 2 : id resource
			else if(resourceId != null && !resourceId.isEmpty()) {
				Resource resource = resourceService.getResourceById(new Long(resourceId));
				mav.addObject(FIRST_RESOURCE, resource);
				mav.addObject(SELECTED_GROUP, resource.getGroup());
				List<Resource> resourceList = resourceService.getResourcesByIdGroup(new Long(resource.getGroup().getId()));
				mav.addObject(RESOURCE_LIST, resourceList);
				mav.addObject(BOOKING_LIST, bookingService.getAllBookingsByIdResource(resource.getId()));
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return mav;
		
	}
	
}
