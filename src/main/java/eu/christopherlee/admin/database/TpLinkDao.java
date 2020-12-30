package eu.christopherlee.admin.database;

import java.util.List;

import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.Period;

public interface TpLinkDao {
	void insertAccount(Account account);
	void insertAccount(Account account, int accountId);
	Account getAccount(int accountId);
	void insertDevice(Device device);
	void insertDevice(Device device, String deviceId);
	List<Device> getDevices(int accountId);
	void insertDeviceState(DeviceState deviceState);
	List<DeviceState> getDeviceState(int accountId, String deviceId, Period period);
	void deleteDeviceState(int months);
}
