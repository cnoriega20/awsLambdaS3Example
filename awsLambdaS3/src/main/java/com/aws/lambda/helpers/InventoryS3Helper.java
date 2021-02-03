package com.aws.lambda.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.lambda.domain.Product;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class InventoryS3Helper {

	Regions clientRegion = Regions.US_EAST_1;
	String bucket = "handy-inventory-bucket";
	String key = "handy-tool-catalog.json";
	S3Object s3Object = new S3Object();
	
	protected Product[] getAllProducts() throws JsonIOException, JsonSyntaxException{
		
		Product[] products = null;
		Gson gson = new Gson();    	
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
		s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
		
		
		// Using a BufferedReader to process the S3Object Stream
		BufferedReader br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
		
		//Marhsall a Json object to a POJO.
    	products = gson.fromJson(br, Product[].class);
		
    	return products;
	}
	
	protected List<Product> getAllProductsList(){
		return new ArrayList<>(Arrays.asList(getAllProducts()));
	}
	
	
	
	protected boolean updateAllProducts(Product [] products) throws AwsServiceException,
    SdkClientException, S3Exception{
		
		Gson gson = new Gson(); 
		String jsonString = gson.toJson(products);
		
		Region region = Region.US_EAST_1; 
		S3Client s3Client = S3Client.builder().region(region).build();
		
		PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build(),
				RequestBody.fromString(jsonString));
		return putResponse.sdkHttpResponse().isSuccessful();
	  }
	 
	protected boolean updateAllProducts(List<Product> productList) throws AwsServiceException,
    SdkClientException, S3Exception {
		Product [] products = (Product[]) productList.toArray(new Product[productList.size()]);
		return updateAllProducts(products);
	}
}
