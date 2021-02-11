package com.aws.lambda.http.domain;

import java.util.HashMap;
import java.util.Map;

import com.aws.lambda.domain.Product;
import com.google.gson.Gson;

public class HttpProductResponse {

	private String body;
	

	private String statusCode = "200";
	
	private Map<String, String> headers = new HashMap<>();

	public HttpProductResponse() {
		super();
		this.headers.put("Content-Type", "application/json");
	}
	
	public HttpProductResponse(Product product) {
		this();
		this.body = product.toString();
	}
	
	public HttpProductResponse(Product[] products) {
		this();
		Gson gson = new Gson();
		this.body = gson.toJson(products);
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
}
