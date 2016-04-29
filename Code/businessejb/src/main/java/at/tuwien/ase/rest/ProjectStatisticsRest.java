package at.tuwien.ase.rest;

import at.tuwien.ase.dao.ProjectDaoInterface;
import at.tuwien.ase.dao.ProjectStatisticsDaoInterface;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Statistics;
import at.tuwien.ase.rest.security.SecurityInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@Path("/ProjectStatisticsRest")
@Interceptors ({SecurityInterceptor.class})
public class ProjectStatisticsRest implements ProjectStatisticsRestI {
    @EJB ProjectStatisticsDaoInterface projectStatisticsDao;
    @EJB ProjectDaoInterface projectDao;

    @GET
    @Path("getStatisticsForProject/{id}/{begin}/{end}")
    @Produces("application/json")
    @Override
    @RolesAllowed({"MANAGER"})
    public ProjectStatistics getStatistics(@PathParam("id") long projectId, @PathParam("begin") long begin, @PathParam("end") long end) {
        final int totalHours = projectStatisticsDao.getTotalHours(projectId, new Date(begin), new Date(end));
        final List<Statistics.Item<Integer>> workingObjectHours = projectStatisticsDao.getWorkingObjectHours(projectId, new Date(begin), new Date(end));
        final List<Statistics.Item<Integer>> taskTypeHours = projectStatisticsDao.getTaskTypeHours(projectId, new Date(begin), new Date(end));
        final List<Statistics.DateItem<Integer>> burndown = projectStatisticsDao.getBurndownCurve(projectId, new Date(begin), new Date(end));

        return new ProjectStatistics() {
            @Override public double getTotalHours() {
                return totalHours;
            }

            @Override public Collection<Statistics.Item<Integer>> getWorkingObjectHours() {
                return workingObjectHours;
            }

            @Override public Collection<Statistics.Item<Integer>> getTaskTypeHours() {
                return taskTypeHours;
            }

            @Override public Collection<Statistics.DateItem<Integer>> getBurndownCurve() {
                return burndown;
            }
        };
    }

    @GET
    @Path("getProjectData/{id}/{begin}/{end}")
    @Produces("application/json")
    @Override
    @RolesAllowed({"MANAGER"})
    public ProjectData getData(@PathParam("id") long projectId, @PathParam("begin") long begin, @PathParam("end") long end) {
        final Collection<Statistics.UserTaskType> workerHours = projectStatisticsDao.getWorkerHours(projectId, new Date(begin), new Date(end));
        final List<Statistics.ResourceSummary> resourceUsages = projectStatisticsDao.getResourceUsages(projectId, new Date(begin), new Date(end));

        return new ProjectData() {
            @Override public Collection<Statistics.UserTaskType> getWorkerHours() {
                return workerHours;
            }

            @Override public Collection<Statistics.ResourceSummary> getResourceUsages() {
                return resourceUsages;
            }
        };
    }
}
