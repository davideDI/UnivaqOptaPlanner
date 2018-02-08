package it.univaq.planner.business;

import java.util.List;

import it.univaq.planner.business.model.Group;

public interface GroupService {

	List<Group> getAllGroups() throws Exception; 
	Group getGroupById(Long id) throws Exception;
	
}
