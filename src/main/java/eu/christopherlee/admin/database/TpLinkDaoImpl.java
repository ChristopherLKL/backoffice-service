package eu.christopherlee.admin.database;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import eu.christopherlee.admin.rowmapper.TpLinkAccountRowMapper;
import eu.christopherlee.admin.rowmapper.TpLinkDeviceRowMapper;
import eu.christopherlee.admin.rowmapper.TpLinkDeviceStateRowMapper;
import eu.christopherlee.admin.tplink.model.Account;
import eu.christopherlee.admin.tplink.model.Device;
import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.Period;

public class TpLinkDaoImpl implements TpLinkDao {
	private TpLinkAccountRowMapper accountRowMapper = new TpLinkAccountRowMapper();
	private TpLinkDeviceRowMapper deviceRowMapper = new TpLinkDeviceRowMapper();
	private TpLinkDeviceStateRowMapper deviceStateRowMapper = new TpLinkDeviceStateRowMapper();
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertAccount(Account account) {
		String query = Database.doInsert(Tables.TABLE_TPLINK_LAST_CONNECT,
				"account_id, reg_time, country_code, email, token", "?, ?, ?, ?, ?");
		Object[] parameters = new Object[] { account.getAccountId(), account.getRegTime(), account.getCountryCode(),
				account.getEmail(), account.getToken() };
		jdbcTemplate.update(query, parameters);
	}

	public void insertAccount(Account account, int accountId) {
		String query = Database.doUpdate(Tables.TABLE_TPLINK_LAST_CONNECT,
				"reg_time=?, country_code=?, email=?, token=?", "account_id=?");
		Object[] parameters = new Object[] { account.getRegTime(), account.getCountryCode(),
				account.getEmail(), account.getToken(), account.getAccountId() };
		jdbcTemplate.update(query, parameters);
	}

	public Account getAccount() {
		String query = Database.doSelect("account_id, reg_time, country_code, email, token",
				Tables.TABLE_TPLINK_LAST_CONNECT, null);
		List<Account> accounts = jdbcTemplate.query(query, accountRowMapper);
		return (CollectionUtils.isEmpty(accounts) ? null : accounts.get(0));
	}

	public void insertDevice(Device device) {
		String query = Database.doInsert(Tables.TABLE_TPLINK_LAST_DEVICES,
				"device_type, role, fw_ver, app_server_url, device_region, device_id, device_name, device_hw_ver, alias, device_mac, oem_id, device_model, hw_id, fw_id, is_same_region, status, account_id",
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
		Object[] parameters = new Object[] { device.getDeviceType(), device.getRole(), device.getFwVer(),
				device.getAppServerUrl(), device.getDeviceRegion(), device.getDeviceId(), device.getDeviceName(),
				device.getDeviceHwVer(), device.getAlias(), device.getDeviceMac(), device.getOemId(),
				device.getDeviceModel(), device.getHwId(), device.getFwId(), device.isSameRegion(),
				device.getStatus(), device.getAccountId() };
		jdbcTemplate.update(query, parameters);
	}

	public void insertDevice(Device device, String deviceId) {
		String query = Database.doUpdate(Tables.TABLE_TPLINK_LAST_DEVICES,
				"device_type=?, role=?, fw_ver=?, app_server_url=?, device_region=?, device_name=?, device_hw_ver=?, alias=?, device_mac=?, oem_id=?, device_model=?, hw_id=?, fw_id=?, is_same_region=?, status=?, account_id=?",
				"device_id=?");
		Object[] parameters = new Object[] { device.getDeviceType(), device.getRole(), device.getFwVer(),
				device.getAppServerUrl(), device.getDeviceRegion(), device.getDeviceName(),
				device.getDeviceHwVer(), device.getAlias(), device.getDeviceMac(), device.getOemId(),
				device.getDeviceModel(), device.getHwId(), device.getFwId(), device.isSameRegion(),
				device.getStatus(), device.getAccountId(), device.getDeviceId() };
		jdbcTemplate.update(query, parameters);
	}

	public List<Device> getDevices(int accountId) {
		String query = Database.doSelect(
				"device_type, role, fw_ver, app_server_url, device_region, device_id, device_name, device_hw_ver, alias, device_mac, oem_id, device_model, hw_id, fw_id, is_same_region, status, account_id",
				Tables.TABLE_TPLINK_LAST_DEVICES, "account_id=?");
		Object[] parameters = new Object[] { accountId };
		List<Device> devices = jdbcTemplate.query(query, parameters, deviceRowMapper);
		return devices;
	}

	public void insertDeviceState(DeviceState deviceState) {
		String version = deviceState.getSystem().getGet_sysinfo().getHw_ver();
		String query = Database.doInsert(Tables.TABLE_TPLINK_DEVICE_STATE,
				"sw_ver, hw_ver, type, model, mac, device_id, hw_id, fw_id, oem_id, alias, dev_name, icon_hash, relay_state, on_time, active_mode, feature, updating, rssi, led_off, latitude, longitude, current, voltage, power, total, start_time",
				"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
		Object[] parameters = new Object[] { deviceState.getSystem().getGet_sysinfo().getSw_ver(), deviceState.getSystem().getGet_sysinfo().getHw_ver(),
				deviceState.getSystem().getGet_sysinfo().getType(), deviceState.getSystem().getGet_sysinfo().getModel(), deviceState.getSystem().getGet_sysinfo().getMac(),
				deviceState.getSystem().getGet_sysinfo().getDeviceId(), deviceState.getSystem().getGet_sysinfo().getHwId(),
				deviceState.getSystem().getGet_sysinfo().getFwId(), deviceState.getSystem().getGet_sysinfo().getOemId(),
				deviceState.getSystem().getGet_sysinfo().getAlias(), deviceState.getSystem().getGet_sysinfo().getDev_name(),
				deviceState.getSystem().getGet_sysinfo().getIcon_hash(), deviceState.getSystem().getGet_sysinfo().getRelay_state(),
				deviceState.getSystem().getGet_sysinfo().getOn_time(), deviceState.getSystem().getGet_sysinfo().getActive_mode(),
				deviceState.getSystem().getGet_sysinfo().getFeature(), deviceState.getSystem().getGet_sysinfo().getUpdating(),
				deviceState.getSystem().getGet_sysinfo().getRssi(), deviceState.getSystem().getGet_sysinfo().getLed_off(),
				("2.0".equals(version) ? (float) deviceState.getSystem().getGet_sysinfo().getLatitude_i() / 10000 : deviceState.getSystem().getGet_sysinfo().getLatitude()),
				("2.0".equals(version) ? (float) deviceState.getSystem().getGet_sysinfo().getLongitude_i() / 10000 : deviceState.getSystem().getGet_sysinfo().getLongitude()),
				("2.0".equals(version) ? (float) deviceState.getEmeter().getGet_realtime().getCurrent_ma() / 1000 : deviceState.getEmeter().getGet_realtime().getCurrent()),
				("2.0".equals(version) ? (float) deviceState.getEmeter().getGet_realtime().getVoltage_mv() / 1000 : deviceState.getEmeter().getGet_realtime().getVoltage()),
				("2.0".equals(version) ? (float) deviceState.getEmeter().getGet_realtime().getPower_mw() / 1000 : deviceState.getEmeter().getGet_realtime().getPower()),
				("2.0".equals(version) ? (float) deviceState.getEmeter().getGet_realtime().getTotal_wh() / 1000 : deviceState.getEmeter().getGet_realtime().getTotal()),
				deviceState.getEmeter().getGet_realtime().getStartTime() };
		jdbcTemplate.update(query, parameters);
	}

	public List<DeviceState> getDeviceState(int accountId, String deviceId, Period period) {
		String query = Database.doSelect(
				"ds.sw_ver, ds.hw_ver, ds.type, ds.model, ds.mac, ds.device_id, ds.hw_id, ds.fw_id, ds.oem_id, ds.alias, ds.dev_name, ds.icon_hash, ds.relay_state, ds.on_time, ds.active_mode, ds.feature, ds.updating, ds.rssi, ds.led_off, ds.latitude, ds.longitude, ds.current, ds.voltage, ds.power, ds.total, ds.start_time",
				Tables.TABLE_TPLINK_DEVICE_STATE + " ds inner join " + Tables.TABLE_TPLINK_LAST_DEVICES + " d on ds.device_id=d.device_id inner join " + Tables.TABLE_TPLINK_LAST_CONNECT + " a on a.account_id=d.account_id", "d.account_id=? and ds.device_id=? and ds.start_time >= NOW() - INTERVAL ? " + period);
		Object[] parameters = new Object[] { accountId, deviceId, "1" };
		List<DeviceState> deviceState = jdbcTemplate.query(query, parameters, deviceStateRowMapper);
		return deviceState;
	}

	public void deleteDeviceState(int months) {
		String sql = Database.doDelete(Tables.TABLE_TPLINK_DEVICE_STATE, "start_time <= NOW() - INTERVAL ? MONTH");

		Object[] parameters = new Object[] { months };

		jdbcTemplate.update(sql, parameters);
	}
}
