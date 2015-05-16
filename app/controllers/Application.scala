package controllers

import models.CardDAO
import models.Card

import play.api.mvc._
import play.api.libs.json._


object Application extends Controller {
  val card_dao = new CardDAO()
  implicit val cardFormat = Json.format[Card]

  def index = Action {
    val card = card_dao.getCard
    Ok(Json.obj("result" -> true, "data" -> card))
  }

  def getCardByID(cardID: String) = Action {
    val card = card_dao.getByID(cardID)
    Ok(Json.obj("result" -> true, "data" -> card))
  }

}
