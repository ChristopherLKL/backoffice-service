package eu.christopherlee.admin.database;

import java.util.List;

import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;

public interface TpLinkDao {
	void insertAccount(Account account);
	void insertAccount(Account account, int accountId);
	Account getAccount();
	void insertDevice(Device device);
	void insertDevice(Device device, String deviceId);
	List<Device> getDevices();
	void insertDeviceState(DeviceState deviceState);
	List<DeviceState> getDeviceState();
	void deleteDeviceState(int months);
}
