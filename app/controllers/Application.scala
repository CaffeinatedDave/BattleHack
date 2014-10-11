package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {
  
  case class Search(species: Symbol, age_min: Int, age_max: Int)

  def search(search: String) = Action {
    search match {
      case "X" => Redirect(routes.Application.index).discardingCookies(DiscardingCookie("searchSpecies"))
      case a => Redirect(routes.Application.index).withCookies(Cookie("searchSpecies", a)) 
    }
  }
  
  def index() = Action { implicit request =>
    request.cookies.get("searchSpecies") match {
      case None    => Ok(views.html.index("CAT OR DOG?"))
      case Some(c) => Ok(views.html.vote(Pet.getRandomPet(List())))
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
    Ok(views.html.profile(pet))
  }

  def shelter(id: Long) = Action {
    val shelter = Shelter.getById(id).getOrElse(Shelter.dummy)
    Ok("A shelter profile for " + shelter.name + "!")
  }

  // Catch all action...
  def redirect(path: Any) = Action {
    Redirect(routes.Application.index).flashing("error" -> "No found")
  }
  
}