package at.tuwien.ase.dao.administration;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import at.tuwien.ase.data.GenerateCategories;
import at.tuwien.ase.data.GenerateCompanies;
import at.tuwien.ase.data.GenerateProjectTypes;
import at.tuwien.ase.data.GenerateProjects;
import at.tuwien.ase.data.GenerateResources;
import at.tuwien.ase.data.GenerateTaskReport;
import at.tuwien.ase.data.GenerateTaskTypes;
import at.tuwien.ase.data.GenerateTasks;
import at.tuwien.ase.data.GenerateUsers;
import at.tuwien.ase.data.GenerateWorkingObjects;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;


@Stateless
public class AdministrationDaoImpl implements AdministrationDaoI {

	@PersistenceContext(name = "AseDataSource")
	EntityManager em;
	
	@Override
	public boolean populateDB() {
		
		try {
			prepareDatabese();
		} catch (Exception e) { e.printStackTrace(); }
		
		addCompanies();
		listCompanies();
		addUsers();
		addCategories();
		addWorkingBojects();
		addProjectTypes();
		addResources();
		addTaskTypes();
		addProject();
		addTasks();
		addTaskReports();

		return true;
	}
	
	private void addCompanies() {
		
		try {
			
	        GenerateCompanies.createCompanies();
	        
	        for (Company c : GenerateCompanies.getCompanies()) 
	        	em.persist(c);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void listCompanies() {
		
		try {
        
	        List<Company> results = em.createQuery("from Company", Company.class ).getResultList();
	        
	        for (Company c : results) {
	              System.out.println(c.getId() + " " + c.getName());
	        }
		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addUsers() {
		
		try {
			
	        GenerateUsers.createWorkers();
	        GenerateUsers.createSupervisors();
	        GenerateUsers.createManagers();
	        
	        for (User u : GenerateUsers.getManagers()) 
	        	em.persist(u);
	        
	        for (User u : GenerateUsers.getSupervisors()) 
	        	em.persist(u);
	        
	        for (User u : GenerateUsers.getWorkers()) 
	        	em.persist(u);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addCategories() {
		
		try {
			
	        GenerateCategories.createCategories();
	        
	        for (Category c : GenerateCategories.getCategories()) 
	        	em.persist(c);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addWorkingBojects() {
		
		try {
			
	        GenerateWorkingObjects.createWorkingObjects();
	        
	        for (WorkingObject wo : GenerateWorkingObjects.getWorkingObjects()) 
	        	em.persist(wo);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addProjectTypes() {
		
		try {
			
	        GenerateProjectTypes.createProjectTypes();
	        
	        for (ProjectType pt : GenerateProjectTypes.getProjectTypes()) 
	        	em.persist(pt);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addResources() {
		
		try {
			
	        GenerateResources.createResources();
	        
	        for (Resource r : GenerateResources.getResources()) 
	        	em.persist(r);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addTaskTypes() {
		
		try {
			
	        GenerateTaskTypes.createTaskTypes();
	        
	        for (TaskType tt : GenerateTaskTypes.getTaskTypes()) 
	        	em.persist(tt);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addProject() {
		
		try {
			
	        GenerateProjects.createProjects();
	        
	        for (Project p : GenerateProjects.getProjects()) 
	        	em.persist(p);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addTasks() {
		
		try {
			
	        GenerateTasks.createTasks();
	        
	        for (Task t : GenerateTasks.getTasks()) 
	        	em.persist(t);

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void addTaskReports() {
		
		try {
			
	        GenerateTaskReport.createTaskReports();
	        
	        for (TaskReport tr : GenerateTaskReport.getReports()) {
	        	em.persist(tr);
	        	
	        	if (tr.getResourceUsageList() != null)
	        		for (ResourceUsage ru : tr.getResourceUsageList())
	        			em.persist(ru);
	        }

		} catch (Exception e) { e.printStackTrace(); } 
	}
	
	private void prepareDatabese() throws Exception {

		try {

			System.out.println("Dumping old records...");
			// Disable referential integrity using SET REFERENTIAL_INTEGRITY
			// FALSE
			String s = "SET REFERENTIAL_INTEGRITY FALSE;";

			// Get the list of all tables using SHOW TABLES
			Query q = em.createNativeQuery("SELECT    TABLE_NAME   FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA  ='PUBLIC'");

			// Delete the data from each table using TRUNCATE TABLE tableName
			@SuppressWarnings("unchecked")
			List<String> res = q.getResultList();
			for (String t : res) {
				s += "TRUNCATE TABLE " + t + ";";

			}
			em.createNativeQuery(s).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			// Enable referential integrity using
			em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE"); // lets do
																	// this
																	// allways
		}
	}

}
