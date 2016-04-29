package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.CategoryDaoInterface;
import at.tuwien.ase.dao.TaskDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.rest.TaskReportRestI;
import at.tuwien.ase.rest.TaskRestI;

import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class ProxyRemoverTest extends TestSuiteRest{
	
	@Test
	public void testAutoFLushBug() throws Exception{
		utx.begin();
		em.joinTransaction();
		User approver=new User();
		em.persist(approver);
		TaskType tt = new TaskType();
		em.persist(tt);
		int rrCount=10;
		tt.setResourceList(new ArrayList<Resource>());
		for (int i = 0; i < rrCount; i++) {
			Resource rr = new Resource();
			em.persist(rr);
			rr.setTaskTypeList(new ArrayList<TaskType>());
			rr.getTaskTypeList().add(tt);
			tt.getResourceList().add(rr);
			
		}
		Task t = new Task();
		t.setApprover(approver);
		em.persist(t);
		t.setTaskType(tt);
		utx.commit();
		
		Query q = em.createNativeQuery("SELECT * FROM TASKTYPE_RESOURCE ");
		assertEquals(rrCount,q.getResultList().size());
		
		utx.begin();
		em.joinTransaction();
		assertNotNull(approver.getId());
		
		given().cookie(getManagerCookie()).contentType("application/json").expect().statusCode(200).when().get("http://localhost:8080/web/rest/TaskRest/listTasksForUserId/"+approver.getId());
		utx.commit();

		
		 q = em.createNativeQuery("SELECT * FROM TASKTYPE_RESOURCE ");
		assertEquals(rrCount,q.getResultList().size());
		
		
		
	}
}