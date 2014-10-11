package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {

  case class Search(species: Symbol, age_min: Int, age_max: Int)

  private def getIdList(cookie: String, request: Request[AnyContent]): List[Long] = { 
    request.cookies.get(cookie) match {
      case Some(s) if (s.value != "") => s.value.split('|').map(_.toLong).toList
      case _ => List()
    }
  }
  
  def search(search: String) = Action {
    search match {
      case "X" => Redirect(routes.Application.index).discardingCookies(DiscardingCookie("searchSpecies"))
      case a => Redirect(routes.Application.index).withCookies(Cookie("searchSpecies", a)) 
    }
  }
  
  def index() = Action { implicit request =>
    request.cookies.get("searchSpecies") match {
      case None    => Ok(views.html.index("CAT OR DOG?"))
      case Some(c) => Ok(views.html.vote(Pet.getRandomPet(getIdList("rejectPets", request))))
    }
  }

  def vote(id: Long, vote: String) = Action { implicit request =>
    val list = vote match {
      case "Y" => getIdList("wantPets", request).toSet
      case "N" => getIdList("rejectPets", request).toSet
      case _ => Set[Long]()
    }
    val newList = (list + id).toList
    val newCookie = (vote) match {
      case "Y"  => Cookie("wantPets", newList.mkString("|"))
      case "N"  => Cookie("rejectPets", newList.mkString("|"))
      case _    => Cookie("", "")
    }
    Redirect(routes.Application.index).withCookies(newCookie)
  }

  def review = Action { implicit request =>
    // Get cookies - show matches:
    request.cookies.get("matches") match {
      case None    => Redirect(routes.Application.index).flashing("error" -> "You haven't favourited any pets yet!")
      case Some(c) => Ok("Reviewing: " + getIdList("wantPets", request))
    }
  }

  def profile(id: Long) = Action { implicit request =>
    val pet = Pet.getById(id)
    Ok(views.html.profile(pet))
  }

  def shelter(id: Long) = Action { implicit request =>
    val shelter = Shelter.getById(id).getOrElse(Shelter.dummy)
    Ok("A shelter profile for " + shelter.name + "!")
  }

  // Catch all action...
  def redirect(path: Any) = Action { implicit request =>
    Redirect(routes.Application.index).flashing("error" -> "Not found")
  }
  
}