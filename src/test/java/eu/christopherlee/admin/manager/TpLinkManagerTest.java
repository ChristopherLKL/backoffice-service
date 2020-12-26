package eu.christopherlee.admin.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class TpLinkManagerTest {
	

	public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		
		URI uri = new URI("https://eu-wap.tplinkcloud.com?token=c4ee52d2-BTNuYp9Vksy8mX88RwQmgaK");
		HttpPost request = new HttpPost(uri);
		JSONObject device = new JSONObject();
		try {
			device.put("method", "passthrough");
			JSONObject deviceParams = new JSONObject();
			device.put("params", deviceParams);
			deviceParams.put("deviceId", "8006E233377C81A65E5BB1618C1D2DB5179E74F4");
			deviceParams.put("requestData", "{\"system\":{\"get_sysinfo\":null},\"emeter\":{\"get_realtime\":null}}");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		StringEntity entity = new StringEntity(device.toString());
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		request.setEntity(entity);
		response = client.execute(request);
		InputStream inputStream = response.getEntity().getContent();
		StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);

		System.out.println(writer.toString());
	}
}
