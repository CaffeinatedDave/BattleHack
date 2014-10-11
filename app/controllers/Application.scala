package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok("DOG OR CAT?")
  }
  
  def review = Action { implicit request =>
    // Get cookies - show matches:
    Ok("Reviewing stuff!")
  }
  
  def profile(id: Int) = Action { 
	Ok("A profile for " + id.toString + "!")
  }
}