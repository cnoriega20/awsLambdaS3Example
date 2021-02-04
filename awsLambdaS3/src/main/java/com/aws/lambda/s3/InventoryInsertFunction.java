package com.aws.lambda.s3;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.lambda.domain.Product;
import com.aws.lambda.helpers.InventoryS3Helper;
import com.aws.lambda.http.domain.HttpProductResponse;
import com.aws.lambda.http.domain.HttpRequest;
import com.google.gson.Gson;

public class InventoryInsertFunction extends InventoryS3Helper
	implements RequestHandler<HttpRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Request: " + request);

        String body = request.getBody();
        
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(body, Product.class);
        
        List<Product> products = getAllProductsList();
        products.add(newProduct);
        
        if(updateAllProducts(products)) {
        	return new HttpProductResponse();
        }
      
        HttpProductResponse response = new HttpProductResponse();
        response.setStatusCode("500");
        return response;
    }
}
