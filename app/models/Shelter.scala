package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.SQLException
import play.api.Logger
import scala.util.parsing.combinator._
import controllers.routes

case class Shelter(id: Long, name: String)

object Shelter {
  val parse = {
    get[Long]("id") ~
    get[String]("name") map {
      case (i~n) => Shelter(i, n) 
    }
  }

  val dummy = Shelter(-1, "DUMMY DATA - SOMETHING'S BROKEN!")

  def getById(id: Long): Option[Shelter] = {
    DB.withConnection { implicit c =>
      SQL(
        """ 
          select id, name from tShelter where id = {id}
        """).on('id -> id).as(parse.singleOpt)
    }   
  }
}