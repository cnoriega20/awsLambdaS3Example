package com.aws.lambda.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

       Regions clientRegion = Regions.US_EAST_1;
       String  bucket ="handy-inventory-bucket";
       String key = "test.txt";
       S3Object s3Object = null;
       String textContent = "";
       
       try {
    	   AmazonS3 s3Client = AmazonS3ClientBuilder.standard().
    			   withRegion(clientRegion).
    			   build();
    	   context.getLogger().log("Downloading an object");  
    	   
    	   // Get an object and print its contents.
    	   s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
    	   context.getLogger().log("Content-Type" + s3Object.getObjectMetadata().getContentType());
    	   context.getLogger().log("Content: ");
    	   
    	   // Get an entire object, overriding the specified response headers, and print the object's content.
    	   textContent = displayS3TextFile(s3Object.getObjectContent(), context);
    	   context.getLogger().log("Line from text file: " + textContent); 
    	   
		} catch (Exception e) {
			// TODO: handle exception
		}
        return textContent;
    }
    
    private static String displayS3TextFile(InputStream input, Context context) {
    	String outputStr = null;
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			outputStr = br.readLine();
			br.close();
		} catch (IOException e) {
			context.getLogger().log("Error: " + e.getMessage());
			e.printStackTrace();
		}
    	return outputStr;
    }

}
