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

import it.univaq.planner.business.RepeatService;
import it.univaq.planner.business.model.Repeat;

@Service
public class RepeatServiceImpl extends AServiceImpl implements RepeatService {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<Repeat> getAllRepeats() throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("RepeatServiceImpl - getAllRepeats()");
		
		List<Repeat> repeatList = new ArrayList<Repeat>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from repeats");
			rs = st.executeQuery();
			while (rs.next()) {
				Repeat repeatTemp = new Repeat();
				
				repeatTemp.setId((Long) rs.getLong("id"));
				repeatTemp.setEventDateEnd((Date)(rs.getTimestamp("event_date_end")));
				repeatTemp.setEventDateStart((Date)(rs.getTimestamp("event_date_start")));
				repeatTemp.setIdBooking((Long) rs.getLong("booking_id"));
				repeatTemp.setIdTipBookingStatus((Short) rs.getShort("tip_booking_status_id"));
				
				repeatList.add(repeatTemp);
			}
		} catch (SQLException e) {
			logger.error("RepeatServiceImpl - getAllRepeats() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("RepeatServiceImpl - getAllRepeats() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("RepeatServiceImpl - getAllRepeats() - " + e.getMessage());
				}
			}

		}
		
		return repeatList;
		
	}

	@Override
	public List<Repeat> getRepeatsByIdBooking(Long idBooking) throws Exception {

		if(logger.isDebugEnabled())
			logger.debug("RepeatServiceImpl - getRepeatsByIdBooking(idBooking: " + idBooking + ")");
		
		List<Repeat> repeatList = new ArrayList<Repeat>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from repeats where booking_id = ?");
			st.setLong(1, idBooking);
			rs = st.executeQuery();
			while (rs.next()) {
				Repeat repeatTemp = new Repeat();
				
				repeatTemp.setId((Long) rs.getLong("id"));
				repeatTemp.setEventDateEnd((Date)(rs.getTimestamp("event_date_end")));
				repeatTemp.setEventDateStart((Date)(rs.getTimestamp("event_date_start")));
				repeatTemp.setIdBooking((Long) rs.getLong("booking_id"));
				repeatTemp.setIdTipBookingStatus((Short) rs.getShort("tip_booking_status_id"));
				repeatList.add(repeatTemp);
			}
		} catch (SQLException e) {
			logger.error("RepeatServiceImpl - getRepeatsByIdBooking() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("RepeatServiceImpl - getRepeatsByIdBooking() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("RepeatServiceImpl - getRepeatsByIdBooking() - " + e.getMessage());
				}
			}

		}
		
		return repeatList;
		
	}

}
