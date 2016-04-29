package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.Category;

public class GenerateCategories {

	private static List<Category> categories = new ArrayList<Category>();
	
	public static void createCategories() {
		
		Category c_1 = new Category();
		c_1.setName("Material");
		
		Category c_2 = new Category();
		c_2.setName("Geräte");
		
		categories.add(c_1);
		categories.add(c_2);
	}

	public static List<Category> getCategories() {
		return categories;
	}
}
