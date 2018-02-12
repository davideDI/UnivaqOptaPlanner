package it.univaq.planner.presentation;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
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

@Controller
@RequestMapping(value="/admin")
public class AdminController extends ABaseController {

	@RequestMapping(value="/optimization.do", method=RequestMethod.POST)
	public ModelAndView optimizationBookings(	HttpServletRequest request, 
												@CookieValue(LOCALIZATION_COOKIE) String localizationCookie) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_CALENDAR_RESOURCE);
		mav.addObject(LOCALIZATION_COOKIE, localizationCookie);
		Calendar inizio = Calendar.getInstance();
		try {
			
			String resourceId = (String) request.getParameter("optimizationSubmit");
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			
			CourseSchedule courseSchedule = getCourseSchedule(resourceIdL);
			System.out.println(courseSchedule);
			SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
	        Solver solver = solverFactory.buildSolver();
	        solver.solve(courseSchedule);
	        CourseSchedule bestSolution = (CourseSchedule) solver.getBestSolution();
			System.out.println(bestSolution);
	        Resource resource = resourceService.getResourceById(resourceIdL);
	        List<Resource> resourceList = resourceService.getResourcesByIdGroup(resource.getGroup().getId());
	        mav.addObject(RESOURCE_LIST, resourceList);
			mav.addObject(SELECTED_GROUP, resource.getGroup());
			mav.addObject(FIRST_RESOURCE, resource);
			List<Booking> bookingListFromBestSolution = getBookingsFromCourseSchedule(bestSolution);
			mav.addObject(BOOKING_LIST, getBookingByResource(bookingListFromBestSolution, resource));
			
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Calendar fine = Calendar.getInstance();
		System.out.println("Inizio:" + inizio);
		System.out.println("Fine:" + fine);
		return mav;
		
	}
	
	
	//////////////////////////
	// FROM COURSE SCHEDULE TO BOOKING
	//////////////////////////
	
	private List<Booking> getBookingByResource(List<Booking> bookingListFromBestSolution, Resource resource) {
		
		List<Booking> filteredList = new ArrayList<Booking>();
		if(bookingListFromBestSolution != null) {
			for (Booking booking : bookingListFromBestSolution) {
				if(!booking.getResource().getId().equals(resource.getId()))
					filteredList.add(booking);
			}
		}
		return filteredList;
		
	}

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
		
		List<Booking> bookingList = new ArrayList<Booking>();
		if(bestSolution != null) {
			
			//Course -> Booking
			//Lecture -> Repeat
			List<Course> courseList = bestSolution.getCourseList();
			List<Lecture> lectureList = bestSolution.getLectureList();
			if(lectureList != null) {
				for (Lecture lectureTemp : bestSolution.getLectureList()) {
					Booking booking = new Booking();
					booking.setId(lectureTemp.getCourse().getId());
					booking.setSubjectID(lectureTemp.getCourse().getCode());
					booking.setTeacherID(lectureTemp.getCourse().getTeacher().getCode());
					booking.setCdsID(lectureTemp.getCourse().getCurriculumList().get(0).getCode());
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
			
			
			if(courseList != null && !courseList.isEmpty()) {
				for (Course course : courseList) {
					Booking booking = new Booking();
					booking.setId(course.getId());
					booking.setSubjectID(course.getCode());
					booking.setTeacherID(course.getTeacher().getCode());
					booking.setCdsID(course.getCurriculumList().get(0).getCode());
					if(bestSolution.getLectureList() != null && !bestSolution.getLectureList().isEmpty()) {
						for (Lecture lectureTemp : bestSolution.getLectureList()) {
							Repeat repeat = new Repeat();
							repeat.setIdBooking(course.getId());
							repeat.setId(lectureTemp.getId());
							repeat.setIdTipBookingStatus(TipBookingStatus.InLavorazione.getId());
							setRepeatDate(lectureTemp, repeat);
							List<Repeat> repeatList = new ArrayList<Repeat>();
							repeatList.add(repeat);
							booking.setRepeatList(repeatList);
							if(booking.getResource() == null)
								booking.setResource(resourceService.getResourceById(lectureTemp.getRoom().getId()));
						}
					}
					bookingList.add(booking);
				}
			}
			
		}
		
		System.out.println(bookingList);
		return bookingList;
		
	}
	
	private static final String[] TIMES = {"09:00", "11:00", "14:00", "16:00", "18:00", "20:00"};
	
	private void setRepeatDate(Lecture lectureTemp, Repeat repeat) {
		
		Period period = lectureTemp.getPeriod();
		System.out.println(period);
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		Calendar cal = GregorianCalendar.getInstance();
		System.out.println(cal.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		System.out.println(cal.getTime());
		
		cal.add(Calendar.DATE, period.getDay().getDayIndex());
		System.out.println(cal.getTime());
		System.out.println(cal.get(Calendar.HOUR_OF_DAY));
		System.out.println(cal.get(Calendar.MINUTE));
		System.out.println(cal.get(Calendar.SECOND));
		
		String hourStart = TIMES[period.getTimeslot().getTimeslotIndex()].substring(0,2);
		String minuteStart = TIMES[period.getTimeslot().getTimeslotIndex()].substring(3,5);
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), Integer.valueOf(hourStart), Integer.valueOf(minuteStart), 0);
		System.out.println(cal.getTime());
		
		Date dataInizio = cal.getTime();
		System.out.println(dt.format(dataInizio));
		Date dI = Timestamp.valueOf(dt.format(dataInizio));
		System.out.println(dI);
		repeat.setEventDateStart(dI);
		
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date dataFine = cal.getTime();
		System.out.println(dataFine);
		System.out.println(dt.format(dataFine));
		Date dF = Timestamp.valueOf(dt.format(dataFine));
		System.out.println(dF);
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
	private CourseSchedule getCourseSchedule(Long resourceId) throws Exception {
		
		CourseSchedule courseScheduleInput = new CourseSchedule();
		
		Resource resource = resourceService.getResourceById(resourceId);
		List<Group> groupList = new ArrayList<Group>();
		groupList.add(resource.getGroup());
		
		List<Booking> bookingList = bookingService.getAllBookingsByIdGroup(resource.getGroup().getId());
				
		//CurriculumList
		courseScheduleInput.setCurriculumList(getCurriculumList(bookingList));
		
		//RoomList
		courseScheduleInput.setRoomList(getRoomList(resourceService.getResourcesByIdGroup(resource.getGroup().getId())));
		
		//TeacherList
		courseScheduleInput.setTeacherList(getTeacherList(bookingList));
		
		//TimesLotList
		courseScheduleInput.setTimeslotList(getTimesList(7));

		//DayList
		List<Day> dayList = getDayList(5);
		courseScheduleInput.setDayList(dayList);

		//PeriodList
		courseScheduleInput.setPeriodList(getPeriodList(courseScheduleInput.getDayList(), courseScheduleInput.getTimeslotList()));

		//CourseList - LectureList
		setCourseLectureList(bookingList, courseScheduleInput);
		
		courseScheduleInput.setUnavailablePeriodPenaltyList(new ArrayList<UnavailablePeriodPenalty>());

		return courseScheduleInput;
		
	}
	
	private void setCourseLectureList(List<Booking> bookingList, CourseSchedule courseScheduleInput) {
		
		List<Course> courseList = new ArrayList<Course>();
		List<Lecture> lectureList = new ArrayList<Lecture>();
		
		//Booking -> Course
		//Repeat -> Lecture
		if(bookingList != null && !bookingList.isEmpty()) {
			for (Booking booking : bookingList) {
				Course courseTemp = new Course();
				if(booking.getRepeatList() != null) {
					
					//for lecture index
					int i = 0;
					for (Repeat repeat : booking.getRepeatList()) {
						
						courseTemp.setId(booking.getId());
						Curriculum curriculumTemp = getCurriculumFromBooking(booking, courseScheduleInput);
						List<Curriculum> curriculumListTemp = new ArrayList<Curriculum>();
						curriculumListTemp.add(curriculumTemp);
						courseTemp.setCurriculumList(curriculumListTemp);
						
						courseTemp.setMinWorkingDaySize(getRepeatDuration(repeat));
						
						courseTemp.setStudentSize(booking.getNumStudents());
						courseTemp.setTeacher(getTeacherFromId(booking.getTeacherID(), courseScheduleInput.getTeacherList()));
						courseTemp.setCode(booking.getSubjectID());
						courseTemp.setLectureSize(getRepeatDuration(repeat));
						
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
	
	private int getRepeatDuration(Repeat repeat) {
		
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
	
	private List<Room> getRoomList(List<Resource> resourceList) {
		
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
	
	//TODO
	//manage vacation
	private List<Day> getDayList(int numDay) {
		
		List<Day> dayList = new ArrayList<Day>();
		int i = 0;
		while(numDay > i) {
			Day dayTemp = new Day();
			dayTemp.setId(new Long(i));
			dayTemp.setDayIndex(i);
			i++;
			dayList.add(dayTemp);
		}
		return dayList;
		
	}
	
	private List<Curriculum> getCurriculumList(List<Booking> bookingList) {
		
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
		
		List<Period> periodList = new ArrayList<Period>();
		if(dayList != null && !dayList.isEmpty() & timeslotList != null && !timeslotList.isEmpty()) {
			int i = 0;
			for (Day day : dayList) {
				for (Timeslot timeslot : timeslotList) {
					Period periodTemp = new Period();
					periodTemp.setId(new Long(i++));
					periodTemp.setDay(day);
					periodTemp.setTimeslot(timeslot);
					periodList.add(periodTemp);
				}
			}
		}
		return periodList;

	}
	
	
	private Teacher getTeacherFromId(String teacherID, List<Teacher> teacherList) {
		
		if(teacherList != null && !teacherList.isEmpty()) {
			for (Teacher teacher : teacherList) {
				if(teacher.getCode().equals(teacherID))
					return teacher;
			}
		}
		return null;
		
	}
	
	private Curriculum getCurriculumFromBooking(Booking booking, CourseSchedule courseScheduleInput) {
		
		if(courseScheduleInput != null && courseScheduleInput.getCurriculumList() != null && !courseScheduleInput.getCurriculumList().isEmpty()) {
			for (Curriculum curriculum : courseScheduleInput.getCurriculumList()) {
				if(curriculum.getCode().equalsIgnoreCase(booking.getCdsID()))
					return curriculum;
			}
		}
		return null;
		
	}
		
}
