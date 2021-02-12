package eu.christopherlee.admin.manager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import eu.christopherlee.admin.client.TpLinkClient;
import eu.christopherlee.admin.database.TpLinkDao;
import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.ResponseData;
import eu.christopherlee.admin.tplink.model.Result;

public class TpLinkManager {
	private static final Log log = LogFactory.getLog(TpLinkManager.class);
	private int purgeDays;
	private TransactionTemplate transactionTemplate;
	private TpLinkClient client;
	private TpLinkDao dao;
	private Gson gson;

	public void setPurgeDays(int purgeDays) {
		this.purgeDays = purgeDays;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public TpLinkClient getClient() {
		return client;
	}

	public TpLinkDao getDao() {
		return dao;
	}

	public void setClient(TpLinkClient client) {
		this.client = client;
	}

	public void setDao(TpLinkDao dao) {
		this.dao = dao;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	private Account fetchAccount() {
		Account account = null;
		try {
			String clientAccount = client.createToken();
			Result<?> result = (Result<?>) gson.fromJson(clientAccount, Result.class);
			account = gson.fromJson(gson.toJson(result.getResult()), Account.class);
		} catch (URISyntaxException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return account;
	}

	public List<Device> fetchDevices(Account account) {
		List<Device> devices = null;
		try {
			String clientDevices = client.getDeviceList(account.getToken());
			Result<?> result = (Result<?>) gson.fromJson(clientDevices, Result.class);
			LinkedTreeMap<?, ?> map = gson.fromJson(gson.toJson(result.getResult()), LinkedTreeMap.class);
			List<?> objects = (List<?>) map.get("deviceList");
			devices = new ArrayList<Device>();
			for(Object object: objects) {
				Device device = gson.fromJson(gson.toJson(object), Device.class);
				device.setAccountId(account.getAccountId());
				try {
					dao.insertDevice(device);
				} catch (Exception e) {
					dao.insertDevice(device, device.getDeviceId());
				}
				devices.add(device);
			}
		} catch (URISyntaxException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return devices;
	}

	private void synchronizeStats() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(TransactionStatus status) {

				Account account = dao.getAccount(-1);
				if (account == null) {
					account = fetchAccount();
					try {
						dao.insertAccount(account);
					} catch (Exception e) {
						dao.insertAccount(account, account.getAccountId());
					}
				}

				List<Device> devices = dao.getDevices(account.getAccountId());

				if (CollectionUtils.isEmpty(devices)) {
					account = fetchAccount();
					devices = fetchDevices(account);
				}

				if (CollectionUtils.isNotEmpty(devices)) {
					for (Device device : devices) {
						try {
							String clientDeviceState = client.getState(device.getAppServerUrl(), account.getToken(), device.getDeviceId());
							Result<?> result = (Result<?>) gson.fromJson(clientDeviceState, Result.class);
							ResponseData responseData = gson.fromJson(gson.toJson(result.getResult()), ResponseData.class);
							String deviceStateString = gson.fromJson(gson.toJson(responseData.getResponseData()), String.class);
							DeviceState deviceState = gson.fromJson(deviceStateString, DeviceState.class);
							deviceState.getEmeter().getGet_realtime().setStartTime(new Date());
							dao.insertDeviceState(deviceState);
						} catch (URISyntaxException e) {
							log.error(e);
						} catch (IOException e) {
							log.error(e);
						} catch (NullPointerException e) {
							log.error(e);
						} catch (Exception e) {
							log.error(e);
						}
					}
				}

				dao.deleteDeviceState(purgeDays);
			}
		});
	}

	public void scheduledTask() {
		try {
			synchronizeStats();
		} catch (Exception e) {
			log.error(e);
		}
	}
}
