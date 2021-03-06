package it.univaq.planner.business.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.univaq.planner.business.BookingService;
import it.univaq.planner.business.RepeatService;
import it.univaq.planner.business.ResourceService;
import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.Repeat;
import it.univaq.planner.business.model.TipEvent;

@Service
public class BookingServiceImpl extends AServiceImpl implements BookingService {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private RepeatService repeatService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Override
	public List<Booking> getAllBookings() throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getAllBookings()");
			
		List<Booking> bookingList = new ArrayList<Booking>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from bookings");
			rs = st.executeQuery();
			while (rs.next()) {
				Booking bookingTemp = new Booking();
				
				bookingTemp.setId((Long) rs.getLong("id"));
				bookingTemp.setName((String) rs.getString("name"));
				bookingTemp.setDescription((String) rs.getString("description"));
				bookingTemp.setNumStudents((Integer) rs.getInt("num_students"));
				bookingTemp.setSubjectID((String) rs.getString("subject_id"));
				bookingTemp.setTeacherID((String) rs.getString("teacher_id"));
				bookingTemp.setCdsID((String) rs.getString("cds_id"));
				bookingTemp.setBookingDate((Date) rs.getDate("booking_date"));
				bookingTemp.setResource(resourceService.getResourceById((Long) rs.getLong("resource_id")));
				bookingTemp.setIdTipEvent((Short) rs.getShort("tip_event_id"));
				bookingTemp.setIdUser((Long) rs.getLong("user_id"));
				bookingTemp.setRepeatList(repeatService.getRepeatsByIdBooking(bookingTemp.getId()));
				
				bookingList.add(bookingTemp);
			}
		} catch (SQLException e) {
			logger.error("BookingServiceImpl - getAllBookings() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookings() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookings() - " + e.getMessage());
				}
			}

		}
		
		return bookingList;
		
	}

	@Override
	public List<String> getDifferentTeacherId() throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getDifferentTeacherId()");
		
		List<String> teacherList = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select distinct(teacher_id) from bookings");
			rs = st.executeQuery();
			while (rs.next()) {
				
				teacherList.add((String) rs.getString("teacher_id"));
				
			}
		} catch (SQLException e) {
			logger.error("BookingServiceImpl - getDifferentTeacherId() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getDifferentTeacherId() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getDifferentTeacherId() - " + e.getMessage());
				}
			}

		}
		
		return teacherList;
		
	}

	@Override
	public List<Booking> getAllBookingsByIdResource(Long idResource) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getAllBookingsByIdResource(idResource: " + idResource + ")");
		
		List<Booking> bookingList = new ArrayList<Booking>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from bookings where resource_id = ?");
			st.setLong(1, idResource);
			rs = st.executeQuery();
			while (rs.next()) {
				Booking bookingTemp = new Booking();
				
				bookingTemp.setId((Long) rs.getLong("id"));
				bookingTemp.setName((String) rs.getString("name"));
				bookingTemp.setDescription((String) rs.getString("description"));
				bookingTemp.setNumStudents((Integer) rs.getInt("num_students"));
				bookingTemp.setSubjectID((String) rs.getString("subject_id"));
				bookingTemp.setTeacherID((String) rs.getString("teacher_id"));
				bookingTemp.setCdsID((String) rs.getString("cds_id"));
				bookingTemp.setBookingDate((Date) rs.getDate("booking_date"));
				bookingTemp.setResource(resourceService.getResourceById((Long) rs.getLong("resource_id")));
				bookingTemp.setIdTipEvent((Short) rs.getShort("tip_event_id"));
				bookingTemp.setIdUser((Long) rs.getLong("user_id"));
				bookingTemp.setRepeatList(repeatService.getRepeatsByIdBooking(bookingTemp.getId()));
				
				bookingList.add(bookingTemp);
			}
		} catch (SQLException e) {
			logger.error("BookingServiceImpl - getAllBookingsByIdResource() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookingsByIdResource() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookingsByIdResource() - " + e.getMessage());
				}
			}

		}
		
		return bookingList;
		
	}

	/**
	 * Return the list of the current week
	 */
	@Override
	public List<Booking> getAllBookingsByIdGroup(Long idGroup, List<TipEvent> tipEventList) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getAllBookingsByIdGroup(idGroup: " + idGroup + ", tipEventList: " + tipEventList + ")");
		
		List<Booking> bookingList = new ArrayList<Booking>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DATE, -6);
		}
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		Date firstDayOfWeek = cal.getTime();
		cal.add(Calendar.DATE, 7);
		Date lastDayOfWeek = cal.getTime();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			StringBuilder query = new StringBuilder();
			query.append("select b.*, rep.* from bookings b, groups g, resources r, repeats rep where b.resource_id = r.id and r.group_id = g.id and g.id = ? and b.id = rep.booking_id and rep.event_date_start >= ? and rep.event_date_end < ? and b.tip_event_id in (");
			for (int i = 0; i < tipEventList.size(); i++) {
				query.append("?");
				if(i != tipEventList.size() - 1)
					query.append(",");
			}
			query.append(")");
			st = con.prepareStatement(query.toString());
			st.setLong(1, idGroup);
			st.setDate(2, new java.sql.Date(firstDayOfWeek.getTime()));
			st.setDate(3, new java.sql.Date(lastDayOfWeek.getTime()));
			int index = 4;
			for (int j = 0; j < tipEventList.size(); j++) {
				st.setShort(index++, tipEventList.get(j).getId());
			}
			
			rs = st.executeQuery();
			while (rs.next()) {
				Booking bookingTemp = new Booking();
				
				bookingTemp.setId((Long) rs.getLong("id"));
				bookingTemp.setName((String) rs.getString("name"));
				bookingTemp.setDescription((String) rs.getString("description"));
				bookingTemp.setNumStudents((Integer) rs.getInt("num_students"));
				bookingTemp.setSubjectID((String) rs.getString("subject_id"));
				bookingTemp.setTeacherID((String) rs.getString("teacher_id"));
				bookingTemp.setCdsID((String) rs.getString("cds_id"));
				bookingTemp.setBookingDate((Date) rs.getDate("booking_date"));
				bookingTemp.setResource(resourceService.getResourceById((Long) rs.getLong("resource_id")));
				bookingTemp.setIdTipEvent((Short) rs.getShort("tip_event_id"));
				bookingTemp.setIdUser((Long) rs.getLong("user_id"));
				bookingTemp.setRepeatList(getRepeat(rs));
				
				bookingList.add(bookingTemp);
			}
		} catch (SQLException e) {
			logger.error("BookingServiceImpl - getAllBookingsByIdGroup() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookingsByIdGroup() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getAllBookingsByIdGroup() - " + e.getMessage());
				}
			}

		}
		
		return bookingList;
		
	}

	private List<Repeat> getRepeat(ResultSet rs) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getRepeat(rs: " + rs + ")");
		
		List<Repeat> repeatList = new ArrayList<Repeat>();
		Repeat repeat = new Repeat();
		repeat.setId((Long) rs.getLong("rep.id"));
		repeat.setIdBooking((Long) rs.getLong("rep.booking_id"));
		repeat.setIdTipBookingStatus((Short) rs.getShort("rep.tip_booking_status_id"));
		repeat.setEventDateEnd((Date)(rs.getTimestamp("rep.event_date_end")));
		repeat.setEventDateStart((Date)(rs.getTimestamp("rep.event_date_start")));
		repeatList.add(repeat);
		return repeatList;
		
	}

	@Override
	public List<String> getDifferentTeacherIdByIdGroupAndTipEvent(Long idGroup, List<TipEvent> tipEventList)
			throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("BookingServiceImpl - getDifferentTeacherIdByIdGroup(idGroup: " + idGroup + ", tipEventList: " + tipEventList + ")");
		
		List<String> teacherList = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			StringBuilder query = new StringBuilder();
			query.append("select distinct(teacher_id) from bookings b, resources res where b.resource_id = res.id and res.group_id = ? and b.tip_event_id in (");
			for (int i = 0; i < tipEventList.size(); i++) {
				query.append("?");
				if(i != tipEventList.size() - 1)
					query.append(",");
			}
			query.append(")");
			st = con.prepareStatement(query.toString());
			
			st.setLong(1, idGroup);
			int index = 2;
			for (int j = 0; j < tipEventList.size(); j++) {
				st.setShort(index++, tipEventList.get(j).getId());
			}
			rs = st.executeQuery();
			while (rs.next()) {
				
				teacherList.add((String) rs.getString("teacher_id"));
				
			}
		} catch (SQLException e) {
			logger.error("BookingServiceImpl - getDifferentTeacherIdByIdGroup() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getDifferentTeacherIdByIdGroup() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("BookingServiceImpl - getDifferentTeacherIdByIdGroup() - " + e.getMessage());
				}
			}

		}
		
		return teacherList;
		
	}

}
