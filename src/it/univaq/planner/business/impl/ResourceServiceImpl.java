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
import it.univaq.planner.business.ResourceService;
import it.univaq.planner.business.model.Resource;

@Service
public class ResourceServiceImpl extends AServiceImpl implements ResourceService {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private GroupService groupService;
	
	@Override
	public Resource getResourceById(Long id) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("ResourceServiceImpl - getResourceById(id: " + id + ")");
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		Resource resource = new Resource();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from resources where id = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			while (rs.next()) {

				resource.setId((Long) rs.getLong("id"));
				resource.setName((String) rs.getString("name"));
				resource.setDescription((String) rs.getString("description"));
				resource.setBlackboard((String) rs.getString("description"));
				resource.setNote((String) rs.getString("description"));
				
				resource.setCapacity((Integer) rs.getInt("capacity"));
				resource.setNetwork((Integer) rs.getInt("network"));
				
				resource.setProjector((Boolean) rs.getBoolean("projector"));
				resource.setScreenMotor((Boolean) rs.getBoolean("screen_motor"));
				resource.setScreenManual((Boolean) rs.getBoolean("screen_manual"));
				resource.setAudio((Boolean) rs.getBoolean("audio"));
				resource.setPc((Boolean) rs.getBoolean("pc"));
				resource.setWireMic((Boolean) rs.getBoolean("wire_mic"));
				resource.setWirelessMic((Boolean) rs.getBoolean("wireless_mic"));
				resource.setOverheadProjector((Boolean) rs.getBoolean("overhead_projector"));
				resource.setVisualPresenter((Boolean) rs.getBoolean("visual_presenter"));
				resource.setWiring((Boolean) rs.getBoolean("wiring"));
				resource.setEquipment((Boolean) rs.getBoolean("equipment"));
				
				resource.setIdTipResource((Short) rs.getShort("tip_resource_id"));
				
				resource.setGroup(groupService.getGroupById((Long) rs.getLong("group_id")));
				
			}
		} catch (SQLException e) {
			logger.error("ResourceServiceImpl - getResourceById() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getResourceById() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getResourceById() - " + e.getMessage());
				}
			}

		}
		
		return resource;
		
	}

	@Override
	public List<Resource> getAllResources() throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("ResourceServiceImpl - getAllResources()");
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Resource> resourcesList = new ArrayList<Resource>();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from resources");
			rs = st.executeQuery();
			while (rs.next()) {

				Resource resource = new Resource();
				resource.setId((Long) rs.getLong("id"));
				resource.setName((String) rs.getString("name"));
				resource.setDescription((String) rs.getString("description"));
				resource.setBlackboard((String) rs.getString("description"));
				resource.setNote((String) rs.getString("description"));
				
				resource.setCapacity((Integer) rs.getInt("capacity"));
				resource.setNetwork((Integer) rs.getInt("network"));
				
				resource.setProjector((Boolean) rs.getBoolean("projector"));
				resource.setScreenMotor((Boolean) rs.getBoolean("screen_motor"));
				resource.setScreenManual((Boolean) rs.getBoolean("screen_manual"));
				resource.setAudio((Boolean) rs.getBoolean("audio"));
				resource.setPc((Boolean) rs.getBoolean("pc"));
				resource.setWireMic((Boolean) rs.getBoolean("wire_mic"));
				resource.setWirelessMic((Boolean) rs.getBoolean("wireless_mic"));
				resource.setOverheadProjector((Boolean) rs.getBoolean("overhead_projector"));
				resource.setVisualPresenter((Boolean) rs.getBoolean("visual_presenter"));
				resource.setWiring((Boolean) rs.getBoolean("wiring"));
				resource.setEquipment((Boolean) rs.getBoolean("equipment"));
				
				resource.setIdTipResource((Short) rs.getShort("tip_resource_id"));
				
				resource.setGroup(groupService.getGroupById((Long) rs.getLong("group_id")));
				resourcesList.add(resource);
				
			}
		} catch (SQLException e) {
			logger.error("ResourceServiceImpl - getAllResources() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getAllResources() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getAllResources() - " + e.getMessage());
				}
			}

		}
		
		return resourcesList;

		
	}

	@Override
	public List<Resource> getResourcesByIdGroup(Long idGroup) throws Exception {
		
		if(logger.isDebugEnabled())
			logger.debug("ResourceServiceImpl - getResourcesByIdGroup(idGroup: " + idGroup + ")");
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Resource> resourcesList = new ArrayList<Resource>();
		
		try {
			con = dataSource.getConnection();
			st = con.prepareStatement("select * from resources where group_id = ?");
			st.setLong(1, idGroup);
			rs = st.executeQuery();
			while (rs.next()) {

				Resource resource = new Resource();
				resource.setId((Long) rs.getLong("id"));
				resource.setName((String) rs.getString("name"));
				resource.setDescription((String) rs.getString("description"));
				resource.setBlackboard((String) rs.getString("description"));
				resource.setNote((String) rs.getString("description"));
				
				resource.setCapacity((Integer) rs.getInt("capacity"));
				resource.setNetwork((Integer) rs.getInt("network"));
				
				resource.setProjector((Boolean) rs.getBoolean("projector"));
				resource.setScreenMotor((Boolean) rs.getBoolean("screen_motor"));
				resource.setScreenManual((Boolean) rs.getBoolean("screen_manual"));
				resource.setAudio((Boolean) rs.getBoolean("audio"));
				resource.setPc((Boolean) rs.getBoolean("pc"));
				resource.setWireMic((Boolean) rs.getBoolean("wire_mic"));
				resource.setWirelessMic((Boolean) rs.getBoolean("wireless_mic"));
				resource.setOverheadProjector((Boolean) rs.getBoolean("overhead_projector"));
				resource.setVisualPresenter((Boolean) rs.getBoolean("visual_presenter"));
				resource.setWiring((Boolean) rs.getBoolean("wiring"));
				resource.setEquipment((Boolean) rs.getBoolean("equipment"));
				
				resource.setIdTipResource((Short) rs.getShort("tip_resource_id"));
				
				resource.setGroup(groupService.getGroupById((Long) rs.getLong("group_id")));
				resourcesList.add(resource);
				
			}
		} catch (SQLException e) {
			logger.error("ResourceServiceImpl - getResourcesByIdGroup() - " + e.getMessage());
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getResourcesByIdGroup() - " + e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ResourceServiceImpl - getResourcesByIdGroup() - " + e.getMessage());
				}
			}

		}
		
		return resourcesList;
		
	}

}
