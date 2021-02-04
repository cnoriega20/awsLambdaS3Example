package com.aws.lambda.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.lambda.domain.Product;
import com.aws.lambda.helpers.InventoryS3Helper;
import com.aws.lambda.http.domain.HttpProductResponse;
import com.aws.lambda.http.domain.HttpQueryStringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class LambdaFunctionHandler extends InventoryS3Helper implements RequestHandler<HttpQueryStringRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpQueryStringRequest request, Context context) {
        context.getLogger().log("Input: " + request +"\n");
        
       return processJsonData(request, context);
    }

	public HttpProductResponse processJsonData(HttpQueryStringRequest request, Context context) {
		
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
		return new HttpProductResponse(product);
	}
    
    private Product getProductById(Integer id, InputStream input, Context context) throws IOException {
    	
    	  	
    	Product product = new Product();
    	try {

			Product[] products = getAllProducts();
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
