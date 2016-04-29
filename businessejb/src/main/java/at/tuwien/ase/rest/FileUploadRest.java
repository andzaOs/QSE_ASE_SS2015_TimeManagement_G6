package at.tuwien.ase.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.parsing.ParseTaskType;
import at.tuwien.ase.parsing.ParseUsers;
import at.tuwien.ase.rest.security.SecurityInterceptor;


@Stateless
@Path("/files")
@Interceptors ({SecurityInterceptor.class})
public class FileUploadRest implements FileUploadRestI {

	@PersistenceContext 
	Session session;
	
	@EJB
	CompanyDaoInterface companyDao;
	
	private ParseTaskType ptt = new ParseTaskType();
	private ParseUsers pu = new ParseUsers();
	
	@POST
	@Path("/uploadTaskTypes")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Response uploadTaskTypes(MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> formParts = input.getFormDataMap();

		List<InputPart> inPart = formParts.get("file");

		for (InputPart inputPart : inPart) {

			 try {

				// Retrieve headers, read the Content-Disposition header to obtain the original name of the file
				MultivaluedMap<String, String> headers = inputPart.getHeaders();
				
				fileName = parseFileName(headers);

				// Handle the body of that part with an InputStream
				InputStream istream = inputPart.getBody(InputStream.class,null);

				System.out.println("Uploaded file: " + fileName);

				if (ptt.checkFileType(fileName)) {
					
					ptt.setTtTypes(true);
					
					if (!ptt.processFile(istream))
						return Response.status(500).entity("Exception while processing a file.").build();
				
					else {
						
						ptt.setTtTypes(false);
						
						if (ptt.getTaskTypes().size() > 0) {
							
							persistTaskType(ptt.getTaskTypes());
							
							return Response.status(200).entity("{\"uploaded\" : \"OK\"}").build();
							
						} else {
							
							return Response.status(500).entity("No TaskTypes imported - check file content.").build();
						}
					}
				} else 
					return Response.status(500).entity("Wrong file format.").build();

			  } catch (IOException e) {
				  e.printStackTrace();
				
				  return Response.status(500).entity("Error while processing a file.").build();
			  }
		}
		
		return Response.status(500).entity("Error while processing a file.").build();
	}
	
	@POST
	@Path("/uploadWorkingObjects")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Response uploadWorkingObjects(MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> formParts = input.getFormDataMap();

		List<InputPart> inPart = formParts.get("file");

		for (InputPart inputPart : inPart) {

			 try {

				// Retrieve headers, read the Content-Disposition header to obtain the original name of the file
				MultivaluedMap<String, String> headers = inputPart.getHeaders();
				fileName = parseFileName(headers);

				// Handle the body of that part with an InputStream
				InputStream istream = inputPart.getBody(InputStream.class,null);

				System.out.println("Uploaded file: " + fileName);

				if (ptt.checkFileType(fileName)) {
					
					ptt.setWoObject(true);
					
					if (!ptt.processFile(istream))
						return Response.status(500).entity("Exception while processing a file.").build();
				
					else {
						
						ptt.setWoObject(false);
						
						if (ptt.getWoS().size() > 0) {
							
							persistWorkingObject(ptt.getWoS());
							
							return Response.status(200).entity("{\"uploaded\" : \"OK\"}").build();
							
						} else {
							
							return Response.status(500).entity("No WorkingObjects imported - check file content.").build();
						}
					}
				} else 
					return Response.status(500).entity("Wrong file format.").build();

			  } catch (IOException e) {
				  e.printStackTrace();
				
				  return Response.status(500).entity("Error while processing a file.").build();
			  }
		}
		
		return Response.status(500).entity("Error while processing a file.").build();
	}
	
	@POST
	@Path("/uploadUsers")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	@RolesAllowed({"SUPERVISOR", "MANAGER"})
	public Response uploadUsers(MultipartFormDataInput input) {
		
		String fileName = "";

		Map<String, List<InputPart>> formParts = input.getFormDataMap();

		List<InputPart> inPart = formParts.get("file");

		for (InputPart inputPart : inPart) {

			 try {

				// Retrieve headers, read the Content-Disposition header to obtain the original name of the file
				MultivaluedMap<String, String> headers = inputPart.getHeaders();
				fileName = parseFileName(headers);

				// Handle the body of that part with an InputStream
				InputStream istream = inputPart.getBody(InputStream.class,null);

				System.out.println("Uploaded file: " + fileName);
				
				pu.setCompanyDao(this.companyDao);

				if (pu.checkFileType(fileName)) {
					
					if (!pu.processFile(istream))
						return Response.status(500).entity("Exception while processing a file.").build();
				
					else {
						
						if (pu.getUsers().size() > 0) {
							
							persistUser(pu.getUsers());
							
							return Response.status(200).entity("{\"uploaded\" : \"OK\"}").build();
							
						} else {
							
							return Response.status(500).entity("No Users imported - check file content.").build();
						}
					}
				} else 
					return Response.status(500).entity("Wrong file format.").build();

			  } catch (IOException e) {
				  e.printStackTrace();
				
				  return Response.status(500).entity("Error while processing a file.").build();
			  }
		}
		
		return Response.status(500).entity("Error while processing a file.").build();
	}

	// Parse Content-Disposition header to get the original file name
	private String parseFileName(MultivaluedMap<String, String> headers) {

		String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

		for (String name : contentDispositionHeader) {

			if ((name.trim().startsWith("filename"))) {

				String[] tmp = name.split("=");

				String fileName = tmp[1].trim().replaceAll("\"","");

				return fileName;
			}
		}
		return "randomName";
	}

	private void persistTaskType(HashMap<String, TaskType> map) {

		TaskType tt = null;
		TaskType storedTT;
		
		for(String key : map.keySet()) {
			
			tt = map.get(key);
			
			storedTT = getByTaskNumber(tt.getTaskNumber());
			
			if (storedTT != null) {
				
				tt.setId(storedTT.getId());
				session.saveOrUpdate(session.merge(tt));
				
			} else
				session.saveOrUpdate(tt);
		}
	}
	
	private void persistWorkingObject(HashMap<String, WorkingObject> map) {

		WorkingObject wo = null;
		WorkingObject storedWO;
		
		for(String key : map.keySet()) {
			
			wo = map.get(key);
				
			storedWO = getByWoNumber(wo.getWoNumber());
		
			if (storedWO != null) {
				
				wo.setId(storedWO.getId());
				session.saveOrUpdate(session.merge(wo));
				
			} else
				session.saveOrUpdate(wo);
		}
	}





	private void persistUser(List<User> users) {
		
		User storedU;
		
		for(User u : users) {
			
			storedU = getByName(u.getForname(), u.getLastname());
			
			if (storedU != null) {
				
				u.setId(storedU.getId());
				session.saveOrUpdate(session.merge(u));
				
			} else {
				session.saveOrUpdate(u);
			}
		}
	}
	
	public User getByName(String forname, String lastname) {
		
		User u = null;
		
		Query q = session.createQuery("from User where forname = ? and lastname = ?");
		q.setParameter(0, forname);
		q.setParameter(1, lastname);
		
		u = (User)q.uniqueResult();
		
		return u;
	}
	
	public TaskType getByTaskNumber(String taskNumber) {
		
		TaskType t = null;
		
		Query q = session.createQuery("from TaskType where taskNumber = ?");
		q.setParameter(0, taskNumber);
		
		t = (TaskType)q.uniqueResult();
		
		return t;
	}
	
	public WorkingObject getByWoNumber(String woNumber) {
		
		WorkingObject wo = null;
		
		Query q = session.createQuery("from WorkingObject where woNumber = ?");
		q.setParameter(0, woNumber);
		
		wo = (WorkingObject)q.uniqueResult();
		
		return wo;
	}
}