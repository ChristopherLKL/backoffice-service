package eu.christopherlee.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import eu.christopherlee.admin.tplink.model.DeviceState;
import eu.christopherlee.admin.tplink.model.Emeter;
import eu.christopherlee.admin.tplink.model.Get_realtime;
import eu.christopherlee.admin.tplink.model.Get_sysinfo;

public class TpLinkDeviceStateRowMapper implements RowMapper<DeviceState> {

	public DeviceState mapRow(ResultSet rs, int line) throws SQLException {
		DeviceState ds = new DeviceState();
		Get_sysinfo get_sysinfo = new Get_sysinfo();
		eu.christopherlee.admin.tplink.model.System system = new eu.christopherlee.admin.tplink.model.System();
		ds.setSystem(system);
		system.setGet_sysinfo(get_sysinfo);
		get_sysinfo.setActive_mode(rs.getString("active_mode"));
		get_sysinfo.setAlias(rs.getString("alias"));
		get_sysinfo.setDeviceId(rs.getString("device_id"));
		get_sysinfo.setDev_name(rs.getString("dev_name"));
		get_sysinfo.setFeature(rs.getString("feature"));
		get_sysinfo.setFwId(rs.getString("fw_id"));
		get_sysinfo.setHwId(rs.getString("hw_id"));
		get_sysinfo.setHw_ver(rs.getString("hw_ver"));
		get_sysinfo.setIcon_hash(rs.getString("icon_hash"));
		get_sysinfo.setLatitude(rs.getInt("latitude"));
		get_sysinfo.setLed_off(rs.getInt("led_off"));
		get_sysinfo.setLongitude(rs.getInt("longitude"));
		get_sysinfo.setMac(rs.getString("mac"));
		get_sysinfo.setModel(rs.getString("model"));
		get_sysinfo.setOemId(rs.getString("oem_id"));
		get_sysinfo.setOn_time(rs.getInt("on_time"));
		get_sysinfo.setRelay_state(rs.getString("relay_state"));
		get_sysinfo.setRssi(rs.getInt("rssi"));
		get_sysinfo.setSw_ver(rs.getString("sw_ver"));
		get_sysinfo.setType(rs.getString("type"));
		get_sysinfo.setUpdating(rs.getInt("updating"));
		
		Get_realtime get_realtime = new Get_realtime();
		Emeter emeter = new Emeter();
		ds.setEmeter(emeter);
		emeter.setGet_realtime(get_realtime);
		get_realtime.setCurrent(rs.getInt("current"));
		get_realtime.setPower(rs.getInt("power"));
		get_realtime.setStartTime(rs.getDate("start_time"));
		get_realtime.setTotal(rs.getInt("total"));
		get_realtime.setVoltage(rs.getInt("voltage"));
		
		return ds;
	}
}
