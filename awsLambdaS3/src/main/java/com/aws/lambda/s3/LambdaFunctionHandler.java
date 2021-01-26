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
import com.aws.lambda.domain.HttpQueryStringRequest;
import com.aws.lambda.domain.Product;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class LambdaFunctionHandler implements RequestHandler<HttpQueryStringRequest, String> {

    @Override
    public String handleRequest(HttpQueryStringRequest request, Context context) {
        context.getLogger().log("Input: " + request +"\n");
        
       return processJsonData(request, context);
    }

	public String processJsonData(HttpQueryStringRequest request, Context context) {
		
		Regions clientRegion = Regions.US_EAST_1;
		String bucket = "handy-inventory-bucket";
		String key = "handy-tool-catalog.json";
		S3Object s3Object = null;
		Product product = new Product();

		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
			context.getLogger().log("Downloading an object \n");

			// Get an object and print its contents.
			s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
			context.getLogger().log("Content-Type: " + s3Object.getObjectMetadata().getContentType() +"\n");
			context.getLogger().log("Content: \n");
			
			
			context.getLogger().log("Getting the id from Map...");
			String strId = (String)request.getQueryStringParameters().get("id");
			context.getLogger().log("String id: " + strId);
			
			Integer prodId = Integer.parseInt(strId);
			context.getLogger().log("Prod id: " + prodId);

			// Invoking getProductById method.
			context.getLogger().log("Invoking getProductById method");
			
			product = getProductById(prodId, s3Object.getObjectContent(), context);
			context.getLogger().log("Product: " + product.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return product.toString();
	}
    
    private Product getProductById(Integer id, InputStream input, Context context) throws IOException {
    	
    	Gson gson = new Gson();
    	Product[] products = null;
    	//Optional<Product> finalProduct = Optional.empty();
    	Product product = new Product();
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			//Marshall Json data to an array of Products.
			
			products = gson.fromJson(br, Product[].class);
			List<Product> productList = Arrays.asList(products);
			
			product = productList.stream().
				    filter(prod -> prod.getId() == id).
				    findFirst()
				    .orElseGet(Product::new );		
			
		} catch (JsonIOException e) {
			context.getLogger().log("Error: " + e.getMessage());
			e.printStackTrace();
		}catch(JsonSyntaxException ex) {
			context.getLogger().log("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
    	return product;
    }

}
