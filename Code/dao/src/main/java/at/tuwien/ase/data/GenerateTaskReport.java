package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.tuwien.ase.model.ResourceUsage;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskReport;
import at.tuwien.ase.model.TaskReportStatus;

public class GenerateTaskReport {

	private static List<TaskReport> reports = new ArrayList<TaskReport>();

	@SuppressWarnings("serial")
	public static void createTaskReports() {
		
		TaskReport tr_1 = new TaskReport();
		tr_1.setStatus(TaskReportStatus.NEW);
		tr_1.setDescription("xyz");
		tr_1.setBegin(Calendar.getInstance().getTime());
		tr_1.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*4));
		
		if (!getAssignedTasksToWorker("soDominic").isEmpty())
			tr_1.setTask(getAssignedTasksToWorker("soDominic").get(0));
		
		if (tr_1.getTask().getRequiresResources()) {
			
			final ResourceUsage ru = new ResourceUsage();
			ru.setBegin(Calendar.getInstance().getTime());
			ru.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*2));
			ru.setQuantity((double) ((ru.getEnd().getTime() - ru.getBegin().getTime())/(1000*60*60)));
			ru.setCost(new Double(125));
			
			ru.setResource(tr_1.getTask().getTaskType().getResourceList().get(0));
			ru.setTaskReport(tr_1);
			tr_1.setResourceUsageList(new ArrayList<ResourceUsage>(){{add(ru); }});
		}
		
		TaskReport tr_2 = new TaskReport();
		tr_2.setStatus(TaskReportStatus.NEW);
		tr_2.setDescription("abc");
		tr_2.setBegin(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*4));
		tr_2.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*6));
		
		if (!getAssignedTasksToWorker("soDominic").isEmpty())
			tr_2.setTask(getAssignedTasksToWorker("soDominic").get(0));
		
		if (tr_2.getTask().getRequiresResources()) {
			
			final ResourceUsage ru = new ResourceUsage();
			ru.setBegin(Calendar.getInstance().getTime());
			ru.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*10));
			ru.setQuantity((double) ((ru.getEnd().getTime() - ru.getBegin().getTime())/(1000*60*60)));
			ru.setCost(new Double(125));
			
			ru.setResource(tr_2.getTask().getTaskType().getResourceList().get(0));
			ru.setTaskReport(tr_2);
			tr_2.setResourceUsageList(new ArrayList<ResourceUsage>(){{add(ru); }});
		}
		
		TaskReport tr_3 = new TaskReport();
		tr_3.setStatus(TaskReportStatus.NEW);
		tr_3.setDescription("def");
		tr_3.setBegin(Calendar.getInstance().getTime());
		tr_3.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*12));
		
		if (!getAssignedTasksToWorker("maRinner").isEmpty())
			tr_3.setTask(getAssignedTasksToWorker("maRinner").get(0));
		
		if (tr_3.getTask().getRequiresResources()) {
			
			final ResourceUsage ru = new ResourceUsage();
			ru.setBegin(Calendar.getInstance().getTime());
			ru.setEnd(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60*2));
			ru.setQuantity((double) ((ru.getEnd().getTime() - ru.getBegin().getTime())/(1000*60*60)));
			ru.setCost(new Double(150));
			
			ru.setResource(tr_3.getTask().getTaskType().getResourceList().get(0));
			ru.setTaskReport(tr_3);
			tr_3.setResourceUsageList(new ArrayList<ResourceUsage>(){{add(ru); }});
		}
		
		reports.add(tr_1);
		reports.add(tr_2);
		reports.add(tr_3);
	}
	
	private static List<Task> getAssignedTasksToWorker(String userName) {
		
		List<Task> assignedTasks = new ArrayList<Task>();
		
		for (Task t : GenerateTasks.getTasks()) {
			
			if (t.getWorker().getUsername().equals(userName))
				assignedTasks.add(t);
		}
		
		return assignedTasks;
	}
	
	public static List<TaskReport> getReports() {
		return reports;
	}	
}