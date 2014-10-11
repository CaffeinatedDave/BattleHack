package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {
  
  case class Search(species: Symbol, age_min: Int, age_max: Int)

  def index = Action { implicit request =>
    request.cookies.get("searchSpecies") match {
      case None    => Ok(views.html.index("DOG OR CAT?"))
      case Some(c) => Ok("Searching for " + c.value)
    }
  }

  def review = Action { implicit request =>
    // Get cookies - show matches:
    request.cookies.get("matches") match {
      case None    => Redirect(routes.Application.index).flashing("error" -> "You haven't favourited any pets yet!")
      case Some(c) => Ok("Reviewing stuff!")
    }
  }

  def profile(id: Long) = Action {
    val pet = Pet.getById(id)
    Ok("A profile for " + pet.name + "!")
  }

  def shelter(id: Long) = Action {
    val shelter = Shelter.getById(id)
    Ok("A shelter profile for " + shelter.name + "!")
  }

  // Catch all action...
  def redirect(path: Any) = Action {
    Redirect(routes.Application.index).flashing("error" -> "No found")
  }
  
}