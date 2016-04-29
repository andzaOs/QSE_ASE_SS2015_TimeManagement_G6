package at.tuwien.ase.rest;

import at.tuwien.ase.model.Statistics;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProjectStatisticsRestI {
    interface ProjectStatistics {
        /**
         * @return the total hours of the project
         */
        double getTotalHours();

        /**
         * @return not null, working object item list
         */
        Collection<Statistics.Item<Integer>> getWorkingObjectHours();

        /**
         * @return not null, task type hour list
         */
        Collection<Statistics.Item<Integer>> getTaskTypeHours();

        /**
         * @return not null, burndown curve data
         */
        Collection<Statistics.DateItem<Integer>> getBurndownCurve();
    }

    interface ProjectData {
        /**
         * @return not null, workers with aggregated task type hours
         */
        Collection<Statistics.UserTaskType> getWorkerHours();

        /**
         * @return not null, total resource usages
         */
        Collection<Statistics.ResourceSummary> getResourceUsages();
    }

    /**
     * Gets statistics for a given project.
     * Precondition: begin < end
     *
     * @param projectId project id
     * @param begin     begin
     * @param end       end
     * @return statistics not null
     */
    ProjectStatistics getStatistics(long projectId, long begin, long end);

    /**
     * Gets data for a given project.
     * Including worker hours and resource usages.
     * Precondition: begin < end
     *
     * @param projectId project id
     * @param begin     begin
     * @param end       end
     * @return statistics not null
     */
    ProjectData getData(long projectId, long begin, long end);

}
