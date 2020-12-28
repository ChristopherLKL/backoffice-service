package eu.christopherlee.admin.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import eu.christopherlee.admin.manager.TpLinkManager;
import eu.christopherlee.admin.model.User;
import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.Period;

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


	@GET
	@Path("/tplink/account")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Account getTpLinkAccount() {
		return manager.getDao().getAccount();
	}

	@GET
	@Path("/tplink/{accountId}/devices")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<Device> getTpLinkDevices(@PathParam("accountId") int accountId) {
		return manager.getDao().getDevices(accountId);
	}

	@GET
	@Path("/tplink/{accountId}/device/{id}/stats/{period}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<DeviceState> getTpLinkDeviceStates(@PathParam("accountId") int accountId, @PathParam("id") String deviceId, @PathParam("period") Period period) {
		return manager.getDao().getDeviceState(accountId, deviceId, period);
	}

}
