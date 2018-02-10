package it.univaq.planner.presentation;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.Group;
import it.univaq.planner.business.model.Resource;

@Controller
@RequestMapping(value="/admin")
public class AdminController extends ABaseController {

	@RequestMapping(value="/optimization.do", method=RequestMethod.POST)
	public ModelAndView optimizationBookings(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(VIEW_COMMON_INDEX);
		
		try {
			
			String resourceId = (String) request.getParameter("optimizationSubmit");
			Long resourceIdL = 0L;
			if(resourceId != null && !resourceId.isEmpty()) {
				resourceIdL = new Long(resourceId);
			}
			
			CourseSchedule courseSchedule = getCourseSchedule(resourceIdL);
			
			SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
	        Solver solver = solverFactory.buildSolver();
	        solver.solve(courseSchedule);
	        CourseSchedule bestSolution = (CourseSchedule) solver.getBestSolution();
			
	        /*
			List<Resource> resourceList = resourceService.getResourcesByIdGroup(new Long(idGroup));
			mav.addObject(RESOURCE_LIST, resourceList);
			
			if(resourceList != null && !resourceList.isEmpty()) {
				Resource selectedResource = resourceList.get(0);
				mav.addObject(FIRST_RESOURCE, selectedResource);
				mav.addObject(BOOKING_LIST, bookingService.getAllBookingsByIdResource(selectedResource.getId()));
			}
	         */
	        Resource resource = resourceService.getResourceById(resourceIdL);
	        List<Resource> resourceList = resourceService.getResourcesByIdGroup(resource.getGroup().getId());
	        mav.addObject(RESOURCE_LIST, resourceList);
			mav.addObject(SELECTED_GROUP, resource.getGroup());
			mav.addObject(FIRST_RESOURCE, resource);
			mav.addObject(BOOKING_LIST, getBookingsFromCourseSchedule(bestSolution));
			
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return mav;
		
	}
	
	private Object getBookingsFromCourseSchedule(CourseSchedule bestSolution) {
		
		return null;
		
	}

	private CourseSchedule getCourseSchedule(Long resourceId) throws Exception {
		
		CourseSchedule courseScheduleInput = new CourseSchedule();
		
		Resource resource = resourceService.getResourceById(resourceId);
		List<Group> groupList = new ArrayList<Group>();
		groupList.add(resource.getGroup());
		
		//CurriculumList
		courseScheduleInput.setCurriculumList(getCurriculumList(groupList));
		
		//RoomList
		courseScheduleInput.setRoomList(getRoomList(resourceService.getResourcesByIdGroup(resource.getGroup().getId())));
		
		//TeacherList
		courseScheduleInput.setTeacherList(getTeacherList(bookingService.getDifferentTeacherIdByIdResource(resourceId)));
		
		//TimesLotList
		courseScheduleInput.setTimeslotList(getTimesList(13));

		//DayList
		List<Day> dayList = getDayList(5);
		courseScheduleInput.setDayList(dayList);

		//PeriodList
		courseScheduleInput.setPeriodList(getPeriodList(courseScheduleInput.getDayList(), courseScheduleInput.getTimeslotList()));

		List<Booking> bookingList = bookingService.getAllBookingsByIdResource(resourceId);

		//CourseList
		courseScheduleInput.setCourseList(getCourseList(bookingList, courseScheduleInput.getTeacherList()));
		
		//LectureList
		courseScheduleInput.setLectureList(getLectureList(courseScheduleInput.getCourseList()));
		
		courseScheduleInput.setUnavailablePeriodPenaltyList(new ArrayList<UnavailablePeriodPenalty>());

		return courseScheduleInput;
		
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
	
	private List<Teacher> getTeacherList(List<String> teacherListString) {
		
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
	
	private List<Curriculum> getCurriculumList(List<Group> groupList) {
		
		List<Curriculum> curriculumList = new ArrayList<Curriculum>();
		if(groupList != null && !groupList.isEmpty()) {
			for (Group group : groupList) {
				Curriculum curriculum = new Curriculum();
				curriculum.setId(group.getId());
				curriculum.setCode(group.getName());
				curriculumList.add(curriculum);
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
	
	private List<Course> getCourseList(List<Booking> bookingList, List<Teacher> teacherList) {
		
		List<Course> courseList = new ArrayList<Course>();
		if(bookingList != null && !bookingList.isEmpty()) {
			int i = 0;
			for (Booking bookingTemp : bookingList) {
				if(bookingTemp.getRepeatList() != null && !bookingTemp.getRepeatList().isEmpty()) {
					Course courseTemp = new Course();
					
					courseTemp.setId(new Long(i++));
					
					Curriculum curriculumTemp = getCurriculumFromGroup(bookingTemp.getResource().getGroup());
					List<Curriculum> curriculumListTemp = new ArrayList<Curriculum>();
					curriculumListTemp.add(curriculumTemp);
					courseTemp.setCurriculumList(curriculumListTemp);
					
					courseTemp.setMinWorkingDaySize(1);
					
					courseTemp.setStudentSize(bookingTemp.getNumStudents());
					
					courseTemp.setTeacher(getTeacherFromId(bookingTemp.getTeacherID(), teacherList));
					
					courseTemp.setCode(bookingTemp.getSubjectID());
					
					courseTemp.setLectureSize(1);
					
					courseList.add(courseTemp);
				}
			}
		}
		return courseList;
		
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

	private Curriculum getCurriculumFromGroup(Group group) {
		
		Curriculum curriculum = new Curriculum();
		curriculum.setId(group.getId());
		curriculum.setCode(group.getName());
		return curriculum;
		
	}
	
	private List<Lecture> getLectureList(List<Course> courseList) {
		
		List<Lecture> lectureList = new ArrayList<>();
		if(courseList != null && !courseList.isEmpty()) {
			int i = 0;
			for (Course course : courseList) {
				Lecture lecture = new Lecture();
				lecture.setId(new Long(i++));
				lecture.setCourse(course);
				lecture.setLectureIndexInCourse(course.getLectureSize());
				course.setLectureSize(course.getLectureSize() + 1);
				lecture.setLocked(false);
				lectureList.add(lecture);
			}
		}
		return lectureList;
		
	}
	
}
