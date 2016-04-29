package at.tuwien.ase.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import at.tuwien.ase.model.Company;


public class GenerateTestData {
	
	private EntityManagerFactory emf;
	private EntityManager em;

	public static void main(String[] args) {
		
		GenerateTestData gtd = new GenerateTestData();
		gtd.init();
		
		//add companies and list them
		gtd.addCompanies();
		gtd.listCompanies();
	}
	
	private void init() {
		
		emf = Persistence.createEntityManagerFactory("AseDataSource");
		em = emf.createEntityManager();
	}
	
	private void addCompanies() {
		
        em.getTransaction().begin();
        
        GenerateCompanies.createCompanies();
        
        for (Company c : GenerateCompanies.getCompanies()) 
        	em.persist(c);
        
        em.getTransaction().commit();
	}
	
	private void listCompanies() {
		
        em.getTransaction().begin();
        
        List<Company> results = em.createQuery("from Company", Company.class ).getResultList();
        
        for (Company c : results) {
              System.out.println(c.getId() + " " + c.getName());
        }
        
        em.getTransaction().commit();
	}
}