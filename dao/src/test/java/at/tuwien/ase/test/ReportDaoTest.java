package at.tuwien.ase.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.ReportDaoInterface;
import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class ReportDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	@EJB
	ReportDaoInterface reportDao;
	@EJB
	private TaskDaoInterface taskdao;

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
		assertEquals(new Long(0), reportDao.getTotalTasksPerProjectId(p.getId()));
		assertNotNull(p.getId());

		

	}
	@Test
	public void testGetTotalFinishedTasksPerProjectId() throws Exception{
		utx.begin();
		em.joinTransaction();

		Project p = new Project();

	
		em.persist(p);
		
		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			em.persist(t);
			t.setProject(p);
			t.setFinished(true);
			
		}
		utx.commit();
		assertEquals(new Long(10), reportDao.getTotalFinishedTasksPerProjectId(p.getId()));
		

		

	}
	@Test
	public void testGetTotalTaskCountForProjectId() throws Exception {
		utx.begin();
		em.joinTransaction();

		Project p = new Project();
		Long size = 10l;
		User worker = new User();
		User supervisor = new User();
		em.persist(worker);
		em.persist(supervisor);
		for (int i = 0; i < size; i++) {
			Task t = new Task();
			t.setWorker(worker);
			t.setApprover(supervisor);
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < size; j++) {
				TaskReport tt = new TaskReport();
				t.getTaskReportList().add(tt);

				em.persist(tt);

			}
			t.setProject(p);
			em.persist(t);
		}
		em.persist(p);
		utx.commit();
		assertEquals(size, reportDao.getTotalTasksPerProjectId(p.getId()));
		assertNotNull(p.getId());

		utx.begin();
		em.joinTransaction();
		Project p1 = new Project();
		Long size1 = 11l;

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
		assertEquals(size, reportDao.getTotalTasksPerProjectId(p.getId()));
		assertEquals(size1, reportDao.getTotalTasksPerProjectId(p1.getId()));
		int i = 1;
		for (Task t : taskdao.listAll()) {
			taskdao.delete(t.getId());
			assertEquals(size + size1 - i, reportDao.getTotalTasksPerProjectId(p.getId()) + reportDao.getTotalTasksPerProjectId(p1.getId()));

			i++;

		}
		
		

	}

	@Test
	public void testGetTotalHoursForProject() throws Exception {

		utx.begin();
		em.joinTransaction();

		Project p = new Project();

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		// TEST NO DATA DATA VALUES
		utx.begin();
		em.joinTransaction();
		p = em.find(Project.class, p.getId());
		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();

				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);
		utx.commit();
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		// TEST 1 DATA DATA VALUES
		utx.begin();
		em.joinTransaction();
		p = em.find(Project.class, p.getId());
		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(1232130));// 1H
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);
		utx.commit();
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		// TEST 1 DATA DATA VALUES
		utx.begin();
		em.joinTransaction();
		p = em.find(Project.class, p.getId());
		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setEnd(new Date(10000));// 1H
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);
		utx.commit();
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		// test DIFFERENT STATUS
		utx.begin();
		em.joinTransaction();
		p = em.find(Project.class, p.getId());

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.NEW);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		// test DIFFERENT STATUS
		utx.begin();
		em.joinTransaction();
		p = em.find(Project.class, p.getId());

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.REJECTED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

		utx.begin();
		em.joinTransaction();

		p = em.find(Project.class, p.getId());

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 200.0, reportDao.getTotalHoursForProject(p.getId()));

	}

	@Test
	public void testGetTotalHoursForProjectEmpty() throws Exception {

		utx.begin();
		em.joinTransaction();

		Project p = new Project();

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			
			t.setProject(p);
			em.persist(t);
		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 0.0, reportDao.getTotalHoursForProject(p.getId()));

	}

	@Test
	public void testDeleted() throws Exception {

		utx.begin();
		em.joinTransaction();

		Project p = new Project();

		for (int i = 0; i < 10; i++) {
			Task t = new Task();
			t.setTaskReportList(new ArrayList<TaskReport>());
			for (int j = 0; j < 5; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				em.persist(r);
			}
			for (int j = 0; j < 5; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				t.setDeleted(false);
				em.persist(r);
			}
			for (int j = 0; j < 10; j++) {
				TaskReport r = new TaskReport();
				r.setBegin(new Date(0));// 1H
				r.setEnd(new Date(3600000));
				r.setStatus(TaskReportStatus.APPROVED);
				t.getTaskReportList().add(r);
				r.setTask(t);
				r.setDeleted(true);
				em.persist(r);
			}

			t.setProject(p);
			em.persist(t);

		}

		em.persist(p);

		utx.commit();
		assertNotNull(p.getId());
		assertEquals((Object) 100.0, reportDao.getTotalHoursForProject(p.getId()));

	}
}