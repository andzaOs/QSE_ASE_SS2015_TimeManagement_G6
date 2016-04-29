package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.Company;

public class GenerateCompanies {

	private static List<Company> companies = new ArrayList<Company>();
	
	public static void createCompanies() {
		
		Company c_1 = new Company();
		c_1.setName("PORR");
		
		Company c_2 = new Company();
		c_2.setName("Kirsch");
		
		Company c_3 = new Company();
		c_3.setName("KWErdbau");
		
		Company c_4 = new Company();
		c_4.setName("Nivelatherm");
		
		Company c_5 = new Company();
		c_5.setName("Sauerbrey");
		
		companies.add(c_1);
		companies.add(c_2);
		companies.add(c_3);
		companies.add(c_4);
		companies.add(c_5);
	}
	
	public static List<Company> getCompanies() {
		
		return companies;
	}
}
