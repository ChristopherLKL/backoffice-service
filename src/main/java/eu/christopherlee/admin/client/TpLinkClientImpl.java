package eu.christopherlee.admin.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;

public class TpLinkClientImpl extends RestClient implements TpLinkClient, InitializingBean {
	private static final Log log = LogFactory.getLog(TpLinkClient.class);
	private String endPoint;
	private String appType;
	private String cloudUserName;
	private String cloudPassword;
	private String tokenUrl;
	private JSONObject json;
	private JSONObject subJson;

	public void setJson(JSONObject json) {
		this.json = json;
	}

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
		try {
			json.put("method", "login");
			json.put("params", subJson);
			subJson.put("appType", this.appType);
			subJson.put("cloudUserName", this.cloudUserName);
			subJson.put("cloudPassword", this.cloudPassword);
			subJson.put("terminalUUID", UUID.randomUUID());
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(this.endPoint, json);
	}

	public String getDeviceList(String token) throws URISyntaxException, IOException {
		try {
			json.put("method", "getDeviceList");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(this.tokenUrl + token, json);
	}

	public String getState(String uri, String token, String deviceId) throws URISyntaxException, IOException {
		try {
			json.put("method", "passthrough");
			json.put("params", subJson);
			subJson.put("deviceId", deviceId);
			subJson.put("requestData", "{\"system\":{\"get_sysinfo\":null},\"emeter\":{\"get_realtime\":null}}");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(uri + "?token=" + token, json);
	}

	public String setState(String uri, String token, String deviceId, String state) throws URISyntaxException, IOException {
		try {
			json.put("method", "passthrough");
			json.put("params", subJson);
			subJson.put("deviceId", deviceId);
			subJson.put("requestData", "{\"system\":{\"set_relay_state\":{\"state\": " + state + "}}}");
		} catch (JSONException e) {
			log.error(e);
		}
		return this.post(uri + "?token=" + token, json);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		subJson = new JSONObject();
	}
}
