package it.univaq.planner.business.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.univaq.planner.business.GroupService;
import it.univaq.planner.business.model.Group;

@Service
public class GroupServiceImpl extends AServiceImpl implements GroupService {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<Group> getAllGroups() throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("GroupServiceImpl - getAllGroups()");
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Group> groupList = new ArrayList<Group>();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from groups");
			rs = st.executeQuery();
			while (rs.next()) {
				Group group = new Group();
				group.setId((Long) rs.getLong("id"));
				group.setName((String) rs.getString("name"));
				group.setDescription((String) rs.getString("description"));
				group.setIdTipGroup((Short) rs.getShort("tip_group_id"));
				groupList.add(group);
			}
		} catch (SQLException e) {
			logger.error("GroupServiceImpl - getAllGroups() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("GroupServiceImpl - getAllGroups() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("GroupServiceImpl - getAllGroups() - " + e.getMessage());
				}
			}

		}
		
		return groupList;
		
	}

	@Override
	public Group getGroupById(Long id) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("GroupServiceImpl - getAllGroups(id: " + id + ")");
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		Group group = new Group();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from groups where id = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			while (rs.next()) {

				group.setId((Long) rs.getLong("id"));
				group.setName((String) rs.getString("name"));
				group.setDescription((String) rs.getString("description"));
				group.setIdTipGroup((Short) rs.getShort("tip_group_id"));
				
			}
		} catch (SQLException e) {
			logger.error("GroupServiceImpl - getGroupById() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("GroupServiceImpl - getGroupById() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("GroupServiceImpl - getGroupById() - " + e.getMessage());
				}
			}

		}
		
		return group;
		
	}

}
