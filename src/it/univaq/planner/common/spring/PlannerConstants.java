package it.univaq.planner.common.spring;

public interface PlannerConstants {

	// Url
	public final static String URL_LOGIN_DO = "/login.do";
	public final static String URL_GROUP_ID = "/group/{idGroup}";
	public final static String URL_BOOKING_ID_RESOURCE = "/bookings/{idResource}";
	
	//View name
	public final static String VIEW_COMMON_INDEX = "common.index";
	public final static String VIEW_COMMON_CALENDAR_RESOURCE = "common.calendar.resource";
	
	//View Object
	public final static String GROUP_LIST = "groupList";
	public final static String LOCALIZATION_COOKIE = "localizationCookie";
	public final static String DEFAULT_VALUE_COOKIE = "it";
	public final static String SELECTED_GROUP = "selectedGroup";
	public final static String FIRST_RESOURCE = "firstResource";
	public final static String RESOURCE_LIST = "resourceList";
	public final static String BOOKING_LIST = "bookingList";
	public final static String ID_GROUP = "idGroup";
	public final static String ID_RESOURCE = "idResource";
	
}
