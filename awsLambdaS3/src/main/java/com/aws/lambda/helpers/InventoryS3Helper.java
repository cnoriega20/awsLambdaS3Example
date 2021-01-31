package com.aws.lambda.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.lambda.domain.Product;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class InventoryS3Helper {

	protected Product[] getAllProducts() {
		
		Regions clientRegion = Regions.US_EAST_1;
		String bucket = "handy-inventory-bucket";
		String key = "handy-tool-catalog.json";
		S3Object s3Object = new S3Object();
		Product[] products = null;
		Gson gson = new Gson();    	
		
		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
			s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
			
			
			// Using Gson
			BufferedReader br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
			
	    	products = gson.fromJson(br, Product[].class);
	    	
			
		} catch (JsonIOException e) {
			
			e.printStackTrace();
		}catch(JsonSyntaxException ex) {
			
			ex.printStackTrace();
		}
		return products;
	}
	
	protected ArrayList<Product> getAllProductsList(){
		return new ArrayList<Product>(Arrays.asList(getAllProducts()));
	}	
}
