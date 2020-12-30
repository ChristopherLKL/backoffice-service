package eu.christopherlee.admin.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import eu.christopherlee.admin.manager.TpLinkManager;
import eu.christopherlee.admin.model.User;
import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.Period;
import eu.christopherlee.admin.tplink.model.ResponseData;
import eu.christopherlee.admin.tplink.model.Result;

@Path("/services")
public class BackofficeServiceImpl implements BackofficeService {
	private static final Log log = LogFactory.getLog(BackofficeServiceImpl.class);
	private TpLinkManager manager;
	private Gson gson;
	
	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login() {
		Account account = manager.getDao().getAccount(-1);
		log.info(account.getEmail());
		User newUser = new User();
		newUser.setUsername(account.getEmail());
		newUser.setPassword("titi");
		return newUser;
	}

	@GET
	@Path("/tplink/account")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Account getTpLinkAccount() {
		return manager.getDao().getAccount(-1);
	}

	@GET
	@Path("/tplink/{accountId}/devices")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<Device> getTpLinkDevices(@PathParam("accountId") int accountId) {
		Account account = manager.getDao().getAccount(accountId);
		List<Device> devices = manager.fetchDevices(account);
		for(Device device : devices) {
			try {
				String clientDeviceState = manager.getClient().getState(device.getAppServerUrl(), account.getToken(), device.getDeviceId());
				Result<ResponseData> result = (Result<ResponseData>) gson.fromJson(clientDeviceState, Result.class);
				ResponseData responseData = gson.fromJson(gson.toJson(result.getResult()), ResponseData.class);
				String deviceStateString = gson.fromJson(gson.toJson(responseData.getResponseData()), String.class);
				DeviceState deviceState = gson.fromJson(deviceStateString, DeviceState.class);
				device.setDeviceState(deviceState);
			} catch (URISyntaxException | IOException e) {
				log.error(e);
			}
		}
		return devices;
	}

	@GET
	@Path("/tplink/{accountId}/device/{id}/stats/{period}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<DeviceState> getTpLinkDeviceStates(@PathParam("accountId") int accountId, @PathParam("id") String deviceId, @PathParam("period") Period period) {
		return manager.getDao().getDeviceState(accountId, deviceId, period);
	}

}
