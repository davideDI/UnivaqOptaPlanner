package it.univaq.planner.presentation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.univaq.planner.business.model.Group;
import it.univaq.planner.business.model.Resource;

@Controller
public class HomeController extends ABaseController {

	@RequestMapping(value=URL_LOGIN_DO, method=RequestMethod.GET)
	public ModelAndView getHomePage(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_INDEX);
		
		try {
		
			request.getSession().setAttribute(GROUP_LIST, groupService.getAllGroups());
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return mav;
		
	}
	
	@RequestMapping(value=URL_GROUP_ID, method=RequestMethod.GET)
	public ModelAndView getResourceByGroup( @PathVariable(ID_GROUP) String idGroup, 
											@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_CALENDAR_RESOURCE);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		
		try {
			
			Group group = groupService.getGroupById(new Long(idGroup));
			mav.addObject(SELECTED_GROUP, group);
			
			List<Resource> resourceList = resourceService.getResourcesByIdGroup(new Long(idGroup));
			mav.addObject(RESOURCE_LIST, resourceList);
			
			if(resourceList != null && !resourceList.isEmpty()) {
				Resource selectedResource = resourceList.get(0);
				mav.addObject(FIRST_RESOURCE, selectedResource);
				mav.addObject(BOOKING_LIST, bookingService.getAllBookingsByIdResource(selectedResource.getId()));
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return mav;
		
	}
	
	@RequestMapping(value=URL_BOOKING_ID_RESOURCE, method=RequestMethod.GET)
	public ModelAndView getResourceById(@PathVariable(ID_RESOURCE) String idResource, 
										@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_CALENDAR_RESOURCE);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		
		try {
		
			Resource resource = resourceService.getResourceById(new Long(idResource));
			mav.addObject(FIRST_RESOURCE, resource);
			mav.addObject(SELECTED_GROUP, resource.getGroup());
			List<Resource> resourceList = resourceService.getResourcesByIdGroup(new Long(resource.getGroup().getId()));
			mav.addObject(RESOURCE_LIST, resourceList);
			mav.addObject(BOOKING_LIST, bookingService.getAllBookingsByIdResource(resource.getId()));
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return mav;
		
	}
	
}
