package it.univaq.planner.presentation;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.univaq.planner.business.BookingService;
import it.univaq.planner.business.GroupService;
import it.univaq.planner.business.ResourceService;
import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.Group;
import it.univaq.planner.business.model.Resource;
import it.univaq.planner.business.model.User;
import it.univaq.planner.common.spring.Utility;

@Controller
public class TestController extends ABaseController {

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private GroupService groupService;
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		
		User user = Utility.getUtente();
		System.out.println(user);
		return "admin.index";
		
	}
	
	@RequestMapping(value="/sicurezza", method=RequestMethod.GET)
	public String securityCheck() {
	
		return "admin.index";
		
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
	
	private List<org.optaplanner.examples.curriculumcourse.domain.Period> getPeriodList(List<Day> dayList,
			List<Timeslot> timeslotList) {
		
		List<org.optaplanner.examples.curriculumcourse.domain.Period> periodList = new ArrayList<org.optaplanner.examples.curriculumcourse.domain.Period>();
		if(dayList != null && !dayList.isEmpty() & timeslotList != null && !timeslotList.isEmpty()) {
			int i = 0;
			for (Day day : dayList) {
				for (Timeslot timeslot : timeslotList) {
					org.optaplanner.examples.curriculumcourse.domain.Period periodTemp = new org.optaplanner.examples.curriculumcourse.domain.Period();
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
	
	@RequestMapping(value="/test-xml", method=RequestMethod.GET)
	public String testXml() {
		
		try {
			
			/*

CourseSchedule [
name=null, 
curriculumList=[Group A, Group B, Group C, Group D, Group E], 
teacherList=[Amy, Beth, Chad, Dan, Elsa], 
courseList=[Math, Chemistry, Physics], 
dayList=[0, 1, 2, 3, 4], ->OK
timeslotList=[0, 1, 2, 3, 4, 5, 6], 
periodList=[
0-0, 0-1, 0-2, 0-3, 0-4, 0-5, 0-6, 
1-0, 1-1, 1-2, 1-3, 1-4, 1-5, 1-6, 
2-0, 2-1, 2-2, 2-3, 
3-0, 3-1, 3-2, 3-3, 3-4, 3-5, 3-6, 
4-0, 4-1, 4-2, 4-3, 4-4, 4-5, 4-6], 
roomList=[R1, R2, R3], 
unavailablePeriodPenaltyList=[Math@4-2, Chemistry@4-0, Physics@3-3], 
lectureList=[Math-0, Chemistry-0, Physics-0, Math-1, Chemistry-1, Physics-1], 

periodList=[
0-0, 0-1, 0-2, 0-3, 0-4, 0-5, 0-6, 
2-0, 2-1, 2-2, 2-3, 2-4, 2-5, 2-6,
1-0, 1-1, 1-2, 1-3, 1-4, 1-5, 1-6, 
3-0, 3-1, 3-2, 3-3, 3-4, 3-5, 3-6, 
4-0, 4-1, 4-2, 4-3, 4-4, 4-5, 4-6],

[Course [code=Math, teacher=Amy, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30], 
Course [code=Chemistry, teacher=Beth, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30], 
Course [code=Physics, teacher=Chad, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]]
Course [code=Chemistry, teacher=Beth, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]
Course [code=Physics, teacher=Chad, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]

Lecture [course=Course [code=Math, teacher=Amy, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30], lectureIndexInCourse=0, locked=false, period=null, room=null]
Lecture [course=Course [code=Chemistry, teacher=Beth, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30], lectureIndexInCourse=0, locked=false, period=null, room=null]
Lecture [course=Course [code=Physics, teacher=Chad, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30], lectureIndexInCourse=0, locked=false, period=null, room=null]

unavabile course period
Course [code=Math, teacher=Amy, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]@4-2
Course [code=Chemistry, teacher=Beth, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]@4-0
Course [code=Physics, teacher=Chad, lectureSize=2, minWorkingDaySize=1, curriculumList=[Group A, Group B], studentSize=30]@3-3
			 */
			CourseSchedule courseScheduleInput = new CourseSchedule();
			
			//RoomList
			courseScheduleInput.setRoomList(getRoomList(resourceService.getAllResources()));
			
			//TeacherList
			courseScheduleInput.setTeacherList(getTeacherList(bookingService.getDifferentTeacherId()));
			
			//CurriculumList
			courseScheduleInput.setCurriculumList(getCurriculumList(groupService.getAllGroups()));

			//TimesLotList
			courseScheduleInput.setTimeslotList(getTimesList(13));

			//DayList
			List<Day> dayList = getDayList(5);
			courseScheduleInput.setDayList(dayList);

			//PeriodList
			courseScheduleInput.setPeriodList(getPeriodList(courseScheduleInput.getDayList(), courseScheduleInput.getTimeslotList()));

			List<Booking> bookingList = bookingService.getAllBookings();

			//CourseList
			courseScheduleInput.setCourseList(getCourseList(bookingList, courseScheduleInput.getTeacherList()));
			
			//LectureList
			courseScheduleInput.setLectureList(getLectureList(courseScheduleInput.getCourseList()));
			
			courseScheduleInput.setUnavailablePeriodPenaltyList(new ArrayList<UnavailablePeriodPenalty>());
			
			System.out.println("***********");
	        System.out.println("courseScheduleInput: " + courseScheduleInput.toString());
	        
	      //Factory problem
			SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
	        Solver solver = solverFactory.buildSolver();
	        solver.solve(courseScheduleInput);
	        CourseSchedule bestSolution = (CourseSchedule) solver.getBestSolution();
	        System.out.println("");
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "admin.index";
		
	}

}
