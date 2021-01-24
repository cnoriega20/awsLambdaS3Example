package com.aws.lambda.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.lambda.domain.Product;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

       return processJsonData(context);
    }

	public String processJsonData(Context context) {
		Regions clientRegion = Regions.US_EAST_1;
		   String  bucket ="handy-inventory-bucket";
		   String key = "handy-tool-catalog.json";
		   S3Object s3Object = null;
		   String textContent = "";
		   Product product = null;
		   
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
			   //textContent = displayS3JsonString(s3Object.getObjectContent(), context);
			   product = getProductById(102, s3Object.getObjectContent(), context);
			   context.getLogger().log("Line from text file: " + textContent); 
			   
			} catch (Exception e) {
				// TODO: handle exception
			}
		    return product.toString();
	}
    
    private Product getProductById(Integer id, InputStream input, Context context) throws IOException {
    	String outputStr = null;
    	Gson gson = new Gson();
    	Product[] products = null;
    	Optional<Product> finalProduct = null;
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			//Marshall Json data to an array of Products.
			
			products = gson.fromJson(br, Product[].class);
			List<Product> productList = Arrays.asList(products);
			finalProduct = productList.stream().
				    filter(prod -> prod.getId() == id).
				    findFirst();		
			
		} catch (JsonIOException e) {
			context.getLogger().log("Error: " + e.getMessage());
			e.printStackTrace();
		}catch(JsonSyntaxException ex) {
			context.getLogger().log("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
    	return finalProduct.get();
    }

}
