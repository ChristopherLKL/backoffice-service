package eu.christopherlee.admin.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class TpLinkClientImpl extends RestClient implements TpLinkClient {
	private static final Log log = LogFactory.getLog(TpLinkClient.class);
	private String endPoint;
	private String appType;
	private String cloudUserName;
	private String cloudPassword;
	private String tokenUrl;

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public void setCloudUserName(String cloudUserName) {
		this.cloudUserName = cloudUserName;
	}

	public void setCloudPassword(String cloudPassword) {
		this.cloudPassword = cloudPassword;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String createToken() throws URISyntaxException, IOException {
		JSONObject device = new JSONObject();
		try {
			device.put("method", "login");
			JSONObject deviceParams = new JSONObject();
			device.put("params", deviceParams);
			deviceParams.put("appType", this.appType);
			deviceParams.put("cloudUserName", this.cloudUserName);
			deviceParams.put("cloudPassword", this.cloudPassword);
			deviceParams.put("terminalUUID", UUID.randomUUID());
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(this.endPoint, device);
	}

	public String getDeviceList(String token) throws URISyntaxException, IOException {
		JSONObject json = new JSONObject();
		try {
			json.put("method", "getDeviceList");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(this.tokenUrl + token, json);
	}

	public String getState(String uri, String token, String deviceId) throws URISyntaxException, IOException {
		JSONObject device = new JSONObject();
		try {
			device.put("method", "passthrough");
			JSONObject deviceParams = new JSONObject();
			device.put("params", deviceParams);
			deviceParams.put("deviceId", deviceId);
			deviceParams.put("requestData", "{\"system\":{\"get_sysinfo\":null},\"emeter\":{\"get_realtime\":null}}");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(uri + "?token=" + token, device);
	}

	public String setState(String uri, String token, String deviceId, String state) throws URISyntaxException, IOException {
		JSONObject device = new JSONObject();
		try {
			device.put("method", "passthrough");
			JSONObject deviceParams = new JSONObject();
			device.put("params", deviceParams);
			deviceParams.put("deviceId", deviceId);
			deviceParams.put("requestData", "{\"system\":{\"set_relay_state\":{\"state\": " + state + "}}");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(uri + "?token=" + token, device);
	}
}
