package at.tuwien.ase.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import at.tuwien.ase.model.Statistics;

public interface ProjectStatisticsDaoInterface {
    
	/**
     * Gets the total hours spent on the project in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return total hours
     */
    int getTotalHours(long projectId, Date begin, Date end);

    /**
     * Gets total hours spent per working object in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return working object statistics, not null
     */
    List<Statistics.Item<Integer>> getWorkingObjectHours(long projectId, Date begin, Date end);

    /**
     * Gets total hours spent per task type in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return working object statistics, not null
     */
    List<Statistics.Item<Integer>> getTaskTypeHours(long projectId, Date begin, Date end);

    /**
     * Get resource usages for a given project in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return working object statistics, not null
     */
    List<Statistics.ResourceSummary> getResourceUsages(long projectId, Date begin, Date end);

    /**
     * Get burndown curve data for a given project in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return working object statistics, not null
     */
    List<Statistics.DateItem<Integer>> getBurndownCurve(long projectId, Date begin, Date end);

    /**
     * Get worker hours per task type for a given project in the given time frame.
     * Precondition: begin < end
     *
     * @param projectId project id the project id
     * @param begin begin of the timeframe
     * @param end end of the timeframe
     *
     * @return working object statistics, not null
     */
    Collection<Statistics.UserTaskType> getWorkerHours(long projectId, Date begin, Date end);
}
