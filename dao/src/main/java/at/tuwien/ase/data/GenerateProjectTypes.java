package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.WorkingObject;

public class GenerateProjectTypes {

	private static List<ProjectType> projectTypes = new ArrayList<ProjectType>();
	
	@SuppressWarnings("serial")
	public static void createProjectTypes() {
		
		ProjectType pt_1 = new ProjectType();
		pt_1.setDescription("Brücke");
		assignWorkingObject(pt_1, new ArrayList<String>(){{add("Boden"); add("Decke"); add("Fundament");}});
		
		ProjectType pt_2 = new ProjectType();
		pt_2.setDescription("Bahn");
		assignWorkingObject(pt_1, new ArrayList<String>(){{add("Fundament"); add("Decke"); add("Gleis");}});
		
		ProjectType pt_3 = new ProjectType();
		pt_3.setDescription("Gebäude");
		assignWorkingObject(pt_1, new ArrayList<String>(){{add("Boden"); add("Decke"); add("Fundament"); add("Wand");}});
		
		projectTypes.add(pt_1);
		projectTypes.add(pt_2);
		projectTypes.add(pt_3);
	}

	public static List<ProjectType> getProjectTypes() {
		return projectTypes;
	}
	
	private static void assignWorkingObject(ProjectType pt, List<String> workingObjectsName) {
		
		List<WorkingObject> wos = new ArrayList<WorkingObject>();
		
		for (WorkingObject wo : GenerateWorkingObjects.getWorkingObjects()) {
			
			if (workingObjectsName.contains(wo.getDescription())) 
				wos.add(wo);
		}
		
		pt.setWorkingObjectList(wos);
	}
}
