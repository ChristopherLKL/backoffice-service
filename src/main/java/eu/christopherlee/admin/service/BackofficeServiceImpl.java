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
import eu.christopherlee.admin.tplink.model.Account;

@Path("/services")
public class BackofficeServiceImpl implements BackofficeService, InitializingBean {
	private static final Log log = LogFactory.getLog(BackofficeServiceImpl.class);

	private TpLinkManager manager;
	
	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login() {
		Account account = manager.getDao().getAccount();
		log.info(account.getEmail());
		User newUser = new User();
		newUser.setUsername(account.getEmail());
		newUser.setPassword("titi");
		return newUser;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
