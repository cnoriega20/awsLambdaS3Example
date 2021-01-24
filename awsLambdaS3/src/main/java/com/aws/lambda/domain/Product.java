package com.aws.lambda.domain;

public class Product {

	private Integer id;
	private String toolType;
	private String brand;
	private String name;
	private Integer count;
	
	public Product() {}
	
	public Product(Integer id, String toolType, String brand, String name, Integer count) {
		
		this.id = id;
		this.toolType = toolType;
		this.brand = brand;
		this.name = name;
		this.count = count;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToolType() {
		return toolType;
	}
	public void setToolType(String toolType) {
		this.toolType = toolType;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", toolType=" + toolType + ", brand=" + brand + ", name=" + name + ", count="
				+ count + "]";
	}
	
	
	
}
