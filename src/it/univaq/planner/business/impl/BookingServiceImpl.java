package it.univaq.planner.business.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.univaq.planner.business.BookingService;
import it.univaq.planner.business.RepeatService;
import it.univaq.planner.business.ResourceService;
import it.univaq.planner.business.model.Booking;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private RepeatService repeatService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Override
	public List<Booking> getAllBookings() throws Exception {
		
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
				bookingTemp.setBookingDate((Date) rs.getDate("booking_date"));
				bookingTemp.setResource(resourceService.getResourceById((Long) rs.getLong("resource_id")));
				bookingTemp.setIdTipEvent((Short) rs.getShort("tip_event_id"));
				bookingTemp.setIdUser((Long) rs.getLong("user_id"));
				bookingTemp.setRepeatList(repeatService.getRepeatsByIdBooking(bookingTemp.getId()));
				
				bookingList.add(bookingTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}

		}
		
		return bookingList;
		
	}

	@Override
	public List<String> getDifferentTeacherId() throws Exception {
		
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
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}

		}
		
		return teacherList;
		
	}

	@Override
	public List<Booking> getAllBookingsByIdResource(Long idResource) throws Exception {
		
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
				bookingTemp.setBookingDate((Date) rs.getDate("booking_date"));
				bookingTemp.setResource(resourceService.getResourceById((Long) rs.getLong("resource_id")));
				bookingTemp.setIdTipEvent((Short) rs.getShort("tip_event_id"));
				bookingTemp.setIdUser((Long) rs.getLong("user_id"));
				bookingTemp.setRepeatList(repeatService.getRepeatsByIdBooking(bookingTemp.getId()));
				
				bookingList.add(bookingTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}

		}
		
		return bookingList;
		
	}

	@Override
	public List<String> getDifferentTeacherIdByIdResource(Long idResource) throws Exception {
		
		List<String> teacherList = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select distinct(teacher_id) from bookings where resource_id = ?");
			st.setLong(1, idResource);
			rs = st.executeQuery();
			while (rs.next()) {
				
				teacherList.add((String) rs.getString("teacher_id"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}

		}
		
		return teacherList;
		
	}

}
