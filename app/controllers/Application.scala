package controllers

import play.api._
import play.api.mvc._
import models._

object Application extends Controller {

  private def getIdList(cookie: String, request: Request[AnyContent]): List[Long] = { 
    request.cookies.get(cookie) match {
      case Some(s) if (s.value != "") => s.value.split('|').map(_.toLong).toList
      case _ => List()
    }
  }
  
  def search(search: String) = Action {
    search match {
      case "X" => Redirect(routes.Application.index).discardingCookies(DiscardingCookie("searchString"), DiscardingCookie("wantPets"), DiscardingCookie("rejectPets"))
      case a => Redirect(routes.Application.index).withCookies(Cookie("searchString", a)) 
    }
  }
  
  def index() = Action { implicit request =>
    if (getIdList("wantPets", request).length >= 6) {Redirect(routes.Application.review).flashing("success" -> "You've found some pets you like - why not take a closer look at them?")}
    else request.cookies.get("searchString") match {
      case Some(c) if (c.value == "C") => Ok(views.html.vote(Pet.getRandomPet("C", getIdList("rejectPets", request) ::: getIdList("wantPets", request))))
      case Some(c) if (c.value == "D") => Ok(views.html.vote(Pet.getRandomPet("D", getIdList("rejectPets", request) ::: getIdList("wantPets", request))))
      case Some(c) => Ok(views.html.vote(Pet.getRandomPet("A", getIdList("rejectPets", request) ::: getIdList("wantPets", request))))
      case _ => Ok(views.html.index()).discardingCookies(DiscardingCookie("searchSpecies"))
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
    getIdList("wantPets", request) match {
      case Nil    => Redirect(routes.Application.index).flashing("error" -> "You haven't favourited any pets yet!")
      case h :: Nil => Redirect(routes.Application.profile(h))
      case l      => Ok(views.html.matches(Pet.getPetList(l)))
    }
  }

  def profile(id: Long) = Action { implicit request =>
    val pet = Pet.getById(id)
    Ok(views.html.profile(pet))
  }

  def shelter(id: Long) = Action { implicit request =>
    val shelter = Shelter.getById(id)
    Ok("A shelter profile for " + shelter.name + "!")
  }

  // Catch all action...
  def redirect(path: Any) = Action { implicit request =>
    Redirect(routes.Application.index).flashing("error" -> "Not found")
  }
  
}