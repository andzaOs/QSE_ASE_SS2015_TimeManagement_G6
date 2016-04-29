package at.tuwien.ase.data;

import java.util.ArrayList;
import java.util.List;

import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

public class GenerateUsers {
	
	private static List<User> workers = new ArrayList<User>();
	private static List<User> supervisors = new ArrayList<User>();
	private static List<User> managers = new ArrayList<User>();

	public static void createWorkers() {
		
		User w_1 = new User();
		w_1.setForname("Simon");
		w_1.setLastname("Dominic");
		w_1.setUsername("soDominic");
		w_1.setPassword("soDominic");
		w_1.setUserType(UserType.WORKER);
		assignCompany(w_1, "PORR");
		
		User w_2 = new User();
		w_2.setForname("Mario");
		w_2.setLastname("Rinner");
		w_2.setUsername("maRinner");
		w_2.setPassword("maRinner");
		w_2.setUserType(UserType.WORKER);
		assignCompany(w_2, "PORR");
		
		User w_3 = new User();
		w_3.setForname("Kurt");
		w_3.setLastname("Berger");
		w_3.setUsername("kuBerger");
		w_3.setPassword("kuBerger");
		w_3.setUserType(UserType.WORKER);
		assignCompany(w_3, "PORR");
		
		User w_4 = new User();
		w_4.setForname("Piotr");
		w_4.setLastname("Sloka");
		w_4.setUsername("piSloka");
		w_4.setPassword("piSloka");
		w_4.setUserType(UserType.WORKER);
		assignCompany(w_4, "Kirsch");
		
		User w_5 = new User();
		w_5.setForname("Marcin");
		w_5.setLastname("Hyla");
		w_5.setUsername("maHyla");
		w_5.setPassword("maHyla");
		w_5.setUserType(UserType.WORKER);
		assignCompany(w_5, "Nivelatherm");
		
		User w_6 = new User();
		w_6.setForname("Jan");
		w_6.setLastname("Mateja");
		w_6.setUsername("jaMateja");
		w_6.setPassword("jaMateja");
		w_6.setUserType(UserType.WORKER);
		assignCompany(w_6, "Nivelatherm");
		
		User w_7 = new User();
		w_7.setForname("Marco");
		w_7.setLastname("Metze");
		w_7.setUsername("maMetze");
		w_7.setPassword("maMetze");
		w_7.setUserType(UserType.WORKER);
		assignCompany(w_7, "KWErdbau");
		
		User w_8 = new User();
		w_8.setForname("Emanuel");
		w_8.setLastname("Baunnack");
		w_8.setUsername("emBaunnack");
		w_8.setPassword("emBaunnack");
		w_8.setUserType(UserType.WORKER);
		assignCompany(w_8, "KWErdbau");
		
		User w_9 = new User();
		w_9.setForname("Olaf");
		w_9.setLastname("Krahl");
		w_9.setUsername("olKrahl");
		w_9.setPassword("olKrahl");
		w_9.setUserType(UserType.WORKER);
		assignCompany(w_9, "Sauerbrey");
		
		User w_10 = new User();
		w_10.setForname("Norbert");
		w_10.setLastname("Böge");
		w_10.setUsername("noBöge");
		w_10.setPassword("noBöge");
		w_10.setUserType(UserType.WORKER);
		assignCompany(w_10, "Sauerbrey");
		
		workers.add(w_1);
		workers.add(w_2);
		workers.add(w_3);
		workers.add(w_4);
		workers.add(w_5);
		workers.add(w_6);
		workers.add(w_7);
		workers.add(w_8);
		workers.add(w_9);
		workers.add(w_10);
	}
	
	public static void createSupervisors() {
		
		User s_1 = new User();
		s_1.setForname("Christian");
		s_1.setLastname("Zupfer");
		s_1.setUsername("chZupfer");
		s_1.setPassword("chZupfer");
		s_1.setUserType(UserType.SUPERVISOR);
		assignCompany(s_1, "PORR");
		
		User s_2 = new User();
		s_2.setForname("Gerald");
		s_2.setLastname("Janisch");
		s_2.setUsername("geJanisch");
		s_2.setPassword("geJanisch");
		s_2.setUserType(UserType.SUPERVISOR);
		assignCompany(s_2, "PORR");
		
		User s_3 = new User();
		s_3.setForname("Hannes");
		s_3.setLastname("Vakovics");
		s_3.setUsername("haVakovics");
		s_3.setPassword("haVakovics");
		s_3.setUserType(UserType.SUPERVISOR);
		assignCompany(s_3, "PORR");
		
		supervisors.add(s_1);
		supervisors.add(s_2);
		supervisors.add(s_3);
	}

	public static void createManagers() {
		
		User m_1 = new User();
		m_1.setForname("John");
		m_1.setLastname("Doe");
		m_1.setUsername("joDoe");
		m_1.setPassword("joDoe");
		m_1.setUserType(UserType.MANAGER);
		assignCompany(m_1, "PORR");
		
		User m_2 = new User();
		m_2.setForname("Max");
		m_2.setLastname("Muster");
		m_2.setUsername("maMuster");
		m_2.setPassword("maMuster");
		m_2.setUserType(UserType.MANAGER);
		assignCompany(m_2, "PORR");
		
		managers.add(m_1);
		managers.add(m_2);
	}
	
	private static void assignCompany(User u, String companyName) {
		
		for (Company c : GenerateCompanies.getCompanies()) {
			
			if (c.getName().equals(companyName)) {
				u.setCompany(c);
				break;
			}
		}
	}

	public static List<User> getWorkers() {
		return workers;
	}

	public static List<User> getSupervisors() {
		return supervisors;
	}

	public static List<User> getManagers() {
		return managers;
	}
}