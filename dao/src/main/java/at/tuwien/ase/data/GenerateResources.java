package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.Category;
import at.tuwien.ase.model.Resource;

public class GenerateResources {

	private static List<Resource> resources = new ArrayList<Resource>();
	
	public static void createResources() {
		
		Resource r_1 = new Resource();
		r_1.setDescription("Material BE");
		assignCategory(r_1, "Material");
		
		Resource r_2 = new Resource();
		r_2.setDescription("Farben");
		assignCategory(r_2, "Material");
		
		Resource r_3 = new Resource();
		r_3.setDescription("Elektro Material");
		assignCategory(r_3, "Material");
		
		Resource r_4 = new Resource();
		r_4.setDescription("Unimog");
		assignCategory(r_4, "Geräte");
		
		Resource r_5 = new Resource();
		r_5.setDescription("Bagger");
		assignCategory(r_5, "Geräte");
		
		Resource r_6 = new Resource();
		r_6.setDescription("Beton");
		assignCategory(r_6, "Material");
		
		Resource r_7 = new Resource();
		r_7.setDescription("Schienen");
		assignCategory(r_7, "Material");
		
		Resource r_8 = new Resource();
		r_8.setDescription("Stahl");
		assignCategory(r_8, "Material");
		
		Resource r_9 = new Resource();
		r_9.setDescription("Reinigunsmittel");
		assignCategory(r_9, "Material");
		
		resources.add(r_1);
		resources.add(r_2);
		resources.add(r_3);
		resources.add(r_4);
		resources.add(r_5);
		resources.add(r_6);
		resources.add(r_7);
		resources.add(r_8);
		resources.add(r_9);
	}
		
	public static List<Resource> getResources() {
		return resources;
	}

	private static void assignCategory(Resource r, String categoryName) {
		
		for (Category c : GenerateCategories.getCategories()) {
			
			if (c.getName().equals(categoryName)) {
				r.setCategory(c);
				break;
			}
		}
	}
}
