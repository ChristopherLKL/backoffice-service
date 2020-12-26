package eu.christopherlee.admin.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class RestClient {
	protected String post(String stringUri, JSONObject json) throws URISyntaxException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;

		URI uri = new URI(stringUri);
		HttpPost request = new HttpPost(uri);
		StringEntity entity = new StringEntity(json.toString());
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		request.setEntity(entity);
		response = client.execute(request);
		InputStream inputStream = response.getEntity().getContent();
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
		return writer.toString();
	}
}
