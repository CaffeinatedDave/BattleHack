package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  case class Search(species: Symbol, age_min: Int, age_max: Int)

  def index = Action { implicit request =>
    val search = request.cookies.get("searchSpecies")
    if (search == None) {
      Ok(views.html.index("DOG OR CAT?"))
    } else {
      Ok("Searching for " + search)
    }
  }
  
  def review = Action { implicit request =>
    // Get cookies - show matches:
    Ok("Reviewing stuff!")
  }
  
  def profile(id: Int) = Action { 
	Ok("A profile for " + id.toString + "!")
  }
  
  def shelter(id: Int) = Action {
    Ok("A shelter profile for " + id.toString + "!")
  }
}