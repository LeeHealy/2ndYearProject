package models;

import java.util.*;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.*;
import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Product extends Model {
	
	@Id
	public Long id;
	
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String description;
	
	@Constraints.Required
	public String category;
	
	@Constraints.Required
	public int stock;
	
	@Constraints.Required
	public double price;
	
	//Default Constructor
	public Product() {
	
	}
	
	//Constructor to initialise object
	public Product(Long id, String name, String description, String 
category, int stock, double price){
	this.id = id;
	this.name = name;
	this.description = description;
	this.category = category;
	this.stock = stock;
	this.price = price;
	}

	public static Model.Finder<Long,Product> find = new
Model.Finder<Long,Product>(Long.class, Product.class);

	public static List<Product> findAll(){
		return Product.find.all();
	}
}


