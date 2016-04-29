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

import at.tuwien.ase.dao.ResourceUsageDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.User;

@RunWith(Arquillian.class)
public class ResourceUsageDaoTest extends TestSuiteDao {
	private static final String DESC = "DESC";

	@EJB
	ResourceUsageDaoInterface resourceUsageDao;
	@Test
	public void testDeleted() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceUsageDao.listAll().size());

		List<ResourceUsage> list = new ArrayList<ResourceUsage>();
		int size = 10;
		Date begin = new Date();
		Double cost = 10.0;
		Date end = new Date();
		Double quantity = 50.0;
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			Resource resource = new Resource();
			;
			em.persist(resource);
			TaskReport taskReport = new TaskReport();
			em.persist(taskReport);
			// create
			ResourceUsage r = new ResourceUsage();
			
			r.setBegin(begin);

			r.setCost(cost);

			r.setEnd(end);

			r.setQuantity(quantity);

			r.setResource(resource);

			r.setTaskReport(taskReport);

			// persist

			em.persist(r);
			list.add(r);
		}
		utx.commit();
		List<ResourceUsage> rList = resourceUsageDao.listAll();

		assertEquals(size, rList.size());
		
		for (int i = 0; i < size; i++) {
			resourceUsageDao.delete(list.get(i).getId());
			assertEquals(size-i-1, resourceUsageDao.listAll().size());
		}

	}
	@Test
	public void testListAll() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceUsageDao.listAll().size());

		List<ResourceUsage> list = new ArrayList<ResourceUsage>();
		int size = 10;
		Date begin = new Date();
		Double cost = 10.0;
		Date end = new Date();
		Double quantity = 50.0;
		for (int i = 0; i < size; i++) {
			// one2one refs (lazy)
			Resource resource = new Resource();
			;
			em.persist(resource);
			TaskReport taskReport = new TaskReport();
			em.persist(taskReport);
			// create
			ResourceUsage r = new ResourceUsage();
			
			r.setBegin(begin);

			r.setCost(cost);

			r.setEnd(end);

			r.setQuantity(quantity);

			r.setResource(resource);

			r.setTaskReport(taskReport);

			// persist

			em.persist(r);
			list.add(r);
		}
		utx.commit();

		List<ResourceUsage> rList = resourceUsageDao.listAll();

		assertEquals(size, rList.size());
		for (int i = 0; i < size; i++) {
			boolean contains = false;
			for (ResourceUsage rd : rList) {
				if (rd.getId().equals(list.get(i).getId())) {
				
					assertNotNull(rd.getResource().getId());
					assertNotNull(rd.getTaskReport().getId());

					assertEquals(begin, rd.getBegin());
					assertEquals(end, rd.getEnd());

					assertEquals(cost, rd.getCost());
					assertEquals(quantity, rd.getQuantity());
					contains=true;
					break;
				}
			}
			if (!contains) {
				fail("returned list does not cointain all items");
			}

		}
	}
	
	
	@Test
	public void getItem() throws Exception {

		utx.begin();
		em.joinTransaction();

		assertEquals("database setup error-> database not empty", 0, resourceUsageDao.listAll().size());

		List<ResourceUsage> list = new ArrayList<ResourceUsage>();
		int size = 10;
		Date begin = new Date();
		Double cost = 10.0;
		Date end = new Date();
		Double quantity = 50.0;
		
			// one2one refs (lazy)
			Resource resource = new Resource();
			;
			em.persist(resource);
			TaskReport taskReport = new TaskReport();
			em.persist(taskReport);
			// create
			ResourceUsage r = new ResourceUsage();
			
			r.setBegin(begin);

			r.setCost(cost);

			r.setEnd(end);

			r.setQuantity(quantity);

			r.setResource(resource);

			r.setTaskReport(taskReport);

			// persist

			em.persist(r);
			list.add(r);
		
		utx.commit();

	
		assertNotNull( r.getId());
		ResourceUsage rd = resourceUsageDao.get(r.getId());
		assertNotNull(rd.getResource().getId());
		assertNotNull(rd.getTaskReport().getId());

		assertEquals(begin, rd.getBegin());
		assertEquals(end, rd.getEnd());

		assertEquals(cost, rd.getCost());
		assertEquals(quantity, rd.getQuantity());
	}
}