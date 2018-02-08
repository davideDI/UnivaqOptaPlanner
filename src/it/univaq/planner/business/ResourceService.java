package it.univaq.planner.business;

import java.util.List;

import it.univaq.planner.business.model.Resource;

public interface ResourceService {

	Resource getResourceById(Long id) throws Exception;
	List<Resource> getAllResources() throws Exception;
	List<Resource> getResourcesByIdGroup(Long idGroup) throws Exception;
	
}
