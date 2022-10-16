package com.action.util;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.drivers.api.APIActionEngine;
import com.framework.utils.GlobalVariables;
import com.framework.utils.Reporter;

/*
 * Class to handle all REST calls(POST/GET/PUT/DELETE)
 * 
 * @author 10675365
 * 
 */

public class GraphQLAction implements APIActionEngine {

	public static HttpClientBuilder clientBuilder;
	private static CloseableHttpClient httpClient;
	private static InheritableThreadLocal<HttpResponse> response = new InheritableThreadLocal<HttpResponse>();
	private static InheritableThreadLocal<String> responseBody = new InheritableThreadLocal<String>();

	/*
	 * Client for SSL
	 * 
	 */
	static {
		String authMethod = GlobalVariables.configProp.getProperty("Authentication");
		if (authMethod.equalsIgnoreCase("Basic")) {
			clientBuilder = HttpClients.custom();
			clientBuilder
					.setDefaultHeaders(Arrays.asList(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")));
			clientBuilder.setDefaultHeaders(Arrays.asList(new BasicHeader(HttpHeaders.AUTHORIZATION,
					"Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDYxYmFjZmRlNTE5NThjMDA2OTQ3YWRiNiJ9LCJuaWNrbmFtZSI6InZpbm90aHJhanUucmFuZ2FuIiwibmFtZSI6InZpbm90aHJhanUucmFuZ2FuQGdtYWlsLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci9kNTllYTBlZTU2ZTE2YmJiYWQxOGYzYWNhNGZjN2VkNT9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRnZpLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDIyLTEwLTE2VDA1OjM2OjM5LjA4NFoiLCJpc3MiOiJodHRwczovL2dyYXBocWwtdHV0b3JpYWxzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2MWJhY2ZkZTUxOTU4YzAwNjk0N2FkYjYiLCJhdWQiOiJQMzhxbkZvMWxGQVFKcnprdW4tLXdFenFsalZOR2NXVyIsImlhdCI6MTY2NTg5ODYwMCwiZXhwIjoxNjY1OTM0NjAwLCJhdF9oYXNoIjoiU2p1S0g0dHNtYWMtUHd3SGFmVmNNUSIsInNpZCI6IndQX0Z6N3hWMTZSLUVSbHJuZ2dsRWZrcnY2TjVJTHdfIiwibm9uY2UiOiI1M3hQYjV2RlhTUk1mNlowaGQ1LTJ0WUFzT2NYWm9pViJ9.ARwHetoT89sjz7g_Dnq3U0WCrEOFKW9ZxRVw7mNTp6a7lQLu1BN-6nUdH6FKQ0qF-lfjHhUxwwtd0Qqmmn-yXjSGSDd6CuHn_lXO8qYAWYS0nxz-5Ywm-asGNJgQVnIFN7nnWHe8TXmaawQLLx3EAWG7_F6kKLsjOFwpQiC-WESi9mrJEjZQrkcgIIKcnzZ3XJNmQf-svHO7PIH-5MgBXGzZFq1a8VmgSyQ1MLsfxO8WaCeQytaS06Jmnii0bhQ-XilgLd3QUiCMukdlyxW6b0-t6mDK6p9FeoYbk7udp6ydeGq9tAHjKhMlPmwVjT_HDiLuIBIawJJuo5rH0a2wCA")));
			clientBuilder.setSSLSocketFactory(getSSLContext());
			CloseableHttpClient client = clientBuilder.build();
			httpClient = client;
		}
		if (authMethod.equalsIgnoreCase("OAuth2")) {
			// Code yet to implement
		}
	}

	/*
	 * Post method - HttpPost
	 * 
	 */
	public void POST(String endPoint, String jsonBody) {

		try {

			URI url = new URIBuilder(endPoint).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("content-type", "application/json");

			StringEntity stringEntity = new StringEntity(jsonBody);
			httpPost.setEntity(stringEntity);
			
			Reporter.addStepLog("<strong>Request URL: </strong><br> <pre>" + httpPost.getURI() + "</pre>");
			Reporter.addStepLog("<strong>Request Json: </strong><br> <pre>" + jsonBody + "</pre>");
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			String resEntity = EntityUtils.toString(httpResponse.getEntity());
			
			Reporter.addStepLog("<strong>Response Json: </strong><br> <pre>" + resEntity + "</pre>");
			if (httpResponse != null) {
				response.set(httpResponse);
				responseBody.set(resEntity);
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*
	 * Client for multi thread
	 * 
	 */
	public static CloseableHttpClient getConcurrentClient(int threadPoolCount) {
		// Create the pool connection manager
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// Set the pool size
		connectionManager.setMaxTotal(threadPoolCount);

		// Make the client builder
		HttpClientBuilder clientBuilder = HttpClients.custom();
		// Set the connection manager
		clientBuilder.setConnectionManager(connectionManager);
		// Build the client
		CloseableHttpClient client = clientBuilder.build();
		return client;
	}

	public HttpResponse getResponse() {
		return response.get();
	}

	public String getResponseBody() {
		return responseBody.get();
	}

	public void resetClient() throws IOException {
		httpClient.close();
	}

	// Default client
	public static CloseableHttpClient getDefaultClient() {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		return closeableHttpClient;
	}

	// In case your service is secure with SSL and Certs
	private static SSLConnectionSocketFactory getSSLContext() {

		TrustStrategy trustStrategy = new TrustStrategy() {

			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {

				return true;
			}
		};

		HostnameVerifier allVerifier = new NoopHostnameVerifier();
		SSLConnectionSocketFactory connFactory = null;
		try {
			connFactory = new SSLConnectionSocketFactory(
					SSLContextBuilder.create().loadTrustMaterial(trustStrategy).build(), allVerifier);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connFactory;
	}

	@Override
	public void GET(String endpoint) {
	}

	@Override
	public void PUT(String endpoint, String jsonBody) {
	}

	@Override
	public void DELETE(String endpoint) {
	}

}
