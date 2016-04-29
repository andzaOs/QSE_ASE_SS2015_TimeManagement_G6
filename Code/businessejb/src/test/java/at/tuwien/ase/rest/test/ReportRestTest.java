package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ReportDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class ReportRestTest extends TestSuiteRest {
	@EJB
	private ReportDaoInterface reportDao;
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/ReportRest/reportTotalTasksPerProjectId/1");
	}

	@Test
	public void testGetTotalTaskCountForProjectIdEmpty() throws Exception {
		utx.begin();
		em.joinTransaction();

		Project p = new Project();

		User worker = new User();
		User supervisor = new User();
		em.persist(worker);
		em.persist(supervisor);
		
		em.persist(p);
		utx.commit();
		
		assertNotNull(p.getId());
		String r = given().cookie(getManagerCookie()).expect().statusCode(200)

				.when().get("http://localhost:8080/web/rest/ReportRest/reportTotalTasksPerProjectId/" + p.getId()).asString();
		assertEquals(0l+"",r);	
		
		

	}
	@Test
	public void testGetTotalTasksPerProjectId() throws Exception {
		utx.begin();
		em.joinTransaction();

		Project p = new Project();
		Integer size = 10;
		User worker = new User();
		User supervisor = new User();
		em.persist(worker);
		em.persist(supervisor);
		for (int i = 0; i < size; i++) {
			Task t = new Task();
			t.setWorker(worker);
			t.setApprover(supervisor);
			t.setTaskReportList(new ArrayList<TaskReport>());
			t.setProject(p);
			for (int j = 0; j < size; j++) {
				TaskReport tt = new TaskReport();
				t.getTaskReportList().add(tt);
				em.persist(tt);
			}
			em.persist(t);
		}
		em.persist(p);
		utx.commit();
		assertNotNull(p.getId());
		String r = given().cookie(getManagerCookie()).expect().statusCode(200)

				.when().get("http://localhost:8080/web/rest/ReportRest/reportTotalTasksPerProjectId/" + p.getId()).asString();
		assertEquals(size+"",r);	
		utx.begin();
		em.joinTransaction();
		Project p1 = new Project();
		Integer size1 = 11;

		for (int i = 0; i < size1; i++) {
			Task t = new Task();
			t.setWorker(worker);
			t.setApprover(supervisor);
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < size; j++) {
				TaskReport tt = new TaskReport();
				t.getTaskReportList().add(tt);
				em.persist(tt);
			}
			t.setProject(p1);
			em.persist(t);
		}
		em.persist(p1);
		utx.commit();
		assertNotNull(p1.getId());

		 r = given().cookie(getManagerCookie()).expect().statusCode(200)

				.when().get("http://localhost:8080/web/rest/ReportRest/reportTotalTasksPerProjectId/" + p.getId()).asString();
		assertEquals(size+"",r);	
		 r = given().cookie(getManagerCookie()).expect().statusCode(200)

				.when().get("http://localhost:8080/web/rest/ReportRest/reportTotalTasksPerProjectId/" + p1.getId()).asString();
		assertEquals(size1+"",r);	
		
		
	}
	
	
}
