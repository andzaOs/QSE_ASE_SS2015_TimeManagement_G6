package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.hibernate.Session;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.model.WorkingObject;
import at.tuwien.ase.rest.TaskRest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class TaskRestTest extends TestSuiteRest {
	private static final String DESC = "DESC";

	@EJB
	TaskDaoInterface taskDao;
	
//	TaskRest/listTasksForUserId/ - Worker, Supervisor
	@Test
	public void listTasksForUserIdAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskRest/listTasksForUserId/1",UserType.SUPERVISOR,UserType.WORKER);
	}

//	TaskRest/listTasksForApproverId/ - Supervisor
	@Test
	public void listTasksForApproverIdAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskRest/listTasksForApproverId/1",UserType.SUPERVISOR);
	}
	
//	TaskRest/persist - Supervisor
	@Test
	public void persistAccessTest() throws Exception{
		Task t = new Task();
		utx.begin();
		em.joinTransaction();

		User a = new User();
		User w = new User();
		Project p = new Project();
		TaskType tt = new TaskType();
		WorkingObject wo = new WorkingObject();

		em.persist(a);
		em.persist(w);
		em.persist(p);
		em.persist(tt);
		em.persist(wo);
		t.setTaskType(tt);
		em.persist(t);
		utx.commit();
	
		testRolesAuthPost("http://localhost:8080/web/rest/TaskRest/persist",t,UserType.SUPERVISOR);
	}


	
	@Test
	public void getItemAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
	}

	@Test
	public void testlistTasksPage() throws Exception{
		final TaskDaoInterface mock = Mockito.mock(TaskDaoInterface.class);
		final Session sMock= Mockito.mock(Session.class);
		TaskRest rr = new  TaskRest(){
			{
				super.session=sMock;
				super.taskDao=mock;
				
			}
		};
		rr.listTasksPaged(0L, 0);
		Mockito.verify(mock).listTasksPaged(0L,0);
		
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		for (int i = 0; i < size; i++) {

			// one2one refs (lazy)
			User approver = new User();
			em.persist(approver);

			User worker = new User();
			em.persist(worker);

			WorkingObject workingObject = new WorkingObject();
			em.persist(workingObject);
			Project project = new Project();
			em.persist(project);
			// create task
			Task t = new Task();
			t.setApprover(approver);
			t.setDescription(DESC);
			t.setExpectedWorkHours(10.0);
			t.setWorker(worker);
			t.setWorkingObject(workingObject);
			t.setProject(project);
			// persist

			em.persist(t);
			idArray[i] = t.getId().intValue();
		}
		utx.commit();

		RestAssured.defaultParser = Parser.JSON;

		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {

			r.body("[" + i + "].approver.id", notNullValue()).body("[" + i + "].description", equalTo(DESC))
			// .body("expectedWorkHours", equalTo(10.0))
					.body("[" + i + "].worker.id", notNullValue()).body("[" + i + "].workingObject.id", notNullValue()).body("[" + i + "].project.id", notNullValue());

		}
		r.when().get("http://localhost:8080/web/rest/TaskRest/listAll/");

	}

	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create 
		Task c = new Task();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.delete("http://localhost:8080/web/rest/TaskRest/delete/" + c.getId());	
		
		given().cookie(getManagerCookie()).expect().statusCode(204)
		.when()
				.get("http://localhost:8080/web/rest/TaskRest/getItem/" + c.getId());	
	}

	@Test()
	public void testUpdateTask() throws Exception {
		Task t = new Task();
		utx.begin();
		em.joinTransaction();

		User a = new User();
		User w = new User();
		Project p = new Project();
		TaskType tt = new TaskType();
		WorkingObject wo = new WorkingObject();

		em.persist(a);
		em.persist(w);
		em.persist(p);
		em.persist(tt);
		em.persist(wo);
		t.setTaskType(tt);
		em.persist(t);
		utx.commit();
	
		
		assertNotNull(a.getId());
		assertNotNull(w.getId());
		assertNotNull(p.getId());
		assertNotNull(tt.getId());
		assertNotNull(wo.getId());
		t.setApprover(a);
		t.setWorker(w);
		t.setProject(p);
		t.setWorkingObject(wo);
		
		
		given().cookie(getManagerCookie()).contentType("application/json").body(t).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskRest/persist/");
		List<Task> list = taskDao.listAll();
		assertEquals(1, list.size());
		Task item = list.get(0);
		assertEquals(a.getId(), item.getApprover().getId());
		assertEquals(w.getId(), item.getWorker().getId());
		assertEquals(p.getId(), item.getProject().getId());
		assertEquals(tt.getId(), item.getTaskType().getId());
		assertEquals(wo.getId(), item.getWorkingObject().getId());

	}
	@Test()
	public void testCreateMinimalTask() throws Exception {
		Task t = new Task();
		utx.begin();
		em.joinTransaction();

		TaskType tt = new TaskType();

		em.persist(tt);

		utx.commit();
		t.setTaskType(tt);

		assertNotNull(tt.getId());

		given().cookie(getManagerCookie()).contentType("application/json").body(t).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskRest/persist/");
		List<Task> list = taskDao.listAll();
		assertEquals(1, list.size());
		Task item = list.get(0);
		assertEquals(null, item.getApprover());
		assertEquals(null, item.getWorker());
		assertEquals(null, item.getProject());
		assertEquals(tt.getId(), item.getTaskType().getId());
		assertEquals(null, item.getWorkingObject());

	}

	@Test
	public void  listTaskForApproverIdDel() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		User approver = new User();int size = 10;
		em.persist(approver);
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			


			User worker = new User();
			em.persist(worker);

			WorkingObject workingObject = new WorkingObject();
			em.persist(workingObject);
			Project project = new Project();
			em.persist(project);
			// create task
			Task t = new Task();
			t.setApprover(approver);
			t.setDescription(DESC);
			t.setExpectedWorkHours(10.0);
			t.setWorker(worker);
			t.setWorkingObject(workingObject);
			t.setProject(project);
			// persist
			list.add(t);
			em.persist(t);
		}
		utx.commit();
		List<Task> rList = taskDao.listTasksForApproverId(approver.getId());

		assertEquals(size, rList.size());
			int i=1;
			for (Task td : rList) {
				
				assertNotNull(td);
				assertEquals(td.getApprover().getId(), approver.getId());
				assertNotNull(td.getApprover());
				assertNotNull(td.getWorker());
				assertNotNull(td.getWorkingObject());
				assertNotNull(td.getProject());

				assertNotNull(td.getApprover().getId());
				assertNotNull(td.getWorker().getId());
				assertNotNull(td.getWorkingObject().getId());
				assertNotNull(td.getProject().getId());
				assertEquals(DESC, td.getDescription());
				given().cookie(getManagerCookie()).expect().statusCode(204).when().delete("http://localhost:8080/web/rest/TaskRest/delete/"+td.getId());
				assertEquals(size-i, taskDao.listTasksForApproverId(approver.getId()).size());
				i++;
			}
			

		
	
	}
	
	@Test()
	public void testCreateTask() throws Exception {
		Task t = new Task();
		utx.begin();
		em.joinTransaction();

		User a = new User();
		User w = new User();
		Project p = new Project();
		TaskType tt = new TaskType();
		WorkingObject wo = new WorkingObject();

		em.persist(a);
		em.persist(w);
		em.persist(p);
		em.persist(tt);
		em.persist(wo);

		utx.commit();
		t.setApprover(a);
		t.setWorker(w);
		t.setProject(p);
		t.setTaskType(tt);
		t.setWorkingObject(wo);

		assertNotNull(a.getId());
		assertNotNull(w.getId());
		assertNotNull(p.getId());
		assertNotNull(tt.getId());
		assertNotNull(wo.getId());
		given().cookie(getManagerCookie()).contentType("application/json").body(t).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskRest/persist/");
		List<Task> list = taskDao.listAll();
		assertEquals(1, list.size());
		Task item = list.get(0);
		assertEquals(a.getId(), item.getApprover().getId());
		assertEquals(w.getId(), item.getWorker().getId());
		assertEquals(p.getId(), item.getProject().getId());
		assertEquals(tt.getId(), item.getTaskType().getId());
		assertEquals(wo.getId(), item.getWorkingObject().getId());

	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());
		// one2one refs (lazy)
		User approver = new User();
		em.persist(approver);

		User worker = new User();
		em.persist(worker);

		WorkingObject workingObject = new WorkingObject();
		em.persist(workingObject);
		Project project = new Project();
		em.persist(project);
		// create task
		Task t = new Task();
		t.setApprover(approver);
		t.setDescription(DESC);
		t.setExpectedWorkHours(10.0);
		t.setWorker(worker);
		t.setWorkingObject(workingObject);
		t.setProject(project);
		// persist

		em.persist(t);
		utx.commit();
		assertNotNull(t.getId());

		RestAssured.defaultParser = Parser.JSON;

		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(t.getId().intValue())).body("approver.id", equalTo(approver.getId().intValue())).body("description", equalTo(DESC))
		// .body("expectedWorkHours", equalTo(10.0))
				.body("worker.id", equalTo(worker.getId().intValue())).body("workingObject.id", equalTo(workingObject.getId().intValue())).body("project.id", equalTo(project.getId().intValue()))

				.when().get("http://localhost:8080/web/rest/TaskRest/getItem/" + t.getId());

	}
}