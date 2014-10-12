package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.SQLException
import play.api.Logger
import scala.util.parsing.combinator._
import controllers.routes

case class Address(lines: List[String])
case class Shelter(id: Long, name: String, addr: Address, phone: String, email: String, info: String, paypal:String)

object Shelter {
  val parse = {
    get[Long]("id") ~
    get[String]("name") ~
    get[String]("addr_1") ~
    get[String]("addr_2") ~
    get[String]("addr_3") ~
    get[String]("addr_4") ~
    get[String]("contact_no") ~
    get[String]("contact_email") ~
    get[String]("info") ~ 
    get[String]("paypal") map {
      case (i~n~a1~a2~a3~a4~p~e~in~pp) => Shelter(i, n, Address(List(a1, a2, a3, a4)), p, e, in, pp) 
    }
  }

  val dummy = Shelter(-1, "Shelter", Address(List("Made up", "Data", "Because", "It broke")), "0203 123 4567", "broken@example.com", "DUMMY DATA - SOMETHING'S BROKEN!", "dave@caffeinateddave.com")

  def getById(id: Long): Shelter = {
    DB.withConnection { implicit c =>
      SQL(
        """ 
          select * from tShelter where id = {id}
        """).on('id -> id).as(parse.singleOpt).getOrElse(dummy)
    }   
  }
}