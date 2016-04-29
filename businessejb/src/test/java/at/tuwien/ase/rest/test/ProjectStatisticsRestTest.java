package at.tuwien.ase.rest.test;

import at.tuwien.ase.model.*;
import com.jayway.restassured.specification.ResponseSpecification;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

@RunWith(Arquillian.class)
public class ProjectStatisticsRestTest extends TestSuiteRest {
    private Project project;

    private static Date createDate(int day) {
        Calendar calendar = new GregorianCalendar(2015, Calendar.JANUARY, day);
        return calendar.getTime();
    }

    @Before
    public void setUp() throws Exception {
        utx.begin();
        em.joinTransaction();

        project = new Project();
        project.setName("Test");
        project.setBegin(createDate(1));
        project.setEnd(createDate(30));
        em.persist(project);

        WorkingObject room1 = new WorkingObject();
        room1.setDescription("Room 1");
        room1.setWoNumber("A123");
        em.persist(room1);

        WorkingObject room2 = new WorkingObject();
        room2.setDescription("Room 2");
        room2.setWoNumber("A123");
        em.persist(room2);

        Company company = new Company();
        company.setName("Company");
        em.persist(company);

        User worker1 = new User();
        worker1.setCompany(company);
        worker1.setLastname("Kurtl");
        em.persist(worker1);

        Task task1 = new Task();
        task1.setProject(project);
        task1.setWorkingObject(room1);
        task1.setWorker(worker1);
        em.persist(task1);

        TaskReport task1Report = new TaskReport();
        task1Report.setDescription("Work done");
        task1Report.setBegin(createDate(2));
        task1Report.setEnd(createDate(3));
        task1Report.setTask(task1);
        em.persist(task1Report);

        Task task2 = new Task();
        task2.setProject(project);
        task2.setWorkingObject(room2);
        task2.setWorker(worker1);
        em.persist(task2);

        TaskReport task2Report = new TaskReport();
        task2Report.setDescription("Work done");
        task2Report.setBegin(createDate(5));
        task2Report.setEnd(createDate(8));
        task2Report.setTask(task2);
        em.persist(task2Report);

        utx.commit();
    }

    @Test
    public void testGetProjectData() throws Exception {
        given().cookie(getManagerCookie()).
        expect().statusCode(200)

            .when()
            .get("http://localhost:8080/web/rest/ProjectStatisticsRest/getProjectData/" + project.getId() + "/0/20000000000000");
    }

    @Test
    public void testGetStatistics() throws Exception {
        given().cookie(getManagerCookie()).
        expect().statusCode(200)
            .when()
            .get("http://localhost:8080/web/rest/ProjectStatisticsRest/getStatisticsForProject/" + project.getId() + "/0/20000000000000");
    }
}


