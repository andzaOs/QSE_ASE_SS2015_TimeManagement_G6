package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.hibernate.Session;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import at.tuwien.ase.dao.TaskReportDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.rest.TaskReportRest;

import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class TaskReportRestTest extends TestSuiteRest {
	private static final String DESC = "DESC";

	private static final String RMESG = "RMESG";

	@EJB
	TaskReportDaoInterface taskReportDao;


//	TaskReportRest/approve - Supervisor
	@Test
	public void approveAccessTest() throws Exception{
		List<TaskReport> list = new ArrayList<TaskReport>();
		testRolesAuthPost("http://localhost:8080/web/rest/TaskReportRest/approve/",list,UserType.SUPERVISOR);
	}
//	TaskReportRest/reject - Supervisor
	@Test
	public void rejectAccessTest() throws Exception{
		utx.begin();
		em.joinTransaction();
		TaskReport t = new TaskReport();
		t.setStatus(TaskReportStatus.NEW);
		em.persist(t);
		utx.commit();

		
		testRolesAuthPost("http://localhost:8080/web/rest/TaskReportRest/reject/",t,UserType.SUPERVISOR);
	}
//	TaskReportRest/getReportsToApproveByApproveId/ - Supervisor
	@Test
	public void getReportsToApproveByApproveIdAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskReportRest/getReportsToApproveByApproveId/1",UserType.SUPERVISOR);
	}
//	TaskReportRest/listAllForWorker/ - Worker
	@Test
	public void listAllForWorkerAccessTest() throws Exception{
		
		testRolesAuth("http://localhost:8080/web/rest/TaskReportRest/listAllForWorker/1",UserType.SUPERVISOR,UserType.WORKER);
	}
//	TaskReportRest/persist - Worker, Supervisor
	@Test
	public void getPersistAccessTest() throws Exception{
		
		testRolesAuthPost("http://localhost:8080/web/rest/TaskReportRest/persist",new TaskReport(),UserType.WORKER,UserType.SUPERVISOR);
	}

	
	
	@Test
	public void testGetTaskReportsForProject() throws Exception{
		final TaskReportDaoInterface mock = Mockito.mock(TaskReportDaoInterface.class);
		final Session sMock= Mockito.mock(Session.class);
		TaskReportRest rr = new  TaskReportRest(){
			{
				super.session=sMock;
				super.taskReportDao=mock;
				
			}
		};
		rr.getTaskReportsForProject(0L);
		Mockito.verify(mock).getTaskReportsForProject(0L);
		
	}
	

	@Test
	public void testGetTaskReportsForWorker() throws Exception{
		final TaskReportDaoInterface mock = Mockito.mock(TaskReportDaoInterface.class);
		final Session sMock= Mockito.mock(Session.class);
		TaskReportRest rr = new  TaskReportRest(){
			{
				super.session=sMock;
				super.taskReportDao=mock;
				
			}
		};
		rr.getTaskReportsForWorker(0L);
		Mockito.verify(mock).getTaskReportsForWorker(0L);
		
	}

	@Test
	public void testListAllForWorker() throws Exception{
		final TaskReportDaoInterface mock = Mockito.mock(TaskReportDaoInterface.class);
		final Session sMock= Mockito.mock(Session.class);
		TaskReportRest rr = new  TaskReportRest(){
			{
				
				super.session=sMock;
				super.taskReportDao=mock;
				
			}
		};
		rr.listAllForWorker(0L);
		Mockito.verify(mock).getAllTaskReportsForWorker(0L);
		
	}

	@Test
	public void testGetLatest10TaskReportForProjectId() throws Exception{
		final TaskReportDaoInterface mock = Mockito.mock(TaskReportDaoInterface.class);
		final Session sMock= Mockito.mock(Session.class);
		
		TaskReportRest rr = new  TaskReportRest(){
			{
				super.session=sMock;
				taskReportDao=mock;
				
			}
		};
		rr.getLatest10TaskReportForProjectId(0L);
		Mockito.verify(mock).getLatest10TaskReportForProjectId(0L);
		
	}
	@Test
	public void testDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		// create
		TaskReport c = new TaskReport();

		// persist

		em.persist(c);

		assertNotNull(c.getId());

		utx.commit();
		given().cookie(getManagerCookie()).expect().statusCode(204).when().delete("http://localhost:8080/web/rest/TaskReportRest/delete/" + c.getId());

		given().cookie(getManagerCookie()).expect().statusCode(204).when().get("http://localhost:8080/web/rest/TaskReportRest/getItem/" + c.getId());
	}

	@Test
	public void testRejectTask() throws Exception {

		utx.begin();
		em.joinTransaction();
		TaskReport t = new TaskReport();
		t.setStatus(TaskReportStatus.NEW);
		em.persist(t);
		utx.commit();
		assertNotNull(t.getId());

		TaskReport td = taskReportDao.get(t.getId());
		assertEquals(TaskReportStatus.NEW, td.getStatus());

		given().cookie(getManagerCookie()).given().contentType("application/json").body(t).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskReportRest/reject");
		td = taskReportDao.get(t.getId());
		assertEquals(TaskReportStatus.REJECTED, td.getStatus());
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		int size = 10;
		Integer[] idArray = new Integer[size];
		Date b = new Date();
		Date e = new Date();
		for (int i = 0; i < size; i++) {

			TaskReport tr = new TaskReport();

			tr.setBegin(b);
			tr.setDescription(DESC);
			tr.setEnd(e);
			tr.setRejectMessage(RMESG);
			Task t = new Task();

			tr.setTask(t);
			// persist
			em.persist(t);
			em.persist(tr);

			idArray[i] = tr.getId().intValue();
		}
		utx.commit();

		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < size; i++) {
			r.body("[" + i + "].id", isOneOf(idArray)).body("[" + i + "].begin", equalTo(b.getTime())).body("[" + i + "].end", equalTo(e.getTime())).body("[" + i + "].description", equalTo(DESC))
					.body("[" + i + "].rejectMessage", equalTo(RMESG)).body("[" + i + "].task.id", notNullValue());
		}

		r.when().get("http://localhost:8080/web/rest/TaskReportRest/listAll/");

	}

	@Test
	public void testApproveTaskList() throws Exception {

		utx.begin();
		em.joinTransaction();
		List<TaskReport> list = new ArrayList<TaskReport>();
		for (int i = 0; i < 5; i++) {
			TaskReport t = new TaskReport();

			t.setStatus(TaskReportStatus.NEW);

			em.persist(t);
		}

		for (int i = 0; i < 10; i++) {
			TaskReport t = new TaskReport();

			t.setStatus(TaskReportStatus.NEW);

			em.persist(t);
			assertNotNull(t.getId());
			list.add(t);
		}

		utx.commit();
		List<TaskReport> l = taskReportDao.listAll();
		int apCount = 0;
		int newCount = 0;
		for (TaskReport tr : l) {
			if (tr.getStatus() == TaskReportStatus.NEW) {
				newCount++;
			} else if (tr.getStatus() == TaskReportStatus.APPROVED) {
				apCount++;
			} else {
				fail("unexpected status");
			}
		}
		assertEquals(newCount, 15);
		assertEquals(apCount, 0);
		given().cookie(getManagerCookie()).contentType("application/json").body(list).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskReportRest/approve");

		l = taskReportDao.listAll();
		apCount = 0;
		newCount = 0;
		for (TaskReport tr : l) {
			if (tr.getStatus() == TaskReportStatus.NEW) {
				newCount++;
			} else if (tr.getStatus() == TaskReportStatus.APPROVED) {
				apCount++;
			} else {
				fail("unexpected status");
			}
		}
		assertEquals(newCount, 5);
		assertEquals(apCount, 10);

	}

	@Test
	public void testPersistTaskReport() throws Exception {

		String desc = "desc";
		String desc2 = "desc2";
		Double cost = 10.0;
		Double cost2 = 11.0;
		Date begin = new Date();
		utx.begin();
		em.joinTransaction();
		Task t = new Task();
		User app = new User();
		t.setApprover(app);
		em.persist(app);

		User work = new User();
		t.setWorker(work);
		em.persist(work);
		Resource resource = new Resource();

		resource.setCategory(new Category());
		em.persist(resource.getCategory());

		em.persist(resource);
		em.persist(t);
		utx.commit();
		assertNotNull(t.getId());
		TaskReport tr1 = new TaskReport();

		tr1.setTask(t);

		tr1.setBegin(begin);

		tr1.setDescription(desc);
		tr1.setResourceUsageList(new ArrayList<ResourceUsage>());

		for (int i = 0; i < 10; i++) {
			ResourceUsage ru = new ResourceUsage();
			ru.setResource(resource);
			ru.setCost(cost);
			ru.setBegin(begin);
			tr1.getResourceUsageList().add(ru);

			ru.setResource(resource);

		}

		given().cookie(getManagerCookie()).contentType("application/json").body(tr1).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskReportRest/persist/");
		List<TaskReport> list = taskReportDao.listAll();
		assertEquals(1, list.size());
		assertEquals(t.getId(), list.get(0).getTask().getId());
		assertEquals(10, list.get(0).getResourceUsageList().size());
		for (ResourceUsage x : list.get(0).getResourceUsageList()) {
			assertEquals(begin, x.getBegin());
			assertEquals(cost, x.getCost());
			assertEquals(list.get(0), x.getTaskReport());
		}
		assertEquals(begin, list.get(0).getBegin());
		tr1.setId(list.get(0).getId());

		tr1.setRejectMessage("BLA BLA");
		tr1.setStatus(TaskReportStatus.REJECTED);

		for (int i = 0; i < 10; i++) {
			tr1.getResourceUsageList().get(i).setId(list.get(0).getResourceUsageList().get(i).getId());

		}
		given().cookie(getManagerCookie()).contentType("application/json").body(tr1).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskReportRest/persist/");
		list = taskReportDao.listAll();
		assertEquals(1, list.size());
		assertEquals(t.getId(), list.get(0).getTask().getId());
		assertEquals(10, list.get(0).getResourceUsageList().size());
		for (ResourceUsage x : list.get(0).getResourceUsageList()) {
			assertEquals(begin, x.getBegin());
			assertEquals(cost, x.getCost());
			assertEquals(list.get(0), x.getTaskReport());
		}
		assertEquals(begin, list.get(0).getBegin());
		assertEquals("BLA BLA", list.get(0).getRejectMessage());
		tr1.setResourceUsageList(null);

		given().cookie(getManagerCookie()).contentType("application/json").body(tr1).expect().statusCode(204).when().post("http://localhost:8080/web/rest/TaskReportRest/persist/");
		list = taskReportDao.listAll();
		assertEquals(1, list.size());
		assertEquals(10, list.get(0).getResourceUsageList().size());

	}

	@Test
	public void testGetReportsToApproveByApproveId() throws Exception {

		utx.begin();
		em.joinTransaction();
		User u1 = new User();
		u1.setUserType(UserType.SUPERVISOR);

		Task t = new Task();
		t.setApprover(u1);
		t.setTaskReportList(new ArrayList<TaskReport>());

		Integer[] idArray = new Integer[10];
		for (int i = 0; i < 3; i++) {// NEW
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.NEW);
			t.getTaskReportList().add(tr);
			em.persist(tr);
			idArray[i] = tr.getId().intValue();
		}
		for (int i = 0; i < 7; i++) {// REJECTED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.REJECTED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
			idArray[i + 3] = tr.getId().intValue();
		}
		for (int i = 0; i < 11; i++) {// APPROVED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.APPROVED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}

		em.persist(u1);
		em.persist(t);

		utx.commit();

		assertNotNull(u1.getId());

		ResponseSpecification r = given().cookie(getManagerCookie()).expect().statusCode(200);

		for (int i = 0; i < 10; i++) {

			r.body("[" + i + "].id", isOneOf(idArray)).body("[" + i + "].task.id", notNullValue());
		}
		r.body("$.size()", equalTo(10));
		r.when().get("http://localhost:8080/web/rest/TaskReportRest/getReportsToApproveByApproveId/" + u1.getId());
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		TaskReport tr = new TaskReport();
		Date b = new Date();
		Date e = new Date();
		tr.setBegin(b);
		tr.setDescription(DESC);
		tr.setEnd(e);
		tr.setRejectMessage(RMESG);
		Task t = new Task();

		tr.setTask(t);
		// persist
		em.persist(t);
		em.persist(tr);
		utx.commit();
		assertNotNull(tr.getId());
		assertNotNull(t.getId());

		given().cookie(getManagerCookie()).expect().statusCode(200).body("id", equalTo(tr.getId().intValue())).body("begin", equalTo(b.getTime())).body("end", equalTo(e.getTime())).body("description", equalTo(DESC))
				.body("rejectMessage", equalTo(RMESG)).body("task.id", equalTo(t.getId().intValue())).when().get("http://localhost:8080/web/rest/TaskReportRest/getItem/" + tr.getId());

	}
}