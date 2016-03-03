package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;

import java.util.ArrayList;
import java.util.List;


import views.html.*;

//Import models
import models.*;

public class Application extends Controller {
	
	public Result index(){
		return ok(index.render("",User.getLoggedIn(session().get("email"))));
	}
	
	public Result items(){
		
		//Get the list of products
		List<Product> products = Product.findAll();
		
		//Pass the list to the view and render
		return ok(items.render(products, User.getLoggedIn(session().get("email"))));
    }
	
	public Result about() {
		return ok(about.render("",User.getLoggedIn(session().get("email"))));
	}

	public Result contactUs() {
		return ok(contactUs.render("",User.getLoggedIn(session().get("email"))));
	}
    
	@Security.Authenticated(Secured.class)
	public Result addProduct() {
		// Instantiate a form object based on the Product class
		Form<Product> addProductForm = Form.form(Product.class);
		
		// Render the Add Product View
		return ok(addProduct.render(addProductForm, User.getLoggedIn(session().get("email"))));
	}
	
    @Security.Authenticated(Secured.class)
	public Result addProductSubmit() {
		
		// Create a product form object (to hold submitted data)
		// 'Bind' the object to the submitted form (this copies the filled form)
		Form<Product> newProductForm = Form.form(Product.class).bindFromRequest();
		
			// Check for errors (based on Product class annotations)
			if(newProductForm.hasErrors()){
				// Display the form again
				return badRequest(addProduct.render(newProductForm, User.getLoggedIn(session().get("email"))));
			}
			
			// Save the product using Ebean (remember Product extends Model)
			newProductForm.get().save();
			
			// Store a success message in the flash session
			flash("success", "Product " + newProductForm.get().name + " has been created");
		
		
		// Redirect to the home page
		return redirect("/");
	}
		
	// Delete Product
    @Security.Authenticated(Secured.class)
	public Result deleteProduct(Long id) {
		// Call delete method
		Product.find.ref(id).delete();
		
		// Add message to flash session
		flash("success", "Product has been deleted");
		
		// Redirect Home
		return redirect("/");
	}	
	
	// Update a product by ID
	// Called when edit button is pressed
    @Security.Authenticated(Secured.class)
	public Result updateProduct(Long id) {
		
		// Create a form based on the Product class
		// Fill the form with product object matching id
		// Use the finder to find the object by ID in the DB
		Form<Product> productForm = Form.form(Product.class).fill(Product.find.byId(id));
		
		// Render the updateProduct view
		// Pass the id and form as parameters
		return ok(updateProduct.render(id, productForm, User.getLoggedIn(session().get("email"))));
	}
	
	// Handle the form data when an updated product is submitted
    @Security.Authenticated(Secured.class)
	public Result updateProductSubmit(Long id) {
		
		// Create a product form object (to hold submitted data)
		// 'Bind' the object to the submitted form (this copies the filled form)
		Form<Product> updateProductForm = Form.form(Product.class).bindFromRequest();
		
		// Check for errors (based on Product Class annotations)
		if(updateProductForm.hasErrors()) {
			//Display the form again
			return badRequest(updateProduct.render(id, updateProductForm, User.getLoggedIn(session().get("email"))));
		}
		// Update the Product (using its ID to select the existing object)
		Product p = updateProductForm.get();
		p.id = id;
		p.update();
		
		//Add a success message to the flash session
		flash("success", "Product " + updateProductForm.get().name + " has been updated");
		
		// Render the Add Product view, passing the form object
		return redirect("/");
		
	}
	
	public Result welcome(String name) {
		return ok(welcome.render(name));
	}
    
    //Add Login to Application
    public Result login(){
        //pass a login form to the login view and render
        return ok(login.render(Form.form(Login.class), User.getLoggedIn(session().get("email"))));
    }
    
    public Result authenticate(){
        //Bind form instance to the values submitted from the form
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        
        //Check for errors
        //Uses the validate method defined in the login class
        if(loginForm.hasErrors()){
            return badRequest(login.render(Form.form(Login.class), User.getLoggedIn(session().get("email"))));
        }
        else{
            //User logged in successfully
            //Clear the existing session
            session().clear();
            //Store the logged in email in the session
            session("email", loginForm.get().email);
            //Return to the home page
            return redirect(routes.Application.index());                     
        }
        
    }
    
    public Result logout(){
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.login());
	}
	
		
}
	

