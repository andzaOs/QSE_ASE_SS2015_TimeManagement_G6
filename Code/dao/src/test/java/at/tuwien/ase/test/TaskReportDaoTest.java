package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ResourceUsageDaoInterface;
import at.tuwien.ase.dao.TaskReportDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

@RunWith(Arquillian.class)
public class TaskReportDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	private static final String RMESG = "RMESG";

	@EJB
	ResourceUsageDaoInterface ruDao;
	@EJB
	TaskReportDaoInterface taskReportDao;

	@Test
	public void testGetTaskReportsForWorker() throws Exception {

		utx.begin();
		em.joinTransaction();
		User w2 = new User();
		w2.setTask(new ArrayList<Task>());
		Task t2 = new Task();
		t2.setWorker(w2);
		w2.getTask().add(t2);
		t2.setTaskReportList(new ArrayList<TaskReport>());

		TaskReport trx = new TaskReport();
		trx.setEnd(new Date());
		t2.getTaskReportList().add(trx);

		em.persist(trx);

		em.persist(t2);

		em.persist(w2);

		User w1 = new User();
		w1.setTask(new ArrayList<Task>());

		Task t1 = new Task();
		t1.setTaskReportList(new ArrayList<TaskReport>());
		w1.getTask().add(t1);
		t1.setWorker(w1);

		TaskReport tr1 = new TaskReport();
		tr1.setEnd(new Date());
		t1.getTaskReportList().add(tr1);
		tr1.setResourceUsageList(new ArrayList<ResourceUsage>());
		tr1.setTask(t1);
		for (int i = 0; i < 3; i++) {
			ResourceUsage r = new ResourceUsage();
			r.setTaskReport(tr1);
			tr1.getResourceUsageList().add(r);
			em.persist(r);
		}
		TaskReport tr2 = new TaskReport();
		tr2.setResourceUsageList(new ArrayList<ResourceUsage>());
		tr2.setEnd(new Date());
		t1.getTaskReportList().add(tr2);
		tr2.setTask(t1);
		for (int i = 0; i < 3; i++) {
			ResourceUsage r = new ResourceUsage();
			r.setTaskReport(tr2);
			tr2.getResourceUsageList().add(r);
			em.persist(r);
		}

		em.persist(w1);

		em.persist(t1);

		em.persist(tr1);

		em.persist(tr2);

		utx.commit();

		assertNotNull(w1.getId());

		assertEquals(2, taskReportDao.getTaskReportsForWorker(w1.getId()).size());
		
		for ( TaskReport t : taskReportDao.getAllTaskReportsForWorker(w1.getId())) {
			assertEquals(3, t.getResourceUsageList().size());
		}
		utx.begin();
		em.joinTransaction();

		t1 = em.find(Task.class, t1.getId());

		TaskReport tr = new TaskReport();
		tr.setEnd(new Date(System.currentTimeMillis() - 23 * 3600 * 1000));
		tr.setTask(t1);
		t1.getTaskReportList().add(tr);

		em.persist(tr);
		em.persist(t1);

		utx.commit();
		assertEquals(3, taskReportDao.getTaskReportsForWorker(w1.getId()).size());

		utx.begin();
		em.joinTransaction();
		tr = em.find(TaskReport.class, tr.getId());
		tr.setEnd(new Date(System.currentTimeMillis() - 24 * 3600 * 1000 + 1));

		em.persist(tr);

		utx.commit();
		assertEquals(2, taskReportDao.getTaskReportsForWorker(w1.getId()).size());

	}
	@Test
	public void testGetAllTaskReportsForWorker() throws Exception {

		utx.begin();
		em.joinTransaction();
		User w2 = new User();
		w2.setTask(new ArrayList<Task>());
		Task t2 = new Task();
		t2.setWorker(w2);
		w2.getTask().add(t2);
		t2.setTaskReportList(new ArrayList<TaskReport>());

		TaskReport trx = new TaskReport();
		trx.setEnd(new Date());
		t2.getTaskReportList().add(trx);

		em.persist(trx);

		em.persist(t2);

		em.persist(w2);

		User w1 = new User();
		w1.setTask(new ArrayList<Task>());

		Task t1 = new Task();
		t1.setTaskReportList(new ArrayList<TaskReport>());
		w1.getTask().add(t1);
		t1.setWorker(w1);

		TaskReport tr1 = new TaskReport();
		tr1.setResourceUsageList(new ArrayList<ResourceUsage>());
		tr1.setEnd(new Date());
		t1.getTaskReportList().add(tr1);
		tr1.setTask(t1);
		for (int i = 0; i < 3; i++) {
			
			ResourceUsage r = new ResourceUsage();
			tr1.getResourceUsageList().add(r);
			r.setTaskReport(tr1);
			em.persist(r);
		}

		TaskReport tr2 = new TaskReport();
		tr2.setResourceUsageList(new ArrayList<ResourceUsage>());
		tr2.setEnd(new Date());
		t1.getTaskReportList().add(tr2);
		tr2.setTask(t1);
		for (int i = 0; i < 3; i++) {
			ResourceUsage r = new ResourceUsage();
			r.setTaskReport(tr2);
			tr2.getResourceUsageList().add(r);
			em.persist(r);
		}
		em.persist(w1);

		em.persist(t1);

		em.persist(tr1);

		em.persist(tr2);

		utx.commit();

		assertNotNull(w1.getId());

		assertEquals(2, taskReportDao.getAllTaskReportsForWorker(w1.getId()).size());
		for ( TaskReport t : taskReportDao.getAllTaskReportsForWorker(w1.getId())) {
			assertEquals(3, t.getResourceUsageList().size());
		}
		utx.begin();
		em.joinTransaction();

		t1 = em.find(Task.class, t1.getId());

		TaskReport tr = new TaskReport();
		tr.setEnd(new Date(System.currentTimeMillis() - 23 * 3600 * 1000));
		tr.setTask(t1);
		t1.getTaskReportList().add(tr);

		em.persist(tr);
		em.persist(t1);

		utx.commit();
		assertEquals(3, taskReportDao.getAllTaskReportsForWorker(w1.getId()).size());
		
		utx.begin();
		em.joinTransaction();
		tr = em.find(TaskReport.class, tr.getId());
		tr.setEnd(new Date(System.currentTimeMillis() - 24 * 3600 * 1000 + 1));

		em.persist(tr);

		utx.commit();
		assertEquals(3, taskReportDao.getAllTaskReportsForWorker(w1.getId()).size());

	}
	@Test
	public void testGetTaskReportsForWorkerDel() throws Exception {

		utx.begin();
		em.joinTransaction();
		User w2 = new User();
		w2.setTask(new ArrayList<Task>());
		Task t2 = new Task();
		t2.setWorker(w2);
		w2.getTask().add(t2);
		t2.setTaskReportList(new ArrayList<TaskReport>());

		TaskReport trx = new TaskReport();
		trx.setEnd(new Date());
		t2.getTaskReportList().add(trx);

		em.persist(trx);

		em.persist(t2);

		em.persist(w2);

		User w1 = new User();
		w1.setTask(new ArrayList<Task>());

		Task t1 = new Task();
		t1.setTaskReportList(new ArrayList<TaskReport>());
		w1.getTask().add(t1);
		t1.setWorker(w1);

		TaskReport tr1 = new TaskReport();
		tr1.setEnd(new Date());
		t1.getTaskReportList().add(tr1);
		tr1.setTask(t1);

		TaskReport tr2 = new TaskReport();
		tr2.setEnd(new Date());
		t1.getTaskReportList().add(tr2);
		tr2.setTask(t1);

		em.persist(w1);

		em.persist(t1);

		em.persist(tr1);

		em.persist(tr2);

		utx.commit();

		assertNotNull(w1.getId());

		assertEquals(2, taskReportDao.getTaskReportsForWorker(w1.getId()).size());
		utx.begin();
		em.joinTransaction();

		t1 = em.find(Task.class, t1.getId());

		TaskReport tr = new TaskReport();
		tr.setEnd(new Date(System.currentTimeMillis() - 23 * 3600 * 1000));
		tr.setTask(t1);
		t1.getTaskReportList().add(tr);

		em.persist(tr);
		em.persist(t1);

		utx.commit();
		assertEquals(3, taskReportDao.getTaskReportsForWorker(w1.getId()).size());

		utx.begin();
		em.joinTransaction();
		tr = em.find(TaskReport.class, tr.getId());
		tr.setEnd(new Date(System.currentTimeMillis() - 24 * 3600 * 1000 + 1));

		em.persist(tr);

		utx.commit();
		assertEquals(2, taskReportDao.getTaskReportsForWorker(w1.getId()).size());
		taskReportDao.delete(tr1.getId());
		assertEquals(1, taskReportDao.getTaskReportsForWorker(w1.getId()).size());
		taskReportDao.delete(tr2.getId());
		assertEquals(0, taskReportDao.getTaskReportsForWorker(w1.getId()).size());

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

		taskReportDao.approveTaskList(list);

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

		taskReportDao.rejectTaskReport(t);
		td = taskReportDao.get(t.getId());
		assertEquals(TaskReportStatus.REJECTED, td.getStatus());
	}

	@Test
	public void testGetReportsToApproveByApproveIdDel() throws Exception {

		utx.begin();
		em.joinTransaction();
		User u1 = new User();
		u1.setUserType(UserType.SUPERVISOR);

		User u2 = new User();
		u2.setUserType(UserType.SUPERVISOR);

		Task t2 = new Task();
		t2.setApprover(u2);
		t2.setTaskReportList(new ArrayList<TaskReport>());
		for (int i = 0; i < 4; i++) {// NEW
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.NEW);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 4; i++) {// REJECTED
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.REJECTED);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 10; i++) {// APPROVED
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.APPROVED);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}

		Task t = new Task();
		t.setApprover(u1);
		t.setTaskReportList(new ArrayList<TaskReport>());
		for (int i = 0; i < 3; i++) {// NEW
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.NEW);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 7; i++) {// REJECTED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.REJECTED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 10; i++) {// APPROVED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.APPROVED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}

		em.persist(u1);
		em.persist(u2);
		em.persist(t);
		em.persist(t2);

		utx.commit();

		assertNotNull(u1.getId());

		assertNotNull(u2.getId());

		List<TaskReport> u1l = taskReportDao.getReportsToApproveByApproveId(u1.getId());
		List<TaskReport> u2l = taskReportDao.getReportsToApproveByApproveId(u2.getId());
		int i = 0;
		for (TaskReport taskReport : u1l) {
			taskReportDao.delete(taskReport.getId());
			i++;
			assertEquals(10 - i, taskReportDao.getReportsToApproveByApproveId(u1.getId()).size());
		}
		i = 0;
		for (TaskReport taskReport : u2l) {
			taskReportDao.delete(taskReport.getId());
			i++;
			assertEquals(8 - i, taskReportDao.getReportsToApproveByApproveId(u2.getId()).size());
		}
	}

	@Test
	public void testGetReportsToApproveByApproveId() throws Exception {

		utx.begin();
		em.joinTransaction();
		User u1 = new User();
		u1.setUserType(UserType.SUPERVISOR);

		User u2 = new User();
		u2.setUserType(UserType.SUPERVISOR);

		Task t2 = new Task();
		t2.setApprover(u2);
		t2.setTaskReportList(new ArrayList<TaskReport>());
		for (int i = 0; i < 4; i++) {// NEW
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.NEW);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 4; i++) {// REJECTED
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.REJECTED);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 10; i++) {// APPROVED
			TaskReport tr = new TaskReport();
			tr.setTask(t2);
			tr.setStatus(TaskReportStatus.APPROVED);
			t2.getTaskReportList().add(tr);
			em.persist(tr);
		}

		Task t = new Task();
		t.setApprover(u1);
		t.setTaskReportList(new ArrayList<TaskReport>());
		for (int i = 0; i < 3; i++) {// NEW
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.NEW);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 7; i++) {// REJECTED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.REJECTED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}
		for (int i = 0; i < 10; i++) {// APPROVED
			TaskReport tr = new TaskReport();
			tr.setTask(t);
			tr.setStatus(TaskReportStatus.APPROVED);
			t.getTaskReportList().add(tr);
			em.persist(tr);
		}

		em.persist(u1);
		em.persist(u2);
		em.persist(t);
		em.persist(t2);

		utx.commit();

		assertNotNull(u1.getId());

		assertNotNull(u2.getId());

		assertEquals(10, taskReportDao.getReportsToApproveByApproveId(u1.getId()).size());
		assertEquals(8, taskReportDao.getReportsToApproveByApproveId(u2.getId()).size());
	}

	@Test
	public void testGetItemDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		TaskReport tr = new TaskReport();
		Date b = new Date(15500);
		Date e = new Date(35500);
		tr.setBegin(b);
		tr.setDescription(DESC);
		tr.setEnd(e);
		tr.setRejectMessage(RMESG);
		Task t = new Task();

		tr.setTask(t);
		// persist
		em.persist(t);
		em.persist(tr);
		int size = 10;
		tr.setResourceUsageList(new ArrayList<ResourceUsage>());
		for (int j = 0; j < size; j++) {
			ResourceUsage u = new ResourceUsage();
			u.setTaskReport(tr);
			tr.getResourceUsageList().add(u);
			em.persist(u);
		}
		utx.commit();
		assertNotNull(tr.getId());
		assertNotNull(t.getId());
		TaskReport td = taskReportDao.get(tr.getId());
		int i = 0;
		for (ResourceUsage o : td.getResourceUsageList()) {
			ruDao.delete(o.getId());
			i++;
			assertEquals(size - i, taskReportDao.get(tr.getId()).getResourceUsageList().size());
		}
	}

	@Test
	public void testListAllDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		Date begin = new Date(15500);
		Date end = new Date(35500);
		List<TaskReport> list = new ArrayList<TaskReport>();
		int size = 10;

		TaskReport tr = new TaskReport();

		tr.setBegin(begin);
		tr.setDescription(DESC);
		tr.setEnd(end);
		tr.setRejectMessage(RMESG);
		Task t = new Task();

		tr.setTask(t);
		// persist
		em.persist(t);
		em.persist(tr);
		list.add(tr);
		tr.setResourceUsageList(new ArrayList<ResourceUsage>());
		for (int j = 0; j < size; j++) {
			ResourceUsage u = new ResourceUsage();
			u.setTaskReport(tr);
			tr.getResourceUsageList().add(u);
			em.persist(u);
		}
		utx.commit();

		List<TaskReport> rList = taskReportDao.listAll();
		assertEquals(1, rList.size());
		tr = rList.get(0);

		int i = 0;
		for (ResourceUsage ru : tr.getResourceUsageList()) {
			ruDao.delete(ru.getId());
			i++;
			assertEquals(size - i, taskReportDao.listAll().get(0).getResourceUsageList().size());
		}
		taskReportDao.delete(tr.getId());
		assertEquals(0, taskReportDao.listAll().size());
	}

	@Test
	public void testGetTaskReportsForProject() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		Date begin = new Date(15500);
		Date end = new Date(35500);
		List<TaskReport> list = new ArrayList<TaskReport>();
		List<TaskReport> list2 = new ArrayList<TaskReport>();
		int size = 10;
		int size2 = 15;
		Project p1=new Project();
		Project p2=new Project();
		em.persist(p1);
		em.persist(p2);
		for (int i = 0; i < size; i++) {

			TaskReport tr = new TaskReport();

			tr.setBegin(begin);
			tr.setDescription(DESC);
			tr.setEnd(end);
			tr.setRejectMessage(RMESG);
			Task t = new Task();
			t.setProject(p1);
			tr.setTask(t);
			// persist
			em.persist(t);
			em.persist(tr);
			list.add(tr);
			tr.setResourceUsageList(new ArrayList<ResourceUsage>());
			
			for (int j = 0; j < size; j++) {
				ResourceUsage u = new ResourceUsage();
				u.setTaskReport(tr);
				em.persist(u);
			}

		}
		
		for (int i = 0; i < size2; i++) {

			TaskReport tr = new TaskReport();

			tr.setBegin(begin);
			tr.setDescription(DESC);
			tr.setEnd(end);
			tr.setRejectMessage(RMESG);
			Task t = new Task();
			t.setProject(p2);
			tr.setTask(t);
			// persist
			em.persist(t);
			em.persist(tr);
			list2.add(tr);
			tr.setResourceUsageList(new ArrayList<ResourceUsage>());
			
			for (int j = 0; j < size2; j++) {
				ResourceUsage u = new ResourceUsage();
				u.setTaskReport(tr);
				em.persist(u);
			}

		}
		utx.commit();

		List<TaskReport> rList = taskReportDao.getTaskReportsForProject(p1.getId());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (TaskReport tr : rList) {
				if (tr.getId().equals(list.get(i).getId())) {
					contains = true;
			
					assertNotNull(tr.getId());
				}

			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}
		}
		
		 rList = taskReportDao.getTaskReportsForProject(p2.getId());
		for (int i = 0; i < size2; i++) {
			boolean contains = false;
			for (TaskReport tr : rList) {
				if (tr.getId().equals(list2.get(i).getId())) {
					contains = true;

					assertNotNull(tr.getId());
				}

			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}
		}
	}

	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		Date begin = new Date(15500);
		Date end = new Date(35500);
		List<TaskReport> list = new ArrayList<TaskReport>();
		int size = 10;
		for (int i = 0; i < size; i++) {

			TaskReport tr = new TaskReport();

			tr.setBegin(begin);
			tr.setDescription(DESC);
			tr.setEnd(end);
			tr.setRejectMessage(RMESG);
			Task t = new Task();

			tr.setTask(t);
			// persist
			em.persist(t);
			em.persist(tr);
			list.add(tr);
			tr.setResourceUsageList(new ArrayList<ResourceUsage>());
			
			for (int j = 0; j < size; j++) {
				ResourceUsage u = new ResourceUsage();
				u.setTaskReport(tr);
				em.persist(u);
			}

		}
		utx.commit();

		List<TaskReport> rList = taskReportDao.listAll();
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (TaskReport tr : rList) {
				if (tr.getId().equals(list.get(i).getId())) {
					contains = true;
					assertEquals(size, rList.size());
					assertNotNull(tr.getId());
					assertEquals(size, tr.getResourceUsageList().size());
					assertEquals(begin, tr.getBegin());
					assertEquals(DESC, tr.getDescription());
					assertEquals(end, tr.getEnd());
					assertEquals(RMESG, tr.getRejectMessage());
					assertNotNull(tr.getTask().getId());
				}

			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}
		}
	}
	@Test
	public void testGetLatest10TaskReportForProjectId() throws Exception {

		utx.begin();
		em.joinTransaction();
		Project p = new Project();
		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		Date begin = new Date(15500);
		Date end = new Date(35500);
		List<TaskReport> list = new ArrayList<TaskReport>();
		int size = 20;
		for (int i = 0; i < size; i++) {

			TaskReport tr = new TaskReport();
			
			tr.setBegin(begin);
			tr.setDescription(DESC);
			tr.setEnd(end);
			tr.setRejectMessage(RMESG);
			Task t = new Task();
			t.setProject(p);
			tr.setTask(t);
			// persist
			em.persist(t);
			em.persist(tr);
			list.add(tr);
			tr.setResourceUsageList(new ArrayList<ResourceUsage>());
			

		}
		em.persist(p);
		utx.commit();

		List<TaskReport> rList = taskReportDao.getLatest10TaskReportForProjectId(p.getId());
		assertEquals(10, rList.size());
	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskReportDao.listAll().size());
		TaskReport tr = new TaskReport();
		Date b = new Date(15500);
		Date e = new Date(35500);
		tr.setBegin(b);
		tr.setDescription(DESC);
		tr.setEnd(e);
		tr.setRejectMessage(RMESG);
		Task t = new Task();
		int size = 10;
		tr.setResourceUsageList(new ArrayList<ResourceUsage>());
		for (int j = 0; j < size; j++) {
			ResourceUsage u = new ResourceUsage();
			u.setTaskReport(tr);
			tr.getResourceUsageList().add(u);
			em.persist(u);
		}
		tr.setTask(t);
		// persist
		em.persist(t);
		em.persist(tr);
		utx.commit();
		assertNotNull(tr.getId());
		assertNotNull(t.getId());
		TaskReport td = taskReportDao.get(tr.getId());

		assertEquals(b, td.getBegin());
		assertEquals(DESC, td.getDescription());
		assertEquals(e, td.getEnd());
		assertEquals(RMESG, td.getRejectMessage());
		assertEquals(size, tr.getResourceUsageList().size());
		assertEquals(t.getId(), td.getTask().getId());

	}
}