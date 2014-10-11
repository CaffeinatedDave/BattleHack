package controllers

import play.api._
import play.api.mvc._
import java.io.File
import models._
import play.api.data._
import play.api.data.Forms._
import views.html.defaultpages.badRequest
import fly.play.s3._
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.BodyParsers.parse.Multipart.PartHandler
import play.api.mvc.BodyParsers.parse.Multipart.handleFilePart
import play.api.mvc.BodyParsers.parse.Multipart.FileInfo
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.iteratee.Iteratee

object API extends Controller {
private def myHandleFilePart: PartHandler[FilePart[Array[Byte]]] = {
  handleFilePart {
    case FileInfo(partName, filename, contentType) =>
      val baos = new java.io.ByteArrayOutputStream()
      Iteratee.fold[Array[Byte], java.io.ByteArrayOutputStream](baos) {
        (os, data) =>
          os.write(data)
          os
      }.mapDone {
        os =>
          os.close()
          baos.toByteArray
      }
  }
}

def sendToBucket(image: Array[Byte]): String = {
	val timestamp = System.currentTimeMillis
    val ext = ".jpg"
    val filename = timestamp.toString + ext
    val bucket = S3("fluffypets")

    val result = bucket add BucketFile(filename, "image/jpeg", image)
        result.map { unit =>
            Logger.info("Saved the file")
        }

    timestamp.toString + ext
}

  
  def addPet = Action(parse.multipartFormData(myHandleFilePart)) { implicit request =>
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
        request.body.file("image").map(file => {
        	val filename = sendToBucket(file.ref)
        	Pet.create(f._1, f._2, filename, f._3, f._4, f._5)
          })
        /*request.body.file("image").map { image =>
          val timestamp = System.currentTimeMillis
          val ext = image.filename.dropWhile(c => c != '.')
          image.ref.moveTo(new File("public/uploads/images/" + timestamp + ext))
          Pet.create(f._1, f._2, timestamp + ext, f._3, f._4, f._5)
          Ok("")
        }.getOrElse {
          BadRequest("Image not uploaded")
        }*/
        Ok("It probably worked!")
      }
    )
  }

  def addPetForm = Action { implicit request =>
    Ok(views.html.addPet())
  }

  def addShelter = Action { implicit request =>
    Ok("Not implemented")
  }
  def addShelterForm = Action { implicit request =>
    Ok("Not implemented")
  }

}