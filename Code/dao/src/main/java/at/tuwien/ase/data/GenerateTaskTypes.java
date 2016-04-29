package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.Resource;
import at.tuwien.ase.model.TaskType;

public class GenerateTaskTypes {

	private static List<TaskType> taskTypes = new ArrayList<TaskType>();
	
	@SuppressWarnings("serial")
	public static void createTaskTypes() {
		
		TaskType tt_0 = new TaskType();
		tt_0.setName("Baustelleneinrichtung");
		tt_0.setDescription("Baustelleneinrichtung");
		tt_0.setTaskNumber("01");
		tt_0.setExpectedWorkHours(new Double(16091));
		assignResource(tt_0, new ArrayList<String>(){{add("Material BE"); add("Elektro Material");}});
		
		TaskType tt_01 = new TaskType();
		tt_01.setName("BE einrichten");
		tt_01.setDescription("BE einrichten");
		tt_01.setTaskNumber("011");
		tt_01.setExpectedWorkHours(new Double(8435));
		assignResource(tt_01, new ArrayList<String>(){{add("Material BE"); add("Elektro Material");}});
		
		TaskType tt_02 = new TaskType();
		tt_02.setName("BE vorhalten");
		tt_02.setDescription("BE vorhalten");
		tt_02.setTaskNumber("012");
		tt_02.setExpectedWorkHours(new Double(5023));
		//assignResource(tt_02, new ArrayList<String>(){{add("Material BE"); }});
		
		TaskType tt_1 = new TaskType();
		tt_1.setName("AB, HGT & BTS");
		tt_1.setDescription("AB, HGT & BTS");
		tt_1.setTaskNumber("11");
		tt_1.setExpectedWorkHours(new Double(13857.79));
		assignResource(tt_1, new ArrayList<String>(){{add("Material BE"); add("Bagger");}});
		
		TaskType tt_11 = new TaskType();
		tt_11.setName("Aufbeton auf Brücken");
		tt_11.setDescription("Aufbeton auf Brücken");
		tt_11.setTaskNumber("111");
		tt_11.setExpectedWorkHours(new Double(807));
		assignResource(tt_11, new ArrayList<String>(){{add("Beton"); add("Bagger");}});
		
		TaskType tt_12 = new TaskType();
		tt_12.setName("HGT im Tunnel");
		tt_12.setDescription("HGT im Tunnel");
		tt_12.setTaskNumber("112");
		tt_12.setExpectedWorkHours(new Double(5812));
		assignResource(tt_12, new ArrayList<String>(){{add("Unimog"); add("Bagger");}});
		
		TaskType tt_13 = new TaskType();
		tt_13.setName("HGT - Freie Strecke");
		tt_13.setDescription("HGT - Freie Strecke");
		tt_13.setTaskNumber("113");
		tt_13.setExpectedWorkHours(new Double(1999));
		assignResource(tt_13, new ArrayList<String>(){{add("Beton"); add("Bagger");}});
		
		TaskType tt_14 = new TaskType();
		tt_14.setName("BTS - Freie Strecke");
		tt_14.setDescription("BTS - Freie Strecke");
		tt_14.setTaskNumber("114");
		tt_14.setExpectedWorkHours(new Double(5238));
		assignResource(tt_14, new ArrayList<String>(){{add("Beton"); add("Bagger");}});
		
		TaskType tt_21 = new TaskType();
		tt_21.setName("Feste Fahrbahn GTP aüf Brücken");
		tt_21.setDescription("Feste Fahrbahn GTP aüf Brücken");
		tt_21.setTaskNumber("21");
		tt_21.setExpectedWorkHours(new Double(25903.66));
		assignResource(tt_21, new ArrayList<String>(){{add("Schienen");}});
		
		TaskType tt_22 = new TaskType();
		tt_22.setName("Feste Fahrbahn GTP im Tunnel");
		tt_22.setDescription("Feste Fahrbahn GTP im Tunnel");
		tt_22.setTaskNumber("22");
		tt_22.setExpectedWorkHours(new Double(74141.77));
		assignResource(tt_22, new ArrayList<String>(){{add("Schienen");}});
		
		TaskType tt_2201 = new TaskType();
		tt_2201.setName("Oberflächenvorbereitung");
		tt_2201.setDescription("Oberflächenvorbereitung");
		tt_2201.setTaskNumber("2201");
		tt_2201.setExpectedWorkHours(new Double(3357.13));
		assignResource(tt_2201, new ArrayList<String>(){{add("Reinigunsmittel");}});
		
		TaskType tt_2202 = new TaskType();
		tt_2202.setName("Bewehrung");
		tt_2202.setDescription("Bewehrung");
		tt_2202.setTaskNumber("2202");
		tt_2202.setExpectedWorkHours(new Double(7985.95));
		assignResource(tt_2202, new ArrayList<String>(){{add("Stahl");}});
		
		TaskType tt_23 = new TaskType();
		tt_23.setName("Feste Fahrbahn GTP - Freie Strecke");
		tt_23.setDescription("Feste Fahrbahn GTP - Freie Strecke");
		tt_23.setTaskNumber("23");
		tt_23.setExpectedWorkHours(new Double(29385.72));
		assignResource(tt_23, new ArrayList<String>(){{add("Schienen");}});
		
		taskTypes.add(tt_0);
		taskTypes.add(tt_01);
		taskTypes.add(tt_02);
		taskTypes.add(tt_1);
		taskTypes.add(tt_11);
		taskTypes.add(tt_12);
		taskTypes.add(tt_13);
		taskTypes.add(tt_14);
		taskTypes.add(tt_21);
		taskTypes.add(tt_22);
		taskTypes.add(tt_2201);
		taskTypes.add(tt_2202);
		taskTypes.add(tt_23);
	}
	
	
	public static List<TaskType> getTaskTypes() {
		return taskTypes;
	}

	private static void assignResource(TaskType tt, List<String> resourceName) {
		
		List<Resource> rs = new ArrayList<Resource>();
		
		for (Resource r : GenerateResources.getResources()) {
			
			if (resourceName.contains(r.getDescription()))
				rs.add(r);
		}
		
		tt.setResourceList(rs);
	}
}
