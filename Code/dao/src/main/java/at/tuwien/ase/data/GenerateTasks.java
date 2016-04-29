package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;
import at.tuwien.ase.model.WorkingObject;

public class GenerateTasks {
	
	private static List<Task> tasks = new ArrayList<Task>();
	
	public static void createTasks() {
		
		Task t_1 = new Task();
		t_1.setDescription("Do something...");
		t_1.setExpectedWorkHours(new Double(100));
		 
		assignProject(t_1, "Building a bridge");
		assignWorker(t_1, "soDominic");
		assignApprover(t_1, "chZupfer");
		assignTaskTypes(t_1, "01");
		assignWorkingObject(t_1, "Boden");
		
		if (t_1.getTaskType().getResourceList() != null)
			t_1.setRequiresResources(true);
		else
			t_1.setRequiresResources(false);
		
		Task t_2 = new Task();
		
		t_2.setDescription("Do something...");
		t_2.setExpectedWorkHours(new Double(100));
		assignProject(t_2, "Building a bridge");
		assignWorker(t_2, "maRinner");
		assignApprover(t_2, "chZupfer");
		assignTaskTypes(t_2, "012");
		assignWorkingObject(t_2, "Boden");
		
		if (t_2.getTaskType().getResourceList() != null)
			t_2.setRequiresResources(true);
		else
			t_2.setRequiresResources(false);
		
		Task t_3 = new Task();
		
		t_3.setDescription("Do something...");
		t_3.setExpectedWorkHours(new Double(100));
		assignProject(t_3, "Building a bridge");
		assignWorker(t_3, "chZupfer");
		assignApprover(t_3, "maMuster");
		assignTaskTypes(t_3, "11");
		assignWorkingObject(t_3, "Fundament");
		
		if (t_3.getTaskType().getResourceList() != null)
			t_3.setRequiresResources(true);
		else
			t_3.setRequiresResources(false);
		
		tasks.add(t_1);
		tasks.add(t_2);
		tasks.add(t_3);
	}
	
	private static void assignWorkingObject(Task t, String workingObjectName) {
		
		for (WorkingObject wo : t.getProject().getProjectType().getWorkingObjectList()) {
			
			if (wo.getDescription().equals(workingObjectName)) {
				t.setWorkingObject(wo);
				break;
			}
		}
	}
	
	private static void assignTaskTypes(Task t, String taskNumber) {
		
		for (TaskType tt : GenerateTaskTypes.getTaskTypes()) {
			
			if (tt.getTaskNumber().equals(taskNumber))
				t.setTaskType(tt);
		}
	}
	
	private static void assignProject(Task t, String projectName) {
		
		for (Project p : GenerateProjects.getProjects()) {
			
			if (p.getName().equals(projectName)) {
				t.setProject(p);
				break;
			}
		}
	}
	
	private static void assignWorker(Task t, String workerName) {
		
		for (User u : t.getProject().getUserList()) {
			
			if (u.getUsername().equals(workerName) && (u.getUserType().equals(UserType.WORKER)  || u.getUserType().equals(UserType.SUPERVISOR))) {
				t.setWorker(u);
				break;
			}
		}
	}
	
	private static void assignApprover(Task t, String approverName) {
		
		for (User u : t.getProject().getUserList()) {
			
			if (u.getUsername().equals(approverName) && (u.getUserType().equals(UserType.SUPERVISOR) || u.getUserType().equals(UserType.MANAGER))) {
				t.setApprover(u);
				break;
			}
		}
	}

	public static List<Task> getTasks() {
		return tasks;
	}	
}