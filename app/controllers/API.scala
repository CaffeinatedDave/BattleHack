package controllers

import play.api._
import play.api.mvc._
import java.io.File
import models._
import play.api.data._
import play.api.data.Forms._
import views.html.defaultpages.badRequest

object API extends Controller {
  
  def addPet = Action(parse.multipartFormData) { implicit request =>
    val uploadForm = Form(tuple(
      "name" -> text.verifying("Name is too long", _.length < 64),
      "age"  -> number,
      "profile" -> text,
      "species" -> text,
      "shelter" -> number,
      "image" -> ignored(request.body.file("image")).verifying("File missing", _.isDefined)
    ))

    uploadForm.bindFromRequest.fold(
      formWithErrors => BadRequest("Binding Error"),
      f => {
        request.body.file("image").map { image =>
          val timestamp = System.currentTimeMillis
          val ext = image.filename.dropWhile(c => c != '.')
          image.ref.moveTo(new File("public/uploads/images/" + timestamp + ext))
          Pet.create(f._1, f._2, timestamp + ext, f._3, f._4, f._5)
          Ok("")
        }.getOrElse {
          BadRequest("Image not uploaded")
        }
      }
    )
  }
  
  def addPetForm = Action {
    Ok(views.html.addPet())
  }

  def addShelter = Action { implicit request =>
    Ok("Not implemented")
  }
  def addShelterForm = Action { implicit request =>
    Ok("Not implemented")
  }

}