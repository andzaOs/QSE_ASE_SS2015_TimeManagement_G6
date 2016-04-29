package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.ProjectType;
import at.tuwien.ase.model.WorkingObject;

public class GenerateWorkingObjects {

	private static List<WorkingObject> workingObjects = new ArrayList<WorkingObject>();
	
	public static void createWorkingObjects() {
		
		WorkingObject wo_1 = new WorkingObject();
		wo_1.setDescription("Boden");
		//assignProjectType(wo_1, new ArrayList<String>(){{add("Gebäude"); add("Brücke");}});
		
		WorkingObject wo_2 = new WorkingObject();
		wo_2.setDescription("Wand");
		//assignProjectType(wo_1, new ArrayList<String>(){{add("Gebäude"); add("Bahn");}});
		
		WorkingObject wo_3 = new WorkingObject();
		wo_3.setDescription("Gleis");
		//assignProjectType(wo_1, new ArrayList<String>(){{add("Bahn"); }});
		
		WorkingObject wo_4 = new WorkingObject();
		wo_4.setDescription("Decke");
		//assignProjectType(wo_1, new ArrayList<String>(){{add("Gebäude"); add("Bahn");}});
		
		WorkingObject wo_5 = new WorkingObject();
		wo_5.setDescription("Fundament");
		//assignProjectType(wo_1, new ArrayList<String>(){{add("Gebäude"); add("Brücke"); add("Bahn");}});
		
		workingObjects.add(wo_1);
		workingObjects.add(wo_2);
		workingObjects.add(wo_3);
		workingObjects.add(wo_4);
		workingObjects.add(wo_5);
	}
	
	
	public static List<WorkingObject> getWorkingObjects() {
		return workingObjects;
	}

	@SuppressWarnings("unused")
	private static void assignProjectType(WorkingObject wo, List<String> projectTypeName) {
		
		List<ProjectType> pts = new ArrayList<ProjectType>();
		
		for (ProjectType pt : GenerateProjectTypes.getProjectTypes()) {
			
			if (projectTypeName.contains(pt.getDescription())) {
				pts.add(pt);
			}
		}
		
		wo.setProjectTypeList(pts);
	}
}
