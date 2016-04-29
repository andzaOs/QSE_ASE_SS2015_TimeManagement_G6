package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.dao.TaskReportDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.WorkingObject;

@RunWith(Arquillian.class)
public class TaskDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	@EJB
	TaskDaoInterface taskDao;

	@EJB
	TaskReportDaoInterface taskReportDao;

	@Test
	public void listTaskForApproverId() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		User approver = new User();
		int size = 10;
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
		}

	}

	@Test
	public void listTaskForApproverIdDel() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		User approver = new User();
		int size = 10;
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
		int i = 0;
		for (Task td : rList) {
			assertNotNull(td.getId());
			taskDao.delete(td.getId());
			i++;

			assertEquals(size - i, taskDao.listTasksForApproverId(approver.getId()).size());
		}

	}

	@Test
	public void listAllDel() throws Exception {
		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		int size = 10;
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
			list.add(t);
			em.persist(t);
		}
		utx.commit();
		List<Task> rList = taskDao.listAll();

		assertEquals(size, rList.size());
		int i = 0;
		for (Task td : rList) {

			assertNotNull(td.getId());
			taskDao.delete(td.getId());
			i++;

			assertEquals(size - i, taskDao.listAll().size());

		}

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIddel() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());

		taskDao.delete(task.getId());
		l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(0, l.size());
	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdManyApprovedApprovedReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		for (int i = 0; i < 10; i++) {
			TaskReport r = new TaskReport();
			r.setStatus(TaskReportStatus.APPROVED);

			em.persist(r);
			lnew.add(r);
		}

		task.setTaskReportList(lnew);

		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(0, l.size());

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdMixedApprovedReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		for (int i = 0; i < 10; i++) {
			TaskReport r = new TaskReport();
			r.setStatus(TaskReportStatus.NEW);

			em.persist(r);
			lnew.add(r);
		}

		for (int i = 0; i < 5; i++) {
			TaskReport r = new TaskReport();
			r.setStatus(TaskReportStatus.REJECTED);
			r.setTask(task);

			lnew.add(r);
			em.persist(r);
		}

		TaskReport r = new TaskReport();
		r.setStatus(TaskReportStatus.APPROVED);
		em.persist(r);
		r.setTask(task);
		lnew.add(r);
		task.setTaskReportList(lnew);

		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(1, l.size());
		assertEquals(task.getId(), l.get(0).getId());

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdMixedNotApprovedReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		for (int i = 0; i < 10; i++) {
			TaskReport r = new TaskReport();
			r.setStatus(TaskReportStatus.NEW);
			r.setTask(task);
			em.persist(r);
			lnew.add(r);
		}

		for (int i = 0; i < 5; i++) {
			TaskReport r = new TaskReport();
			r.setStatus(TaskReportStatus.REJECTED);

			r.setTask(task);
			lnew.add(r);
			em.persist(r);
		}
		task.setTaskReportList(lnew);

		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(1, l.size());
	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdNewReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		TaskReport r = new TaskReport();
		r.setStatus(TaskReportStatus.NEW);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		lnew.add(r);
		r.setTask(task);
		task.setTaskReportList(lnew);

		em.persist(r);
		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());

		assertEquals(1, l.size());
		assertEquals(task.getId(), l.get(0).getId());

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdApproveddReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);

		TaskReport r = new TaskReport();
		r.setTask(task);
		r.setStatus(TaskReportStatus.APPROVED);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		lnew.add(r);
		task.setTaskReportList(lnew);
		em.persist(r);
		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());

		assertEquals(0, l.size());

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdRejecteddReport() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);
		em.persist(approver);
		em.persist(task);

		TaskReport r = new TaskReport();
		r.setTask(task);
		r.setStatus(TaskReportStatus.REJECTED);

		List<TaskReport> lnew = new ArrayList<TaskReport>();
		lnew.add(r);
		task.setTaskReportList(lnew);

		em.persist(r);
		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(1, l.size());
		assertEquals(task.getId(), l.get(0).getId());

	}

	@Test
	public void testListTasksWhichAreNotApprovedForApproverIdEmptyReportList() throws Exception {
		utx.begin();
		em.joinTransaction();
		User approver = new User();
		Task task = new Task();
		task.setApprover(approver);
		em.persist(approver);
		em.persist(task);
		utx.commit();
		assertNotNull(approver.getId());
		List<Task> l = taskDao.listTasksWhichAreNotApprovedForApproverId(approver.getId());
		assertEquals(1, l.size());
		assertEquals(approver.getId(), l.get(0).getApprover().getId());
		assertEquals(task.getId(), l.get(0).getId());

	}

	@Test
	public void testListTasksPage() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		int size = 35;
		Project p = new Project();
		em.persist(p);
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
		}
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			User approver = new User();
			em.persist(approver);

			User worker = new User();
			em.persist(worker);

			WorkingObject workingObject = new WorkingObject();
			em.persist(workingObject);

			// create task
			Task t = new Task();
			t.setApprover(approver);
			t.setDescription(DESC);
			t.setExpectedWorkHours(10.0);
			t.setWorker(worker);
			t.setWorkingObject(workingObject);

			t.setProject(p);
			// persist

			em.persist(t);
		}

		// create task
		Task t = new Task();
		t.setDeleted(true);
		t.setProject(p);
		// persist

		em.persist(t);
		utx.commit();

		assertEquals(10, taskDao.listTasksPaged(p.getId(), 0).size());
		assertEquals(10, taskDao.listTasksPaged(p.getId(), 1).size());
		assertEquals(10, taskDao.listTasksPaged(p.getId(), 2).size());
		assertEquals(5, taskDao.listTasksPaged(p.getId(), 3).size());
		assertEquals(0, taskDao.listTasksPaged(p.getId(), 4).size());

	}

	@Test
	public void testGetItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		int size = 10;
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
			list.add(t);
			em.persist(t);
		}
		utx.commit();
		List<Task> rList = taskDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Task tdx : rList) {
				Task td = taskDao.get(tdx.getId());
				contains = true;

				assertNotNull(td);

				assertNotNull(td.getApprover());
				assertNotNull(td.getWorker());
				assertNotNull(td.getWorkingObject());
				assertNotNull(td.getProject());

				assertNotNull(td.getApprover().getId());
				assertNotNull(td.getWorker().getId());
				assertNotNull(td.getWorkingObject().getId());
				assertNotNull(td.getProject().getId());
				assertEquals(DESC, td.getDescription());

			}

		}
	}

	@Test
	public void testlistTasksForUserIdDel() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		int size = 10;
		User worker = new User();
		em.persist(worker);
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			User approver = new User();
			em.persist(approver);

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
		List<Task> rList = taskDao.listAll();

		assertEquals(size, rList.size());
		int i = 0;

		for (Task td : rList) {

			assertNotNull(td.getId());
			taskDao.delete(td.getId());
			i++;

			assertEquals(size - i, taskDao.listTasksForUserId(worker.getId()).size());
		}

	}

	@Test
	public void testlistTasksForUserId() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		int size = 10;
		User worker = new User();
		em.persist(worker);
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			User approver = new User();
			em.persist(approver);

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
		List<Task> rList = taskDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Task td : rList) {
				if (td.getId().equals(list.get(i).getId())) {
					contains = true;

					assertNotNull(td);
					assertEquals(worker.getId(), td.getWorker().getId());
					assertNotNull(td.getApprover());
					assertNotNull(td.getWorker());
					assertNotNull(td.getWorkingObject());
					assertNotNull(td.getProject());

					assertNotNull(td.getApprover().getId());
					assertNotNull(td.getWorker().getId());
					assertNotNull(td.getWorkingObject().getId());
					assertNotNull(td.getProject().getId());
					assertEquals(DESC, td.getDescription());
				}

			}

			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
	}

	@Test
	public void testGetList() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, taskDao.listAll().size());

		List<Task> list = new ArrayList<Task>();
		int size = 10;
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
			list.add(t);
			em.persist(t);
		}
		utx.commit();
		List<Task> rList = taskDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (Task td : rList) {
				if (td.getId().equals(list.get(i).getId())) {
					contains = true;

					assertNotNull(td);

					assertNotNull(td.getApprover());
					assertNotNull(td.getWorker());
					assertNotNull(td.getWorkingObject());
					assertNotNull(td.getProject());

					assertNotNull(td.getApprover().getId());
					assertNotNull(td.getWorker().getId());
					assertNotNull(td.getWorkingObject().getId());
					assertNotNull(td.getProject().getId());
					assertEquals(DESC, td.getDescription());
				}

			}

			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
	}
}