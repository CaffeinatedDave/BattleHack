package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.SQLException
import play.api.Logger
import scala.util.parsing.combinator._
import controllers.routes

case class Pet(id: Long, name: String, age: Int, image: String, profile: String, species: Symbol, shelter: Shelter)

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
    get[Int]("age") ~
    get[String]("image") ~ 
    get[String]("profile") ~
    get[String]("species") ~
    get[Long]("shelter") map {
      case (i~n~a~im~p~s~sh) => Pet(i, n, a, im, p, getSpecies(s), Shelter.getById(sh).getOrElse(Shelter.dummy))
    }
  }

  val dummy = Pet(1, "Fido", 2, "1413042069069.jpg", "This is a pet, please adopt it!", 'Moose, Shelter.getById(-1).getOrElse(Shelter.dummy))

  
  def getById(id: Long): Pet = {
    DB.withConnection { implicit c => 
      SQL(""" 
        select * from tPet where id = {id}
      """).on('id -> id).as(parse.singleOpt).getOrElse(Pet.dummy)
    }
  }
  
  def getRandomPet(reject: List[Long]): Pet = {
    Pet.dummy
  }

  def getPetList(wanted: List[Long]): List[Pet] = {
    List(getRandomPet(List()))
  }
  
  def create(name: String, age: Int, image: String, profile: String, species: String, shelter: Long) {
    DB.withConnection { implicit c =>
      SQL("""
        insert into tPet (name, image, age, profile, species, shelter_id)
        values ({name}, {image}, {age}, {profile}, {species}, 1) 
      """).on(
        'name -> name,
        'age -> age,
        'image -> image,
        'profile -> profile,
        'species -> species
      ).executeInsert()
    }
  }
}