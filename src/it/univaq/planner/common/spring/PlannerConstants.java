package it.univaq.planner.common.spring;

public interface PlannerConstants {

	// Url
	public final static String URL_LOGIN_DO = "/login.do";
	public final static String URL_LOGIN_ERROR_DO = "/loginError.do";
	public final static String URL_GROUP_ID = "/group/{idGroup}";
	public final static String URL_BOOKING_ID_RESOURCE = "/bookings/{idResource}";
	public final static String URL_VIEW_BOOKING_DO = "/viewBooking.do";
	public final static String URL_ADMIN = "/admin";
	public final static String URL_OPTIMIZATION_COURSE_DO = "/optimizationCourse.do";
	public final static String URL_OPTIMIZATION_EXAM_DO = "/optimizationExam.do";
	public final static String URL_EXAM_INSERT_CONSTRAINT_DO = "/exam/insertConstraints.do";
	public final static String URL_COURSE_INSERT_CONSTRAINT_DO = "/course/insertConstraints.do";
	
	//Redirect
	public final static String REDIRECT_HOME = "redirect:/";
	
	//Parameter
	public final static String PARAMETER_MESSAGE_ERROR = ".generic.message.error";
	public final static String PARAMETER_RESOURCE_ID = "resourceId";
	public final static String PARAMETER_GROUP_ID = "groupId";
	public final static String PARAMETER_OPTIMIZATION_COURSE_SUBMIT = "optimizationCourseSubmit";
	public final static String PARAMETER_OPTIMIZATION_EXAM_SUBMIT = "optimizationExamSubmit";
	public final static String PARAMETER_SECOND_LIMIT = "secondLimit";
	
	//View name
	public final static String VIEW_COMMON_INDEX = "common.index";
	public final static String VIEW_COMMON_CALENDAR_RESOURCE = "common.calendar.resource";
	public final static String VIEW_ADMIN_COURSE_INSERT_CONSTRAINT = "course.insert.constraint";
	public final static String VIEW_ADMIN_EXAM_INSERT_CONSTRAINT = "exam.insert.constraint";
	public final static String VIEW_OPTIMIZATION_RESULT = "optimization.result";
	
	//View Object
	public final static String MESSAGE_ERROR = "messageError";
	public final static String MESSAGE_ERROR_LOGIN = "messageErrorLogin";
	public final static String GROUP_LIST = "groupList";
	public final static String LOCALIZATION_COOKIE = "localizationCookie";
	public final static String DEFAULT_VALUE_COOKIE = "it";
	public final static String SELECTED_GROUP = "selectedGroup";
	public final static String FIRST_RESOURCE = "firstResource";
	public final static String RESOURCE_LIST = "resourceList";
	public final static String BOOKING_LIST = "bookingList";
	public final static String ID_GROUP = "idGroup";
	public final static String ID_RESOURCE = "idResource";
	public final static String TEACHER_LIST = "teacherList";
	public final static String TIMESLOT_LIST = "timeslotList";
	public final static String DAY_WEEK_LIST = "dayWeekList";
	public final static String RESULT_SCORE = "resultScore";
	
	//Utility
	public final static String N_D = "N.D.";
	public static final String[] TIMES = {"09:00", "11:00", "14:00", "16:00", "18:00"};
	public static final String[] WEEKDAYS = {"Mo", "Tu", "We", "Th", "Fr", "Sat"};
	public static final String _DAY = "_day";
	
	//Path Solver
	public static final String PATH_CURRICULUM_SOLVER_CONFIG = "org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml";
	public static final String PATH_EXAM_SOLVER_CONFIG = "org/optaplanner/examples/examination/solver/examinationSolverConfig.xml";
	
}
