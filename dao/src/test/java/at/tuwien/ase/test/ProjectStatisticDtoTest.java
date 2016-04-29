package at.tuwien.ase.test;

import at.tuwien.ase.dao.ProjectStatisticsDaoInterface;
import at.tuwien.ase.dao.ResourceDaoInterface;
import at.tuwien.ase.model.*;
import com.google.common.collect.Sets;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ProjectStatisticDtoTest extends TestSuiteDao {
    private static final String DESC = "DESC";
    private final double DELTA = .0000001;

    @EJB ProjectStatisticsDaoInterface projectStatisticsDao;
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

        TaskType taskType = new TaskType();
        taskType.setName("CLEAN");
        em.persist(taskType);

        User worker1 = new User();
        worker1.setCompany(company);
        worker1.setForname("Kurt");
        worker1.setLastname("Kurtl");
        em.persist(worker1);
        project.setUserList(Sets.newHashSet(worker1));

        Task task1 = new Task();
        task1.setProject(project);
        task1.setTaskType(taskType);
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
        task2.setTaskType(taskType);
        task2.setProject(project);
        task2.setWorkingObject(room2);
        task2.setWorker(worker1);
        task2.setDescription("Task");
        task2.setRequiresResources(true);
        em.persist(task2);


        Resource resource = new Resource();
        resource.setDescription("Bagger");
        em.persist(resource);

        Resource resource2 = new Resource();
        resource2.setDescription("Beton");
        em.persist(resource2);

        ResourceUsage bagger = new ResourceUsage();
        bagger.setResource(resource);
        bagger.setBegin(createDate(5));
        bagger.setEnd(createDate(6));
        em.persist(bagger);

        ResourceUsage beton = new ResourceUsage();
        beton.setResource(resource2);
        beton.setQuantity(1000.);
        beton.setBegin(createDate(5));
        beton.setEnd(createDate(6));
        em.persist(beton);

        TaskReport task2Report = new TaskReport();
        task2Report.setDescription("Work done");
        task2Report.setBegin(createDate(5));
        task2Report.setEnd(createDate(8));
        task2Report.setTask(task2);
        task2Report.setResourceUsageList(Arrays.asList(bagger, beton));
        em.persist(task2Report);

        beton.setTaskReport(task2Report);
        bagger.setTaskReport(task2Report);
        task2.setTaskReportList(Arrays.asList(task2Report));
        resource.setResourceUsageList(Arrays.asList(bagger));
        resource2.setResourceUsageList(Arrays.asList(beton));

        utx.commit();
    }

    @Test
    public void testTotalHours() throws Exception {
        assertEquals(24 * 4, projectStatisticsDao.getTotalHours(project.getId(), createDate(1), createDate(30)));
    }

    @Test
    public void testTotalHoursInRange() throws Exception {
        assertEquals(24, projectStatisticsDao.getTotalHours(project.getId(), createDate(1), createDate(4)));
    }

    @Test
    public void testBurndownCurve() throws Exception {
        List<Statistics.DateItem<Integer>> burndownCurve = projectStatisticsDao.getBurndownCurve(project.getId(), createDate(1), createDate(30));
        assertEquals(2, burndownCurve.size());

        assertEquals(60 * 24, (int) burndownCurve.get(0).getValue());
        assertEquals(createDate(3), burndownCurve.get(0).getDate());

        assertEquals(60 * 24 * 4, (int) burndownCurve.get(1).getValue());
        assertEquals(createDate(8), burndownCurve.get(1).getDate());
    }

    @Test
    public void testResourceBaggerUsage() throws Exception {
        List<Statistics.ResourceSummary> usages = projectStatisticsDao.getResourceUsages(project.getId(), createDate(1), createDate(30));

        assertEquals(2, usages.size());
        Statistics.ResourceSummary s1 = usages.get(0);
        Statistics.ResourceSummary s2 = usages.get(1);

        Statistics.ResourceSummary bagger = s1.isMachine() ? s1 : s2;
        assertEquals(24 * 60, bagger.getTotalMinutes());

        Statistics.ResourceSummary beton = s2.isMachine() ? s1 : s2;
        assertEquals(1000., beton.getTotalQuantity(), DELTA);
    }

    @Test
    public void testTaskTypeHours() throws Exception {
        List<Statistics.Item<Integer>> taskTypeHours = projectStatisticsDao.getTaskTypeHours(project.getId(), createDate(1), createDate(30));

        assertEquals(1, taskTypeHours.size());
        assertEquals(96, (int) taskTypeHours.get(0).getValue());
        assertEquals("CLEAN", taskTypeHours.get(0).getName());
    }

    @Test
    public void testWorkerHours() throws Exception {
        List<Statistics.UserTaskType> workerHours = new ArrayList<Statistics.UserTaskType>(projectStatisticsDao.getWorkerHours(project.getId(), createDate(1), createDate(30)));

        assertEquals(1, workerHours.size());
        assertEquals("Kurt Kurtl", workerHours.get(0).getName());
        assertEquals("CLEAN", workerHours.get(0).getTaskTypes().get(0).getName());
        assertEquals(4 * 24 * 60, workerHours.get(0).getTaskTypes().get(0).getTotalMinutes());
    }
}
