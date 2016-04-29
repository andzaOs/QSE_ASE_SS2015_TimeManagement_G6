package at.tuwien.ase.rest.administration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import at.tuwien.ase.dao.administration.AdministrationDaoI;
import at.tuwien.ase.rest.administration.AdministrationRestI;

@Stateless// please use only stateles sessionbeans
@Path("/AdministrationRest")

public class AdministrationRest implements AdministrationRestI {

	@EJB//inject 
	AdministrationDaoI adminDao;
	
	@Override
	@GET
	@Path("populate")
	@Produces("application/json")
	public boolean populateDB() throws Exception {
		
		System.out.println("Initial population of the DB.");
		
		return adminDao.populateDB();
	}

}
