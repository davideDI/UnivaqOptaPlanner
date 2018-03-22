package it.univaq.planner.presentation;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;
import org.optaplanner.examples.examination.domain.Exam;
import org.optaplanner.examples.examination.domain.Examination;
import org.optaplanner.examples.examination.domain.FollowingExam;
import org.optaplanner.examples.examination.domain.LeadingExam;
import org.optaplanner.examples.examination.domain.PeriodPenalty;
import org.optaplanner.examples.examination.domain.RoomPenalty;
import org.optaplanner.examples.examination.domain.Student;
import org.optaplanner.examples.examination.domain.Topic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.Group;
import it.univaq.planner.business.model.Repeat;
import it.univaq.planner.business.model.Resource;
import it.univaq.planner.business.model.TipBookingStatus;
import it.univaq.planner.business.model.TipEvent;
import it.univaq.planner.common.spring.PlannerConstants;

@Controller
@RequestMapping(value=PlannerConstants.URL_ADMIN)
public class AdminController extends ABaseController {

	////////////////////////
	//Optimize Courses
	////////////////////////
	@RequestMapping(value=URL_COURSE_INSERT_CONSTRAINT_DO, method=RequestMethod.POST)
	public ModelAndView courseInsertConstraint(	HttpServletRequest request, 
												@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - courseInsertConstraint()");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		mav.setViewName(VIEW_ADMIN_COURSE_INSERT_CONSTRAINT);
		
		try {
			
			String resourceId = (String) request.getParameter(PARAMETER_OPTIMIZATION_COURSE_SUBMIT);
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			mav.addObject(ID_RESOURCE, resourceIdL);
			
			List<String> teacherList = bookingService.getDifferentTeacherIdByIdGroupAndTipEvent(resourceService.getResourceById(resourceIdL).getGroup().getId(), TipEvent.getCourseTipEvent());
			mav.addObject(TEACHER_LIST, teacherList);
			
			mav.addObject(TIMESLOT_LIST, TIMES);
			mav.addObject(DAY_WEEK_LIST, getListOfDaysWeek());
			
		} catch(Exception ex) {
			manageGenericError(mav, ex);
		}
		
		return mav;
		
	}
	
	private HashMap<Integer, Object> getListOfDaysWeek() {
		
		HashMap<Integer, Object> listOfDaysWeek = new HashMap<Integer, Object>();
		
		for (int i = 0; i < WEEKDAYS.length; i++) {
			listOfDaysWeek.put(i, WEEKDAYS[i]);
		}
		
		return listOfDaysWeek;
		
	}
	
	@RequestMapping(value=URL_OPTIMIZATION_COURSE_DO, method=RequestMethod.POST)
	public ModelAndView optimizationCourseDo(	HttpServletRequest request, 
												@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - optimizationCourseDo()");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_OPTIMIZATION_RESULT);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		Calendar inizio = Calendar.getInstance();
		
		try {
			
			String resourceId = (String) request.getParameter(PARAMETER_OPTIMIZATION_COURSE_SUBMIT);
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			
			CourseSchedule courseSchedule = getCourseSchedule(resourceIdL, request);
			SolverFactory<CourseSchedule> solverFactory = SolverFactory.createFromXmlResource(PATH_CURRICULUM_SOLVER_CONFIG);
	        SolverConfig solverConfig = solverFactory.getSolverConfig();
	        
	        String secondLimit = (String) request.getParameter(PARAMETER_SECOND_LIMIT);
	        if(secondLimit != null && !secondLimit.isEmpty()) {
	        	solverConfig.getTerminationConfig().setMinutesSpentLimit(null);
		        solverConfig.getTerminationConfig().setSecondsSpentLimit(new Long(secondLimit));
	        } else {
	        	solverConfig.getTerminationConfig().setMinutesSpentLimit(null);
		        solverConfig.getTerminationConfig().setSecondsSpentLimit(15L);
	        }
	        
	        SolverConfigContext solverContext = new SolverConfigContext();
			Solver<CourseSchedule> solver = solverConfig.buildSolver(solverContext);
	        solver.solve(courseSchedule);
	        CourseSchedule bestSolution = (CourseSchedule) solver.getBestSolution();
	        mav.addObject(RESULT_SCORE, bestSolution.getScore().getHardScore() + "hard/" + bestSolution.getScore().getSoftScore() + "soft");
	        Resource resource = resourceService.getResourceById(resourceIdL);
	        List<Resource> resourceList = resourceService.getResourcesByIdGroup(resource.getGroup().getId());
	        mav.addObject(RESOURCE_LIST, resourceList);
			mav.addObject(SELECTED_GROUP, resource.getGroup());
			mav.addObject(FIRST_RESOURCE, resource);
			List<Booking> bookingListFromBestSolution = getBookingsFromCourseSchedule(bestSolution);
			mav.addObject(BOOKING_LIST, bookingListFromBestSolution);
	        
		} catch (Exception ex) {
			manageGenericError(mav, ex);
		}
		Calendar fine = Calendar.getInstance();
		System.out.println("Inizio:" + inizio.getTime());
		System.out.println("Fine:" + fine.getTime());
		return mav;
		
	}
	
	//////////////////////////
	// FROM COURSE SCHEDULE TO BOOKING
	//////////////////////////

	//****** OUTPUT
	/*
	CourseSchedule [
	name=null, 
	teacherList=[N.D., 003909, 002996, 001642, 003287], 
	curriculumList=[N.D., F4I, I4W, I4T], 
	courseList=[
	Course [code=N.D., teacher=N.D., lectureSize=0, minWorkingDaySize=0, curriculumList=[N.D.], studentSize=15], 
	Course [code=N.D., teacher=N.D., lectureSize=2, minWorkingDaySize=2, curriculumList=[N.D.], studentSize=11], 
	Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=26], 
	Course [code=N.D., teacher=N.D., lectureSize=3, minWorkingDaySize=3, curriculumList=[N.D.], studentSize=22], 
	Course [code=N.D., teacher=N.D., lectureSize=4, minWorkingDaySize=4, curriculumList=[N.D.], studentSize=24], 
	Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=15], 
	Course [code=F4I - INFORMATION SYSTEMS AND NETWORK SECURITY - DS9003 - 2015, teacher=003909, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=10], 
	Course [code=F4I - APPLICAZIONI PER DISPOSITIVI MOBILI - F1081 - 2015, teacher=002996, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=13], 
	Course [code=I4W - NETWORK FLOWS - DT0059 - 2016, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4W], studentSize=14], 
	Course [code=I4T - NETWORK OPTIMIZATION - DT0060 - 2015, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4T], studentSize=21], 
	Course [code=F4I - TEORIA DELL'INFORMAZIONE - F0158 - 2015, teacher=003287, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=11]], 
	dayList=[0, 1, 2, 3, 4], 
	timeslotList=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 
	periodList=[0-0, 0-1, 0-2, 0-3, 0-4, 0-5, 0-6, 0-7, 0-8, 0-9, 0-10, 0-11, 0-12, 1-0, 1-1, 1-2, 1-3, 1-4, 1-5, 1-6, 1-7, 1-8, 1-9, 1-10, 1-11, 1-12, 2-0, 2-1, 2-2, 2-3, 2-4, 2-5, 2-6, 2-7, 2-8, 2-9, 2-10, 2-11, 2-12, 3-0, 3-1, 3-2, 3-3, 3-4, 3-5, 3-6, 3-7, 3-8, 3-9, 3-10, 3-11, 3-12, 4-0, 4-1, 4-2, 4-3, 4-4, 4-5, 4-6, 4-7, 4-8, 4-9, 4-10, 4-11, 4-12], roomList=[A1.1, A1.2], 
	unavailablePeriodPenaltyList=[], 
	lectureList=[
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=0, minWorkingDaySize=0, curriculumList=[N.D.], 	studentSize=15], lectureIndexInCourse=0, locked=false, period=0-3, room=A1.2], 
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=2, minWorkingDaySize=2, curriculumList=[N.D.], 	studentSize=11], lectureIndexInCourse=0, locked=false, period=0-2, room=A1.2], 
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], 	studentSize=26], lectureIndexInCourse=0, locked=false, period=0-5, room=A1.1], 
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=3, minWorkingDaySize=3, curriculumList=[N.D.], 	studentSize=22], lectureIndexInCourse=0, locked=false, period=0-1, room=A1.1], 
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=4, minWorkingDaySize=4, curriculumList=[N.D.], 	studentSize=24], lectureIndexInCourse=0, locked=false, period=0-0, room=A1.1], 
	Lecture [course=Course [code=N.D., 																teacher=N.D., 	lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], 	studentSize=15], lectureIndexInCourse=0, locked=false, period=0-4, room=A1.2], 
	Lecture [course=Course [code=F4I - INFORMATION SYSTEMS AND NETWORK SECURITY - DS9003 - 2015, 	teacher=003909, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], 	studentSize=10], lectureIndexInCourse=0, locked=false, period=0-4, room=A1.1], 
	Lecture [course=Course [code=F4I - APPLICAZIONI PER DISPOSITIVI MOBILI - F1081 - 2015, 			teacher=002996, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], 	studentSize=13], lectureIndexInCourse=0, locked=false, period=0-2, room=A1.1], 
	Lecture [course=Course [code=I4W - NETWORK FLOWS - DT0059 - 2016, 								teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4W], 	studentSize=14], lectureIndexInCourse=0, locked=false, period=0-1, room=A1.2], 
	Lecture [course=Course [code=I4T - NETWORK OPTIMIZATION - DT0060 - 2015, 						teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4T], 	studentSize=21], lectureIndexInCourse=0, locked=false, period=0-0, room=A1.2], 
	Lecture [course=Course [code=F4I - TEORIA DELL'INFORMAZIONE - F0158 - 2015, 					teacher=003287, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], 	studentSize=11], lectureIndexInCourse=0, locked=false, period=0-3, room=A1.1]], 
	score=0hard/-59soft]
			*/
	private List<Booking> getBookingsFromCourseSchedule(CourseSchedule bestSolution) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getBookingsFromCourseSchedule(bestSolution: " + bestSolution + ")");
		
		List<Booking> bookingList = new ArrayList<Booking>();
		if(bestSolution != null) {
			
			//Course -> Booking
			//Lecture -> Repeat
			List<Lecture> lectureList = bestSolution.getLectureList();
			if(lectureList != null) {
				for (Lecture lectureTemp : bestSolution.getLectureList()) {
					Booking booking = new Booking();
					booking.setId(lectureTemp.getCourse().getId());
					booking.setSubjectID(lectureTemp.getCourse().getCode());
					booking.setTeacherID(lectureTemp.getCourse().getTeacher().getCode());
					booking.setCdsID(lectureTemp.getCourse().getCurriculumList().get(0).getCode());
					booking.setName(booking.getSubjectID() + " - " + booking.getTeacherID());
					Repeat repeat = new Repeat();
					repeat.setIdBooking(lectureTemp.getCourse().getId());
					repeat.setId(lectureTemp.getId());
					repeat.setIdTipBookingStatus(TipBookingStatus.InLavorazione.getId());
					setRepeatDate(lectureTemp, repeat);
					List<Repeat> repeatList = new ArrayList<Repeat>();
					repeatList.add(repeat);
					booking.setRepeatList(repeatList);
					if(booking.getResource() == null)
						booking.setResource(resourceService.getResourceById(lectureTemp.getRoom().getId()));
					bookingList.add(booking);
				}
			}
			
		}
		
		return bookingList;
		
	}
	
	private void setRepeatDate(Lecture lectureTemp, Repeat repeat) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - setRepeatDate(lectureTemp: " + lectureTemp + ", " + "repeat: " + repeat + ")");
		
		Period period = lectureTemp.getPeriod();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		
		cal.add(Calendar.DATE, period.getDay().getDayIndex());
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(Calendar.DATE, 1);
		
		String hourStart = TIMES[period.getTimeslot().getTimeslotIndex()].substring(0,2);
		String minuteStart = TIMES[period.getTimeslot().getTimeslotIndex()].substring(3,5);
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), Integer.valueOf(hourStart), Integer.valueOf(minuteStart), 0);
		
		Date dataInizio = cal.getTime();
		Date dI = Timestamp.valueOf(dt.format(dataInizio));
		repeat.setEventDateStart(dI);
		
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date dataFine = cal.getTime();
		Date dF = Timestamp.valueOf(dt.format(dataFine));
		repeat.setEventDateEnd(dF);

	}

	//////////////////////////
	// FROM BOOKING TO COURSE SCHEDULE
	//////////////////////////
	
	//****** INPUT
	/*
	CourseSchedule [
	name=null, 
	teacherList=[N.D., 003909, 002996, 001642, 003287], 
	curriculumList=[N.D., F4I, I4W, I4T], 
	courseList=[
	Course [code=N.D., teacher=N.D., lectureSize=0, minWorkingDaySize=0, curriculumList=[N.D.], studentSize=15], 
	Course [code=N.D., teacher=N.D., lectureSize=2, minWorkingDaySize=2, curriculumList=[N.D.], studentSize=11], 
	Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=26], 
	Course [code=N.D., teacher=N.D., lectureSize=3, minWorkingDaySize=3, curriculumList=[N.D.], studentSize=22], 
	Course [code=N.D., teacher=N.D., lectureSize=4, minWorkingDaySize=4, curriculumList=[N.D.], studentSize=24], 
	Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=15], 
	Course [code=F4I - INFORMATION SYSTEMS AND NETWORK SECURITY - DS9003 - 2015, teacher=003909, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=10], 
	Course [code=F4I - APPLICAZIONI PER DISPOSITIVI MOBILI - F1081 - 2015, teacher=002996, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=13], 
	Course [code=I4W - NETWORK FLOWS - DT0059 - 2016, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4W], studentSize=14], 
	Course [code=I4T - NETWORK OPTIMIZATION - DT0060 - 2015, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4T], studentSize=21], 
	Course [code=F4I - TEORIA DELL'INFORMAZIONE - F0158 - 2015, teacher=003287, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=11]], 
	dayList=[0, 1, 2, 3, 4], 
	timeslotList=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], 
	periodList=[0-0, 0-1, 0-2, 0-3, 0-4, 0-5, 0-6, 0-7, 0-8, 0-9, 0-10, 0-11, 0-12, 1-0, 1-1, 1-2, 1-3, 1-4, 1-5, 1-6, 1-7, 1-8, 1-9, 1-10, 1-11, 1-12, 2-0, 2-1, 2-2, 2-3, 2-4, 2-5, 2-6, 2-7, 2-8, 2-9, 2-10, 2-11, 2-12, 3-0, 3-1, 3-2, 3-3, 3-4, 3-5, 3-6, 3-7, 3-8, 3-9, 3-10, 3-11, 3-12, 4-0, 4-1, 4-2, 4-3, 4-4, 4-5, 4-6, 4-7, 4-8, 4-9, 4-10, 4-11, 4-12], 
	roomList=[A1.1, A1.2], 
	unavailablePeriodPenaltyList=[], 
	lectureList=[
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=0, minWorkingDaySize=0, curriculumList=[N.D.], studentSize=15], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=2, minWorkingDaySize=2, curriculumList=[N.D.], studentSize=11], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=26], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=3, minWorkingDaySize=3, curriculumList=[N.D.], studentSize=22], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=4, minWorkingDaySize=4, curriculumList=[N.D.], studentSize=24], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=N.D., teacher=N.D., lectureSize=1, minWorkingDaySize=1, curriculumList=[N.D.], studentSize=15], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=F4I - INFORMATION SYSTEMS AND NETWORK SECURITY - DS9003 - 2015, teacher=003909, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=10], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=F4I - APPLICAZIONI PER DISPOSITIVI MOBILI - F1081 - 2015, teacher=002996, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=13], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=I4W - NETWORK FLOWS - DT0059 - 2016, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4W], studentSize=14], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=I4T - NETWORK OPTIMIZATION - DT0060 - 2015, teacher=001642, lectureSize=2, minWorkingDaySize=2, curriculumList=[I4T], studentSize=21], lectureIndexInCourse=0, locked=false, period=null, room=null], 
	Lecture [course=Course [code=F4I - TEORIA DELL'INFORMAZIONE - F0158 - 2015, teacher=003287, lectureSize=2, minWorkingDaySize=2, curriculumList=[F4I], studentSize=11], lectureIndexInCourse=0, locked=false, period=null, room=null]], 
	score=null]
			*/
	private CourseSchedule getCourseSchedule(Long resourceId, HttpServletRequest request) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getCourseSchedule(resourceId: " + resourceId + ")");
		
		CourseSchedule courseScheduleInput = new CourseSchedule();
		
		Resource resource = resourceService.getResourceById(resourceId);
		List<Group> groupList = new ArrayList<Group>();
		groupList.add(resource.getGroup());
		
		List<String> teacherList = bookingService.getDifferentTeacherIdByIdGroupAndTipEvent(resource.getGroup().getId(), TipEvent.getCourseTipEvent());
		
		List<Booking> bookingList = bookingService.getAllBookingsByIdGroup(resource.getGroup().getId(), TipEvent.getCourseTipEvent());
				
		//CurriculumList
		courseScheduleInput.setCurriculumList(getCurriculumList(bookingList));
		
		//RoomList
		courseScheduleInput.setRoomList(getRoomListCourse(resourceService.getResourcesByIdGroup(resource.getGroup().getId())));
		
		//TeacherList
		courseScheduleInput.setTeacherList(getTeacherList(bookingList));
		
		//TimesLotList
		courseScheduleInput.setTimeslotList(getTimesList(5));

		//DayList
		List<Day> dayList = getDayList(5, 5);
		courseScheduleInput.setDayList(dayList);

		//PeriodList
		courseScheduleInput.setPeriodList(getPeriodList(courseScheduleInput.getDayList(), courseScheduleInput.getTimeslotList()));

		//CourseList - LectureList
		setCourseLectureList(bookingList, courseScheduleInput);
		
		//UnavailablePeriodPenalty
		setUnavaiblePeriod(courseScheduleInput, teacherList, request);

		return courseScheduleInput;
		
	}
	
	private List<Course> getCourseFromTeacher(String teacherID, CourseSchedule courseScheduleInput) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getCourseFromTeacher(teacherID: " + teacherID + ", courseScheduleInput: " + courseScheduleInput + ")");
		
		List<Course> courseList = new ArrayList<Course>();
		
		for (Course course : courseScheduleInput.getCourseList()) {
			if(course.getTeacher().getCode().equals(teacherID))
				courseList.add(course);
		}
		
		return courseList;
		
	}
	
	private void setUnavaiblePeriod(CourseSchedule courseScheduleInput, List<String> teacherList, HttpServletRequest request) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - setUnavaiblePeriod(courseScheduleInput: " + courseScheduleInput + ", teacherList: " + teacherList + ")");
		
		List<UnavailablePeriodPenalty> unavailablePeriodPenaltyList = new ArrayList<>(courseScheduleInput.getCourseList().size());
        long penaltyId = 0L;
        
        for (String teacherTemp : teacherList) {
			if(!teacherTemp.equalsIgnoreCase(N_D)) {
				List<Course> courseList = getCourseFromTeacher(teacherTemp, courseScheduleInput);
				if(courseList != null) {
					String [] periodList = request.getParameterValues(teacherTemp);
					String [] dayList = request.getParameterValues(teacherTemp + _DAY);
					if(periodList != null && dayList != null) {
						for (Course course : courseList) {
							for (int i = 0; i < periodList.length; i++) {
								for (int j = 0; j < dayList.length; j++) {
									UnavailablePeriodPenalty penalty = new UnavailablePeriodPenalty();
						            penalty.setId(penaltyId);
						            penalty.setCourse(course);
						            penalty.setPeriod(getPeriodByDayAndTimesLot(courseScheduleInput, j, i));
						            unavailablePeriodPenaltyList.add(penalty);
						            penaltyId++;	
								}
							}
						}
					}
				}
			}
		}
        
        courseScheduleInput.setUnavailablePeriodPenaltyList(unavailablePeriodPenaltyList);
		
	}
	
	private Period getPeriodByDayAndTimesLot(CourseSchedule courseScheduleInput, int dayIndex, int timeslotIndex) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getPeriodByDayAndTimesLot(courseScheduleInput: " + courseScheduleInput + ", dayIndex: " + dayIndex + ", timeslotIndex: " + timeslotIndex + ")");
		
		List<Period> periodList = courseScheduleInput.getPeriodList();
		if(periodList != null && !periodList.isEmpty()) {
			for (Period period : periodList) {
				if(period.getDay().getDayIndex() == dayIndex && period.getTimeslot().getTimeslotIndex() == timeslotIndex)
					return period;
			}
		}
		return null;
		
	}

	private void setCourseLectureList(List<Booking> bookingList, CourseSchedule courseScheduleInput) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - setCourseLectureList(bookingList: " + bookingList + ", courseScheduleInput: " + courseScheduleInput + ")");
		
		List<Course> courseList = new ArrayList<Course>();
		List<Lecture> lectureList = new ArrayList<Lecture>();
		
		//Booking -> Course
		//Repeat -> Lecture
		if(bookingList != null && !bookingList.isEmpty()) {
			for (Booking booking : bookingList) {
				Course courseTemp = new Course();
				if(booking.getRepeatList() != null) {
					
					//1 course has multiple lectures
					int i = 1;
					int lectureSize = booking.getRepeatList().size();
					for (Repeat repeat : booking.getRepeatList()) {
						
						courseTemp.setId(booking.getId());
						Curriculum curriculumTemp = getCurriculumFromBooking(booking, courseScheduleInput);
						List<Curriculum> curriculumListTemp = new ArrayList<Curriculum>();
						curriculumListTemp.add(curriculumTemp);
						courseTemp.setCurriculumList(curriculumListTemp);
						
						courseTemp.setMinWorkingDaySize(lectureSize);
						
						courseTemp.setStudentSize(booking.getNumStudents());
						courseTemp.setTeacher(getTeacherFromId(booking.getTeacherID(), courseScheduleInput.getTeacherList()));
						courseTemp.setCode(booking.getSubjectID());
						courseTemp.setLectureSize(lectureSize);
						
						Lecture lecture = new Lecture();
						lecture.setLocked(false);
						lecture.setId(repeat.getId());
						lecture.setCourse(courseTemp);
						lecture.setLectureIndexInCourse(i++);
						lectureList.add(lecture);
						
					}
				}
				courseList.add(courseTemp);
			}
		}
		
		courseScheduleInput.setCourseList(courseList);
		courseScheduleInput.setLectureList(lectureList);
		
	}
	
	@SuppressWarnings("unused")
	private int getRepeatDurationHours(Repeat repeat) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getRepeatDuration(repeat: " + repeat + ")");
		
		long diff = repeat.getEventDateEnd().getTime() - repeat.getEventDateStart().getTime();

		long diffSeconds = diff / 1000 % 60;  
		long diffMinutes = diff / (60 * 1000) % 60;     
		long diffHours = diff / (60 * 60 * 1000);    
		
		if(diffSeconds > 0)
			diffMinutes++;
		
		if(diffMinutes > 0)
			diffHours++;
		
		return (int) diffHours;
		
	}
	
	private int getRepeatDurationMinutes(Repeat repeat) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getRepeatDuration(repeat: " + repeat + ")");
		
		long diff = repeat.getEventDateEnd().getTime() - repeat.getEventDateStart().getTime();

		long diffSeconds = diff / 1000;  
		long diffMinutes = diffSeconds / 60;     
		
		return (int) diffMinutes;
		
	}
	
	private List<Room> getRoomListCourse(List<Resource> resourceList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getRoomListCourse(resourceList: " + resourceList + ")");
		
		List<Room> roomList = new ArrayList<Room>();
		if(resourceList != null && !resourceList.isEmpty()) {
			for (Resource resource : resourceList) {
				Room roomTemp = new Room();
				roomTemp.setId(resource.getId());
				roomTemp.setCapacity(resource.getCapacity());
				roomTemp.setCode(resource.getName());
				roomList.add(roomTemp);
			}
		}
		return roomList;
		
	}
	
	private List<Teacher> getTeacherList(List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getTeacherList(bookingList: " + bookingList + ")");
		
		List<String> teacherListString = new ArrayList<String>();
		if(bookingList != null && !bookingList.isEmpty()) {
			for (Booking booking : bookingList) {
				if(teacherListString.isEmpty()) {
					teacherListString.add(booking.getTeacherID());
				} else {
					boolean contains = false;
					for (String teacherTemp : teacherListString) {
						if(teacherTemp.equalsIgnoreCase(booking.getTeacherID())) {
							contains = true;
							break;
						}
					}
					if(!contains)
						teacherListString.add(booking.getTeacherID());
				}
			}
		}
		
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if(teacherListString != null && !teacherListString.isEmpty()) {
			Long id = 0L;
			for (String teacherString : teacherListString) {
				Teacher teacher = new Teacher();
				teacher.setId(id++);
				teacher.setCode(teacherString);
				teacherList.add(teacher);
			}
		}
		return teacherList;
		
	}
	
	private List<Day> getDayList(int numDay, int numsLot) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getDayList(numDay: " + numDay + ", numsLot" + numsLot + ")");
		
		List<Day> dayList = new ArrayList<Day>();
		int i = 0;
		while(numDay > i) {
			Day dayTemp = new Day();
			dayTemp.setId(new Long(i));
			dayTemp.setDayIndex(i);
			i++;
			dayTemp.setPeriodList(new ArrayList<Period>(numsLot));
			dayList.add(dayTemp);
		}
		return dayList;
		
	}
	
	private List<Curriculum> getCurriculumList(List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getCurriculumList(bookingList: " + bookingList + ")");
		
		List<Curriculum> curriculumList = new ArrayList<Curriculum>();
		if(bookingList != null && !bookingList.isEmpty()) {
			int i = 0;
			for (Booking booking : bookingList) {
				Curriculum curriculum = new Curriculum();
				curriculum.setCode(booking.getCdsID());
				if(curriculumList.isEmpty()) {
					curriculum.setId((long)i++);
					curriculumList.add(curriculum);
				} else {
					boolean contains = false;
					for (Curriculum curriculumTemp : curriculumList) {
						if(curriculumTemp.getCode().equalsIgnoreCase(booking.getCdsID())) {
							contains = true;
							break;
						}
					}
					if(!contains) {
						curriculum.setId((long)i++);
						curriculumList.add(curriculum);
					}
				}
			}
		}
		return curriculumList;
		
	}
	
	private List<Timeslot> getTimesList(int numsLot) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getTimesList(numsLot: " + numsLot + ")");
		
		List<Timeslot> timesLotList = new ArrayList<Timeslot>();
		int i = 0;
		while(numsLot > i) {
			Timeslot timeslot = new Timeslot();
			timeslot.setId(new Long(i));
			timeslot.setTimeslotIndex(i);
			i++;
			timesLotList.add(timeslot);
		}
		return timesLotList;
		
	}
	
	private List<Period> getPeriodList(List<Day> dayList,
			List<Timeslot> timeslotList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getPeriodList(dayList: " + dayList + ", timeslotList: " + timeslotList + ")");
		
		List<Period> periodList = new ArrayList<Period>();
		if(dayList != null && !dayList.isEmpty() & timeslotList != null && !timeslotList.isEmpty()) {
			int i = 0;
			for (Day day : dayList) {
				for (Timeslot timeslot : timeslotList) {
//					if (day.getDayIndex() == 0 && timeslot.getTimeslotIndex() >= 2) {
//	                    // No lectures Monday afternoon
//	                    continue;
//	                }
//					if (day.getDayIndex() == 1 && timeslot.getTimeslotIndex() < 2) {
//	                    // No lectures Monday afternoon
//	                    continue;
//	                }
					Period periodTemp = new Period();
					periodTemp.setId(new Long(i++));
					periodTemp.setDay(day);
					periodTemp.setTimeslot(timeslot);
					day.getPeriodList().add(periodTemp);
					periodList.add(periodTemp);
				}
			}
		}
		return periodList;

	}
	
	private Teacher getTeacherFromId(String teacherID, List<Teacher> teacherList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getTeacherFromId(teacherID: " + teacherID + ", teacherList: " + teacherList + ")");
		
		if(teacherList != null && !teacherList.isEmpty()) {
			for (Teacher teacher : teacherList) {
				if(teacher.getCode().equals(teacherID))
					return teacher;
			}
		}
		return null;
		
	}
	
	private Curriculum getCurriculumFromBooking(Booking booking, CourseSchedule courseScheduleInput) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getCurriculumFromBooking(booking: " + booking + ", courseScheduleInput: " + courseScheduleInput + ")");
		
		if(courseScheduleInput != null && courseScheduleInput.getCurriculumList() != null && !courseScheduleInput.getCurriculumList().isEmpty()) {
			for (Curriculum curriculum : courseScheduleInput.getCurriculumList()) {
				if(curriculum.getCode().equalsIgnoreCase(booking.getCdsID()))
					return curriculum;
			}
		}
		return null;
		
	}
	
	
	////////////////////////
	//Optimize Exams
	////////////////////////
	@RequestMapping(value=URL_EXAM_INSERT_CONSTRAINT_DO, method=RequestMethod.POST)
	public ModelAndView examInsertConstraint(	HttpServletRequest request, 
												@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - examInsertConstraint()");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		mav.setViewName(VIEW_ADMIN_EXAM_INSERT_CONSTRAINT);
		
		try {
			
			String resourceId = (String) request.getParameter(PARAMETER_OPTIMIZATION_EXAM_SUBMIT);
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			mav.addObject(ID_RESOURCE, resourceIdL);
			
			List<String> teacherList = bookingService.getDifferentTeacherIdByIdGroupAndTipEvent(resourceService.getResourceById(resourceIdL).getGroup().getId(), TipEvent.getExaminationTipEvent());
			mav.addObject(TEACHER_LIST, teacherList);
			
			mav.addObject(TIMESLOT_LIST, TIMES);
			mav.addObject(DAY_WEEK_LIST, getListOfDaysWeek());
			
		} catch(Exception ex) {
			manageGenericError(mav, ex);
		}
		
		return mav;
		
	}
	
	@RequestMapping(value=URL_OPTIMIZATION_EXAM_DO, method=RequestMethod.POST)
	public ModelAndView optimizationExamDo(	HttpServletRequest request, 
											@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - optimizationExamDo()");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_OPTIMIZATION_RESULT);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		
		Calendar inizio = Calendar.getInstance();
		
		try {
			
			String resourceId = (String) request.getParameter(PARAMETER_OPTIMIZATION_EXAM_SUBMIT);
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			
			Examination examination = getExamination(resourceIdL, request);
			SolverFactory<Examination> solverFactory = SolverFactory.createFromXmlResource(PATH_EXAM_SOLVER_CONFIG);
	        SolverConfig solverConfig = solverFactory.getSolverConfig();
	        
			String secondLimit = (String) request.getParameter(PARAMETER_SECOND_LIMIT);
			if(secondLimit != null && !secondLimit.isEmpty()) {
	        	solverConfig.getTerminationConfig().setMinutesSpentLimit(null);
		        solverConfig.getTerminationConfig().setSecondsSpentLimit(Long.getLong(secondLimit));
	        } else {
	        	solverConfig.getTerminationConfig().setMinutesSpentLimit(null);
		        solverConfig.getTerminationConfig().setSecondsSpentLimit(15L);
	        }
			
			SolverConfigContext solverContext = new SolverConfigContext();
			Solver<Examination> solver = solverConfig.buildSolver(solverContext);
			
			solver.solve(examination);
			
			Examination bestSolution = (Examination) solver.getBestSolution();
			System.out.println(bestSolution);
			
			Resource resource = resourceService.getResourceById(resourceIdL);
	        List<Resource> resourceList = resourceService.getResourcesByIdGroup(resource.getGroup().getId());
	        mav.addObject(RESOURCE_LIST, resourceList);
			mav.addObject(SELECTED_GROUP, resource.getGroup());
			mav.addObject(FIRST_RESOURCE, resource);
			List<Booking> bookingListFromBestSolution = getBookingsFromExamination(bestSolution);
			mav.addObject(BOOKING_LIST, bookingListFromBestSolution);
			
		} catch(Exception ex) {
			manageGenericError(mav, ex);
		}
		
		Calendar fine = Calendar.getInstance();
		System.out.println("Inizio:" + inizio.getTime());
		System.out.println("Fine:" + fine.getTime());
		
		return mav;
		
	}
	
	private List<Booking> getBookingsFromExamination(Examination bestSolution) {
		
		List<Booking> bookingList = new ArrayList<Booking>();
		
		
		return bookingList;
		
	}

	private Examination getExamination(Long resourceId, HttpServletRequest request) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getExamination(resourceId: " + resourceId + ")");
		
		Examination examination = new Examination();
		
		Map<Topic, Set<Topic>> coincidenceMap = new HashMap<Topic, Set<Topic>>();
		
		Resource resource = resourceService.getResourceById(resourceId);
		
		List<TipEvent> tipEventList = new ArrayList<TipEvent>();
		tipEventList.add(TipEvent.Esame);
		List<Booking> bookingList = bookingService.getAllBookingsByIdGroup(resource.getGroup().getId(), tipEventList);
		
		//roomList
		examination.setRoomList(getRoomListExamination(resourceService.getResourcesByIdGroup(resource.getGroup().getId())));
		
		//studentList
		examination.setStudentList(getStudentList(bookingList));
		
		//periodList
		examination.setPeriodList(getPeriodListExamination(bookingList));
		
		//topicList
		examination.setTopicList(getTopicList(bookingList, examination, coincidenceMap));
		
		//periodPenaltyList
		examination.setPeriodPenaltyList(getPeriodPenaltyList(request, bookingList));
		
		//roomPenaltyList
		examination.setRoomPenaltyList(getRoomPenaltyList(examination.getTopicList()));
		
		//examList
	    examination.setExamList(getExamList(examination, bookingList, coincidenceMap));
		
		return examination;
		
	}
	
	private List<Exam> getExamList(Examination examination, List<Booking> bookingList, Map<Topic, Set<Topic>> coincidenceMap) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getExamList(examination: " + examination + ", " + bookingList + ", coincidenceMap: " + coincidenceMap + ")");
		
		List<Topic> topicList = examination.getTopicList();
        List<Exam> examList = new ArrayList<>(topicList.size());
        Map<Topic, LeadingExam> leadingTopicToExamMap = new HashMap<>(topicList.size());
        for (Topic topic : topicList) {
        	Exam exam;
            Topic leadingTopic = topic;
        	for (Topic coincidenceTopic : coincidenceMap.get(topic)) {
                if (coincidenceTopic.getId() < leadingTopic.getId()) {
                    leadingTopic = coincidenceTopic;
                }
            }
            if (leadingTopic == topic) {
                LeadingExam leadingExam = new LeadingExam();
                leadingExam.setFollowingExamList(new ArrayList<FollowingExam>(10));
                leadingTopicToExamMap.put(topic, leadingExam);
                exam = leadingExam;
            } else {
                FollowingExam followingExam = new FollowingExam();
                LeadingExam leadingExam = leadingTopicToExamMap.get(leadingTopic);
                if (leadingExam == null) {
                    throw new IllegalStateException("The followingExam (" + topic.getId()
                            + ")'s leadingExam (" + leadingExam + ") cannot be null.");
                }
                followingExam.setLeadingExam(leadingExam);
                leadingExam.getFollowingExamList().add(followingExam);
                exam = followingExam;
            }
            exam.setId(topic.getId());
            exam.setTopic(topic);
            // Notice that we leave the PlanningVariable properties on null
            examList.add(exam);
        	
        	
        	
//            Exam exam;
//            LeadingExam leadingExam = new LeadingExam();
//            leadingExam.setFollowingExamList(new ArrayList<FollowingExam>(10));
//            exam = leadingExam;
//            exam.setId(topic.getId());
//            exam.setTopic(topic);
//            // Notice that we leave the PlanningVariable properties on null
//            examList.add(exam);
        }
        examination.setExamList(examList);
		
		return examList;
		
	}
	
	private List<RoomPenalty> getRoomPenaltyList(List<Topic> topicList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getRoomPenaltyList(topicList: " + topicList + ")");
		
		List<RoomPenalty> roomPenaltyList = new ArrayList<RoomPenalty>();
//		long id = 0;
//		if(topicList != null) {
//			for (Topic topic : topicList) {
//				RoomPenalty roomPenalty = new RoomPenalty();
//				roomPenalty.setId(id++);
//				roomPenalty.setTopic(topic);
//				roomPenalty.setRoomPenaltyType(RoomPenaltyType.ROOM_EXCLUSIVE);
//				roomPenaltyList.add(roomPenalty);
//			}
//		}
		
		return roomPenaltyList;
		
	}

	private List<PeriodPenalty> getPeriodPenaltyList(HttpServletRequest request, List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getPeriodPenaltyList()");
		
		List<PeriodPenalty> periodPenalityList = new ArrayList<PeriodPenalty>();
		
//		long penaltyId = 0L;
//
//		List<String> teacherList = getTeacherListExamination(bookingList);
//        for (String teacherTemp : teacherList) {
//			if(!teacherTemp.equalsIgnoreCase(N_D)) {
//				String [] periodList = request.getParameterValues(teacherTemp);
//				String [] dayList = request.getParameterValues(teacherTemp + _DAY);
//				
//				PeriodPenalty periodPenalityTemp = new PeriodPenalty();
//				periodPenalityTemp.setId(penaltyId++);
//				periodPenalityList.add(periodPenalityTemp);
//				
//			}
//		}
		
		return periodPenalityList;
		
	}
	
	private List<String> getTeacherListExamination(List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getTeacherListExamination(bookingList: " + bookingList + ")");
		
		List<String> teacherListString = new ArrayList<String>();
		if(bookingList != null && !bookingList.isEmpty()) {
			for (Booking booking : bookingList) {
				if(teacherListString.isEmpty()) {
					teacherListString.add(booking.getTeacherID());
				} else {
					boolean contains = false;
					for (String teacherTemp : teacherListString) {
						if(teacherTemp.equalsIgnoreCase(booking.getTeacherID())) {
							contains = true;
							break;
						}
					}
					if(!contains)
						teacherListString.add(booking.getTeacherID());
				}
			}
		}
		
		return teacherListString;
	}
	
	private List<Topic> getTopicList(List<Booking> bookingList, Examination examination, Map<Topic, Set<Topic>> coincidenceMap) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getTopicList(bookingList: " + bookingList + ", examination:" + examination + ")");
		
		List<Topic> topicList = new ArrayList<Topic>();
		
		long i = 0;
		if(bookingList != null) {
			for (Booking booking : bookingList) {
				
				for (Repeat repeat : booking.getRepeatList()) {
					Topic topicTemp = new Topic();
					topicTemp.setId(i++);
					topicTemp.setDuration(getRepeatDurationMinutes(repeat));
					//All the topics have the same students
					topicTemp.setStudentList(examination.getStudentList());
					topicList.add(topicTemp);
					coincidenceMap.put(topicTemp, new HashSet<Topic>());
				}
				
			}
		}
		
		return topicList;
		
	}
	
	private List<org.optaplanner.examples.examination.domain.Period> getPeriodListExamination(List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getPeriodListExamination(bookingList: " + bookingList + ")");
		
		List<org.optaplanner.examples.examination.domain.Period> periodList = new ArrayList<org.optaplanner.examples.examination.domain.Period>();
		
		if(bookingList != null) {
			for (Booking booking : bookingList) {
				for (Repeat repeat : booking.getRepeatList()) {
					org.optaplanner.examples.examination.domain.Period periodTemp = new org.optaplanner.examples.examination.domain.Period();
					periodTemp.setId(repeat.getId());
					periodTemp.setDuration(getRepeatDurationMinutes(repeat));
					periodList.add(periodTemp);
				}
			}
		}
		
		return periodList;
		
	}
	
	private List<Student> getStudentList(List<Booking> bookingList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getStudentList(bookingList: " + bookingList + ")");
		
		List<Student> studentList = new ArrayList<Student>();
		int maxNumStudents = 0;
		if(bookingList != null) {
			for (Booking booking : bookingList) {
				if(booking.getNumStudents() > maxNumStudents) {
					maxNumStudents = booking.getNumStudents();
				}
			}
		}
		for (int i = 1; i <= maxNumStudents; i++) {
			Student student = new Student();
			student.setId((long)i);
			studentList.add(student);
		}
		return studentList;
		
	}
	
//	private int randomWithRange(int min, int max) {
//	   int range = (max - min) + 1;     
//	   return (int)(Math.random() * range) + min;
//	}

	private List<org.optaplanner.examples.examination.domain.Room> getRoomListExamination(List<Resource> resourceList) {
		
		if(logger.isDebugEnabled())
			logger.debug("AdminController - getRoomListExamination(resourceList: " + resourceList + ")");
		
		List<org.optaplanner.examples.examination.domain.Room> roomList = new ArrayList<org.optaplanner.examples.examination.domain.Room>();
		if(resourceList != null && !resourceList.isEmpty()) {
			for (Resource resource : resourceList) {
				org.optaplanner.examples.examination.domain.Room roomTemp = new org.optaplanner.examples.examination.domain.Room();
				roomTemp.setId(resource.getId());
				roomTemp.setCapacity(resource.getCapacity());
				roomList.add(roomTemp);
			}
		}
		return roomList;
		
	}
		
}
