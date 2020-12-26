package eu.christopherlee.admin.manager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.gson.Gson;

import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.ResponseData;
import eu.christopherlee.admin.tplink.model.Result;

public class TpLinkTask extends TimerTask implements InitializingBean {
	private static final Log log = LogFactory.getLog(TpLinkTask.class);
	private TpLinkManager manager;
	private Gson gson;

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	private void scheduledTask() {
		Account account = manager.getDao().getAccount();
		if (account == null) {
			account = manager.fetchAccount();
			try {
				manager.getDao().insertAccount(account);
			} catch (Exception e) {
				manager.getDao().insertAccount(account, account.getAccountId());
			}
		}

		List<Device> devices = manager.getDao().getDevices();

		if (CollectionUtils.isEmpty(devices)) {
			account = manager.fetchAccount();
			devices = manager.fetchDevices(account.getToken());
		}

		if (CollectionUtils.isNotEmpty(devices)) {
			for (Device device : devices) {
				try {
					String clientDeviceState = manager.getClient().getState(device.getAppServerUrl(), account.getToken(), device.getDeviceId());
					Result<ResponseData> result = (Result<ResponseData>) gson.fromJson(clientDeviceState, Result.class);
					ResponseData responseData = gson.fromJson(gson.toJson(result.getResult()), ResponseData.class);
					String deviceStateString = gson.fromJson(gson.toJson(responseData.getResponseData()), String.class);
					DeviceState deviceState = gson.fromJson(deviceStateString, DeviceState.class);
					deviceState.getEmeter().getGet_realtime().setStartTime(new Date());
					manager.getDao().insertDeviceState(deviceState);
				} catch (URISyntaxException e) {
					log.error(e);
					e.printStackTrace();
				} catch (IOException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		}

		manager.getDao().deleteDeviceState(3);
	}

	@Override
	public void run() {
		this.scheduledTask();
	}

	public void afterPropertiesSet() throws Exception {
	}
}