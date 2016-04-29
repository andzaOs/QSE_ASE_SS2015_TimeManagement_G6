package at.tuwien.ase.test;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquilianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Rule;

@ArquilianSuiteDeployment
public class TestSuiteDao {
	@Rule public LogTestName logTestName = new LogTestName();
	@PersistenceContext(name = "AseDataSource")
	 EntityManager em;
	@Inject
	UserTransaction utx;
	
	@Deployment
	public static EnterpriseArchive createDeployment() {
		EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("build/ASE.ear"));
		WebArchive war = ear.getAsType(WebArchive.class, "/web.war");
		
		war.addPackage(TestSuiteDao.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	
		return ear;
	}
	
	@Before
	public void prepareDatabese() throws Exception {
		utx.begin();
		em.joinTransaction();
		try {

			//System.out.println("Dumping old records before tests...");
			// Disable referential integrity using SET REFERENTIAL_INTEGRITY
			// FALSE
			String s = "SET REFERENTIAL_INTEGRITY FALSE;";

			// Get the list of all tables using SHOW TABLES
			Query q = em.createNativeQuery("SELECT    TABLE_NAME   FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA  ='PUBLIC'");

			// Delete the data from each table using TRUNCATE TABLE tableName
			List<String> res = q.getResultList();
			for (String t : res) {
				s += "TRUNCATE TABLE " + t + ";";

			}
			em.createNativeQuery(s).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			// Enable referential integrity using
			em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE"); // lets do
																	// this
																	// allways
			utx.commit();
		}
	}

}
