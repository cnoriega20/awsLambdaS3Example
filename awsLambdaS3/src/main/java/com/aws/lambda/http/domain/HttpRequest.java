package com.aws.lambda.http.domain;

import java.util.Map;

public class HttpRequest {
	
	Map<String, String> queryStringParameters;
	Map<String, String> pathParameters;
	String body;
	
	public Map<String, String> getQueryStringParameters() {
		return queryStringParameters;
	}
	public void setQueryStringParameters(Map<String, String> queryStringParameters) {
		this.queryStringParameters = queryStringParameters;
	}
	public Map<String, String> getPathParameters() {
		return pathParameters;
	}
	public void setPathParameters(Map<String, String> pathParameters) {
		this.pathParameters = pathParameters;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	

}
