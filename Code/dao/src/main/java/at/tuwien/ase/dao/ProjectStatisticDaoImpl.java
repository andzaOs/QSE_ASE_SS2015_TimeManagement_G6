package at.tuwien.ase.dao;

import at.tuwien.ase.model.Statistics;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.BasicTransformerAdapter;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import java.util.*;

@SuppressWarnings("unchecked")
@Stateless
public class ProjectStatisticDaoImpl implements ProjectStatisticsDaoInterface {
    private final BasicTransformerAdapter itemTransformer = new BasicTransformerAdapter() {
        @Override public Object transformTuple(final Object[] tuple, String[] aliases) {
            return new Statistics.Item<Integer>(((Number) tuple[1]).intValue(), (String) tuple[0]);
        }
    };

    private final BasicTransformerAdapter dateTransformer = new BasicTransformerAdapter() {
        @Override public Object transformTuple(final Object[] tuple, String[] aliases) {
            return new Statistics.DateItem<Integer>(((Number) tuple[1]).intValue(), (Date) tuple[0]);
        }
    };

    @PersistenceContext private Session session;

    @Override public int getTotalHours(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT SUM(DATEDIFF(\"HOUR\", TASKREPORT.begin, TASKREPORT.end))" +
                " FROM TASKREPORT" +
                "  JOIN task ON TASK_ID = TASK.ID" +
                " WHERE PROJECT_ID = ?" +
                " AND END > ? and END < ?"
        );

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);

        Object result = query.uniqueResult();
        return result == null ? 0 : ((Number) result).intValue();
    }

    @Override public List<Statistics.Item<Integer>> getWorkingObjectHours(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT  WORKINGOBJECT.DESCRIPTION," +
                "  SUM(DATEDIFF(\"HOUR\", TASKREPORT.begin, TASKREPORT.end))" +
                " FROM WORKINGOBJECT\n" +
                "  JOIN TASK ON TASK.WORKINGOBJECT_ID = WORKINGOBJECT.ID" +
                "  JOIN TASKREPORT ON TASKREPORT.TASK_ID = task.id" +
                "  WHERE PROJECT_ID = ?" +
                " AND END > ? and END < ?" +
                " GROUP BY WORKINGOBJECT.ID");

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);

        query.setResultTransformer(itemTransformer);

        return Collections.<Statistics.Item<Integer>>unmodifiableList(query.list());
    }

    @Override public List<Statistics.Item<Integer>> getTaskTypeHours(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT  TASKTYPE.NAME," +
                "  SUM(DATEDIFF(\"HOUR\", TASKREPORT.begin, TASKREPORT.end))" +
                " FROM TASKTYPE" +
                "  JOIN TASK ON TASK.TASKTYPE_ID = TASKTYPE.ID" +
                "  JOIN TASKREPORT ON TASK_ID = TASK.ID" +
                "  WHERE PROJECT_ID = ?" +
                " AND END > ? and END < ?" +
                " GROUP BY TASKTYPE.ID");

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setResultTransformer(itemTransformer);

        return Collections.<Statistics.Item<Integer>>unmodifiableList(query.list());
    }

    @Override public List<Statistics.ResourceSummary> getResourceUsages(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT\n" +
                "  resource.description,\n" +
                "  sum(DATEDIFF('MINUTE', RESOURCEUSAGE.begin, RESOURCEUSAGE.end)),\n" +
                "  sum(quantity)\n" +
                "FROM RESOURCEUSAGE\n" +
                "  JOIN resource ON resource.id = RESOURCEUSAGE.resource_id\n" +
                "  JOIN taskreport ON taskreport_id = taskreport.id\n" +
                "  JOIN task ON taskreport.task_id = task.id\n" +
                "WHERE project_id = ?\n" +
                " AND taskreport.END > ? and taskreport.END < ?" +
                " GROUP BY resource.id, resource.description");

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setResultTransformer(new BasicTransformerAdapter() {
            @Override public Object transformTuple(Object[] tuple, String[] aliases) {
                return new Statistics.ResourceSummary(tuple[0] == null ? null : (String) tuple[0], tuple[1] == null ? 0 : ((Number) tuple[1]).intValue(), tuple[2] == null ? 0 : ((Number) tuple[2]).intValue());
            }
        });

        return Collections.<Statistics.Item<Integer>>unmodifiableList(query.list());
    }

    @Override public List<Statistics.DateItem<Integer>> getBurndownCurve(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT END, DATEDIFF('MINUTE', BEGIN, END) " +
                "FROM taskreport " +
                "  JOIN task ON taskreport.task_id = task.id " +
                "WHERE task.project_id = ?" +
                " AND END > ? and END < ?" +
                "ORDER BY end");

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);
        query.setResultTransformer(dateTransformer);

        List<Statistics.DateItem<Integer>> list = query.list();
        List<Statistics.DateItem<Integer>> cumulative = new ArrayList<Statistics.DateItem<Integer>>(list.size());

        // calculate cumulative list
        int sum = 0;
        for (Statistics.DateItem<Integer> item : list) {
            sum += item.getValue();
            cumulative.add(new Statistics.DateItem<Integer>(sum, item.getDate()));
        }

        return Collections.unmodifiableList(cumulative);
    }

    @Override
    public Collection<Statistics.UserTaskType> getWorkerHours(long projectId, Date begin, Date end) {
        SQLQuery query = session.createSQLQuery(
            "SELECT\n" +
                "  user.id                                                   user_id,\n" +
                "  company.name                                              company,\n" +
                "  concat(user.forname, ' ', user.lastname)                  name,\n" +
                "  tasktype.id                                               tasktype_id,\n" +
                "  tasktype.name                                             taskype_name,\n" +
                "  sum(DATEDIFF('minute', taskreport.begin, taskreport.end)) total_minutes\n" +
                "FROM taskreport\n" +
                "  JOIN task ON task.id = taskreport.task_id\n" +
                "  JOIN user ON task.worker_id = user.id\n" +
                "  JOIN project_user ON user.id= project_user.userlist_id\n" +
                "  JOIN company ON user.company_id = company.id\n" +
                "  JOIN tasktype ON task.tasktype_id = tasktype.id\n" +
                "WHERE project_user.project_id = ?\n" +
                "  AND END > ? and END < ?" +
                " GROUP BY user_id, tasktype.id\n");

        query.setParameter(0, projectId);
        query.setParameter(1, begin);
        query.setParameter(2, end);

        final Map<Integer, Statistics.UserTaskType> result = new HashMap<Integer, Statistics.UserTaskType>();
        query.setResultTransformer(new BasicTransformerAdapter() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                int id = ((Number) tuple[0]).intValue();
                Statistics.UserTaskType user = result.get(id);
                if (user == null) {
                    user = new Statistics.UserTaskType(id, (String) tuple[2], (String) tuple[1], new ArrayList<Statistics.TaskType>());
                    result.put(user.getId(), user);
                }

                Statistics.TaskType taskType = new Statistics.TaskType(((Number) tuple[3]).intValue(), (String) tuple[4], ((Number) tuple[5]).intValue());
                user.getTaskTypes().add(taskType);
                return null;
            }
        });

        query.list();

        return result.values();
    }
}
