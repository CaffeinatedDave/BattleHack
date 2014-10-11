package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.SQLException
import play.api.Logger
import scala.util.parsing.combinator._
import controllers.routes

case class Pet(id: Long, name: String, image: String, profile: String, species: Symbol, shelter: Shelter)

object Pet {
  def getSpecies(name: String): Symbol = {
    name match {
      case "D" => 'Dog
      case "C" => 'Cat
      case _   => 'Moose
    }
  }
  
  val parse = {
    get[Long]("id") ~
    get[String]("name") ~
    get[String]("image") ~ 
    get[String]("profile") ~
    get[String]("species") ~
    get[Long]("shelter") map {
      case (i~n~im~p~s~sh) => Pet(i, n, im, p, getSpecies(s), Shelter.getById(sh))
    }
  }

  def getById(id: Long): Pet = {
    Pet(id, "Fido v" + id.toString, "image", "This is a pet, please adopt it!", 'Moose, Shelter.getById(1))
  }
  
  def getRandomPet(reject: List[Long]): Pet = {
    Pet(1, "Fido", "image", "This is a pet, please adopt it!", 'Moose, Shelter.getById(1))
  }

  def getPetList(wanted: List[Long]): List[Pet] = {
    List(getRandomPet(List()))
  }
}