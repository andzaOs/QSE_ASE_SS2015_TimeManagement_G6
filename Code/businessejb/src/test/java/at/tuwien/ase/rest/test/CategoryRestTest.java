package at.tuwien.ase.rest.test;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tuwien.ase.dao.CategoryDaoInterface;
import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.UserType;

import com.jayway.restassured.specification.ResponseSpecification;

@RunWith(Arquillian.class)
public class CategoryRestTest extends TestSuiteRest {


    private static final String NAME = "DESC";

    @EJB
    CategoryDaoInterface categoryObjectDao;


    @Test
    public void getItemAccessTest() throws Exception {

        testRolesAuth("http://localhost:8080/web/rest/CategoryRest/getItem/1", UserType.SUPERVISOR, UserType.MANAGER);
    }


    @Test
    public void getListAllAccessTest() throws Exception {

        testRolesAuth("http://localhost:8080/web/rest/CategoryRest/listAll/", UserType.SUPERVISOR, UserType.MANAGER);
    }

    @Test
    public void getPersistAccessTest() throws Exception {

        testRolesAuthPost("http://localhost:8080/web/rest/CategoryRest/persist/", new Category(), UserType.MANAGER, UserType.SUPERVISOR);
    }


    @Test
    public void testListAll() throws Exception {

        utx.begin();
        em.joinTransaction();

        assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());
        int size = 10;
        Integer[] idArray = new Integer[size];
        for (int i = 0; i < size; i++) {

            // create
            Category c = new Category();
            c.setName(NAME);

            // persist

            em.persist(c);

            assertNotNull(c.getId());

            idArray[i] = c.getId().intValue();
        }
        utx.commit();

        ResponseSpecification r = expect().statusCode(200);

        for (int i = 0; i < size; i++) {

            r.body("[" + i + "].id", isOneOf(idArray))

                .body("[" + i + "].name", equalTo(NAME));


        }

        r.given().cookie(getManagerCookie()).when().get("http://localhost:8080/web/rest/CategoryRest/listAll/");
    }

    @Test
    public void testGetItem() throws Exception {

        utx.begin();
        em.joinTransaction();

        assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());

        // create
        Category c = new Category();
        c.setName(NAME);


        // persist

        em.persist(c);

        assertNotNull(c.getId());

        utx.commit();

        expect().statusCode(200).body("id", equalTo(c.getId().intValue()))

            .body("name", equalTo(NAME))
            .given().cookie(getManagerCookie()).when()
            .when()
            .get("http://localhost:8080/web/rest/CategoryRest/getItem/" + c.getId());
    }


    @Test
    public void testDel() throws Exception {

        utx.begin();
        em.joinTransaction();

        assertEquals("database setup error-> database not empty", 0, categoryObjectDao.listAll().size());

        // create
        Category c = new Category();
        c.setName(NAME);


        // persist

        em.persist(c);

        assertNotNull(c.getId());

        utx.commit();
        expect().statusCode(204)
            .given().cookie(getManagerCookie()).when()
            .when()
            .delete("http://localhost:8080/web/rest/CategoryRest/delete/" + c.getId());
        expect().statusCode(204)
            .given().cookie(getManagerCookie()).when()
            .when()
            .get("http://localhost:8080/web/rest/CategoryRest/getItem/" + c.getId());
    }
}