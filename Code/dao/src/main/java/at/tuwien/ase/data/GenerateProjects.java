package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.User;

public class GenerateProjects {

	private static List<Project> projects = new ArrayList<Project>();
	
	@SuppressWarnings("serial")
	public static void createProjects() {
		
		Project p_1 = new Project();
		p_1.setName("Building a bridge");
		p_1.setExpectedWorkHours(new Double(500000));
		p_1.setWorkHours(new Double(5));
		assignProjectType(p_1, "Brücke");
		assignTaskTypes(p_1, new ArrayList<String>(){{add("01"); add("011"); add("012"); add("11"); add("21"); add("2201");}});
		assignUsers(p_1, new ArrayList<String>(){{add("soDominic"); add("maRinner"); add("piSloka"); add("emBaunnack"); add("chZupfer"); add("maMuster");}});
		
		projects.add(p_1);
	}
	
	private static void assignTaskTypes(Project p, List<String> taskNumbers) {
		
		Set<TaskType> tts = new HashSet<TaskType>();
		
		for (TaskType tt : GenerateTaskTypes.getTaskTypes()) {
			
			if (taskNumbers.contains(tt.getTaskNumber()))
				tts.add(tt);
		}
		
		p.setTaskTypeList(tts);
	}
	
	private static void assignUsers(Project p, List<String> users) {
		
		Set<User> usr = new HashSet<User>();
		
		for (User u : GenerateUsers.getWorkers()) {
			
			if (users.contains(u.getUsername()))
				usr.add(u);
		}
		
		for (User u : GenerateUsers.getSupervisors()) {
			
			if (users.contains(u.getUsername()))
				usr.add(u);
		}
		
		for (User u : GenerateUsers.getManagers()) {
			
			if (users.contains(u.getUsername()))
				usr.add(u);
		}
		
		p.setUserList(usr);
	}
	
	private static void assignProjectType(Project p, String projectTypeName) {
		
		for (ProjectType pt : GenerateProjectTypes.getProjectTypes()) {
			
			if (pt.getDescription().equals(projectTypeName)) {
				p.setProjectType(pt);
				break;
			}
		}
	}

	public static List<Project> getProjects() {
		return projects;
	}
}