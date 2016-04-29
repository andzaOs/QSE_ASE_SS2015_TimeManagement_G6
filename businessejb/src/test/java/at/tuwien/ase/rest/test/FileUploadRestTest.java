package at.tuwien.ase.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;

import javax.ejb.EJB;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.dao.TaskTypeDaoInterface;
import at.tuwien.ase.dao.UserDaoInterface;
import at.tuwien.ase.dao.WorkingObjectDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;

import com.jayway.restassured.response.Cookie;

@SuppressWarnings("deprecation")
@RunWith(Arquillian.class)
public class FileUploadRestTest extends TestSuiteRest {

	@EJB
	TaskTypeDaoInterface taskTypeDao;
	@Test
	public void getItemAccessTest() throws Exception{
		String fileName = "test_case_wrong_format.jpg";

		File file = new File(fileName);
		String urlString = "http://localhost:8080/web/rest/files/uploadTaskTypes";
		String fileDescription= "File Uploaded :: test.xlsx";
		
    	DefaultHttpClient client = new DefaultHttpClient() ;
        HttpPost postRequest = new HttpPost (urlString) ;
        HttpResponse response = null;
        
        	//Set various attributes 
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            
            multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : "")) ;
 
            //FileBody fileBody = new FileBody(file, "application/octect-stream") ;
            FileBody fileBody = new FileBody(file, fileName, "application/octect-stream", null);
            
            //Prepare payload
            multiPartEntity.addPart("file", fileBody) ;
 
            //Set to request body
            postRequest.setEntity(multiPartEntity) ;

			try {
				response = client.execute(postRequest);
			} catch (Exception e) {
				System.out.println("File not found!");
			}
            
            //Verify response if any
            if (response != null) {
                
            }
            assertEquals(403, response.getStatusLine().getStatusCode());
        }
       

		
	@EJB
	WorkingObjectDaoInterface woDao;
	
	@EJB
	UserDaoInterface userDao;
	
	@EJB
	CompanyDaoInterface companyDao;
		
	@Test
	public void testFileTaskTypeFormatNotOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());

		String fname = "test_case_wrong_format.jpg";

		File file = new File(fname);
		//System.out.println(file.getAbsolutePath()+"@@@@@@@@@@@@@@@@@@@@@@@@@"+file.exists());

		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadTaskTypes", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("Wrong file format.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileTaskTypeNoContent() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());
		
		String fname = "test_case_no_data.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadTaskTypes", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("No TaskTypes imported - check file content.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileTaskTypeOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, taskTypeDao.listAll().size());
		
		String fname = "task_types_extracted.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadTaskTypes", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(200, res.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testFileWorkingObjectFormatNotOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, woDao.listAll().size());

		String fname = "test_case_wrong_format.jpg";

		File file = new File(fname);
		//System.out.println(file.getAbsolutePath()+"@@@@@@@@@@@@@@@@@@@@@@@@@"+file.exists());

		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadWorkingObjects", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("Wrong file format.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileWorkingObjectNoContent() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, woDao.listAll().size());
		
		String fname = "test_case_no_data.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadWorkingObjects", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("No WorkingObjects imported - check file content.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileWorkingObjectOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, woDao.listAll().size());
		
		String fname = "task_types_extracted.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadWorkingObjects", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(200, res.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testFileUsersFormatNotOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());
		
		String fname = "test_case_wrong_format.jpg";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadUsers", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("Wrong file format.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileUserNoContent() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());
		
		String fname = "test_case_no_data.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadUsers", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(500, res.getStatusLine().getStatusCode());
		assertEquals("No Users imported - check file content.", convertStreamToString(res.getEntity().getContent()));
	}
	
	@Test
	public void testFileUsersOK() throws Exception {
		
		assertEquals("database setup error-> database not empty", 0, userDao.listAll().size());
		
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, companyDao.listAll().size());

		// create company
		Company c = new Company();
		c.setName("PORR");
		c.setUserList(new ArrayList<User>());
		
		em.persist(c);
		
		// create company
		Company c1 = new Company();
		c1.setName("Nivelatherm");
		c1.setUserList(new ArrayList<User>());
		
		em.persist(c1);
		
		utx.commit();
		
		String fname = "users.xlsx";
		
		File file = new File(fname);
		
		HttpResponse res = executeMultiPartRequest("http://localhost:8080/web/rest/files/uploadUsers", 
        		file, file.getName(), "File Uploaded :: test.xlsx") ;
		
		assertNotNull(res);
		assertEquals(200, res.getStatusLine().getStatusCode());
	}
	

    public HttpResponse executeMultiPartRequest(String urlString, File file, String fileName, String fileDescription) throws Exception {
    	
    	DefaultHttpClient client = new DefaultHttpClient() ;
        HttpPost postRequest = new HttpPost (urlString) ;
        Cookie c = getManagerCookie();

        CookieStore cookieStore = new BasicCookieStore();
        client.setCookieStore(cookieStore);

        BasicClientCookie cookie = new BasicClientCookie(c.getName(), c.getValue());
        cookie.setDomain("localhost");
        cookie.setPath("/");
        client.getCookieStore().addCookie(cookie);
        HttpResponse response = null;
        
        try {
        	
        	//Set various attributes 
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            
            multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : "")) ;
 
            //FileBody fileBody = new FileBody(file, "application/octect-stream") ;
            FileBody fileBody = new FileBody(file, fileName, "application/octect-stream", null);
            
            //Prepare payload
            multiPartEntity.addPart("file", fileBody) ;
 
            //Set to request body
            postRequest.setEntity(multiPartEntity) ;

			try {
				response = client.execute(postRequest);
			} catch (Exception e) {
				System.out.println("File not found!");
			}
            
            //Verify response if any
            if (response != null) {
                
                return response;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace() ;
            
            return response;
        }
        
        return response;
    }
    
    @SuppressWarnings("resource")
	private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}