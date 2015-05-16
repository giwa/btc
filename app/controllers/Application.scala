package controllers

import models.CardDAO
import play.api.mvc._


object Application extends Controller {
  val card_dao = new CardDAO()

  def index = Action {
    Ok(card_dao.getCard)
  }

}
