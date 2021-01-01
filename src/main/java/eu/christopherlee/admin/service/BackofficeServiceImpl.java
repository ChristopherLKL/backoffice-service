package eu.christopherlee.admin.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import eu.christopherlee.admin.manager.TpLinkManager;
import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.DeviceState.RelayState;
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
		List<Device> devices = manager.getDao().getDevices(accountId);
		for(Device device : devices) {
			try {
				String clientDeviceState = manager.getClient().getState(device.getAppServerUrl(), device.getToken(), device.getDeviceId());
				Result<?> result = (Result<?>) gson.fromJson(clientDeviceState, Result.class);
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
		return manager.getDao().getDeviceStates(accountId, deviceId, period);
	}

	@GET
	@Path("/tplink/{accountId}/devices/state")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<DeviceState> getTpLinkCurrentDevicesState(@PathParam("accountId") int accountId) {
		List<Device> devices = manager.getDao().getDevices(accountId);
		List<DeviceState> states = new ArrayList<DeviceState>();
		for(Device device: devices) {
			String clientDeviceState;
			try {
				clientDeviceState = manager.getClient().getState(device.getAppServerUrl(), device.getToken(), device.getDeviceId());
				Result<?> result = (Result<?>) gson.fromJson(clientDeviceState, Result.class);
				ResponseData responseData = gson.fromJson(gson.toJson(result.getResult()), ResponseData.class);
				String deviceStateString = gson.fromJson(gson.toJson(responseData.getResponseData()), String.class);
				DeviceState deviceState = gson.fromJson(deviceStateString, DeviceState.class);
				if("2.0".equals(deviceState.getSystem().getGet_sysinfo().getHw_ver())) {
					deviceState.getEmeter().getGet_realtime().setPower((float) deviceState.getEmeter().getGet_realtime().getPower_mw() / 1000);
					deviceState.getEmeter().getGet_realtime().setTotal((float) deviceState.getEmeter().getGet_realtime().getTotal_wh() / 1000);
				}
				states.add(deviceState);
			} catch (URISyntaxException | IOException e) {
				log.error(e);
			}
		}
		return states;
	}

	@PUT
	@Path("/tplink/{accountId}/device/{id}/state")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void setTpLinkCurrentDeviceState(@PathParam("accountId") int accountId, @PathParam("id") String deviceId, RelayState relayState) {
		Device device = manager.getDao().getDevices(accountId).stream().filter(c -> c.getDeviceId().equals(deviceId)).collect(Collectors.toList()).get(0);
		try {
			manager.getClient().setState(device.getAppServerUrl(), device.getToken(), device.getDeviceId(), String.valueOf(relayState.getValue()));
		} catch (URISyntaxException | IOException e) {
			log.error(e);
		}
		
	}
}
