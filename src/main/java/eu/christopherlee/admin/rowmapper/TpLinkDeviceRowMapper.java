package eu.christopherlee.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import eu.christopherlee.admin.tplink.model.Device;

public class TpLinkDeviceRowMapper implements RowMapper<Device> {

	public Device mapRow(ResultSet rs, int line) throws SQLException {
		Device device = new Device();
		device.setAlias(rs.getString("alias"));
		device.setAppServerUrl(rs.getString("app_server_url"));
		device.setDeviceHwVer(rs.getString("device_hw_ver"));
		device.setDeviceId(rs.getString("device_id"));
		device.setDeviceMac(rs.getString("device_mac"));
		device.setDeviceModel(rs.getString("device_model"));
		device.setDeviceName(rs.getString("device_name"));
		device.setDeviceRegion(rs.getString("device_region"));
		device.setDeviceType(rs.getString("device_type"));
		device.setFwId(rs.getString("fw_id"));
		device.setFwVer(rs.getString("fw_ver"));
		device.setHwId(rs.getString("hw_id"));
		device.setOemId(rs.getString("oem_id"));
		device.setRole(rs.getInt("role"));
		device.setSameRegion(rs.getBoolean("is_same_region"));
		device.setStatus(rs.getInt("status"));
		device.setAccountId(rs.getInt("account_id"));
		device.setToken(rs.getString("token"));
		return device;
	}

}
