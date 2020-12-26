package eu.christopherlee.admin.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import eu.christopherlee.admin.manager.TpLinkManager;
import eu.christopherlee.admin.model.User;

@Path("/services")
public class BackofficeServiceImpl implements InitializingBean {
	private static final Log log = LogFactory.getLog(BackofficeServiceImpl.class);
	
	private TpLinkManager manager;

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login() {
		manager = new TpLinkManager();	
		log.info(manager.getHelloWorld());
		User newUser = new User();
		newUser.setUsername(manager.getHelloWorld());
		newUser.setPassword("titi");
		return newUser;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
