package controllers

import models.{BtcIndex, Card, RD}
import play.api.libs.json._
import play.api.mvc._
import services.BtcContext

object Application extends Controller {
  val bc = new BtcContext()
  val btcIndex = new BtcIndex()
  implicit val cardFormat = Json.format[Card]

  def index = Action {
    // val rd = new RD(bc, bc.cards, btcIndex)
    Ok(Json.obj("result" -> true, "data" -> "ok"))
  }

  // def getCardByID(cardID: String) = Action {
  //   val rd = new RD(bc, bc.cards, btcIndex)
  //   Ok(Json.obj("result" -> true, "data" -> rd.getByCardID(cardID)))
  // }

  def getCardsInBox(
       limit: Int,
       findByIdEqual: Option[String],
       findByBoxCategoryEqual: Option[String],
       findByBoxPriorityGTE: Option[String],
       findByBoxPriorityLTE: Option[String],
       findByCardTagsIncludeAll: Option[String],
       findByCardTagsIncludeAny: Option[String],
       findByCardMetricsGTE: Option[String],
       findByCardMetricsLTE: Option[String]) = Action {

    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)
    if (findByIdEqual.exists(_.trim.nonEmpty)) {
      rd = rd.findByBoxIdEqual(findByIdEqual.toString)
    }
    if (findByBoxCategoryEqual.exists(_.trim.nonEmpty)) {
      rd = rd.findByBoxCategoryEqual(findByBoxCategoryEqual.toString)
    }
    if (findByBoxPriorityGTE.exists(_.trim.nonEmpty)) {
      rd = rd.findByBoxPriorityGTE(findByBoxPriorityGTE.toString.toLong)
    }
    if (findByBoxPriorityLTE.exists(_.trim.nonEmpty)) {
      rd = rd.findByBoxPriorityLTE(findByBoxPriorityLTE.toString.toLong)
    }
    if (findByCardTagsIncludeAll.exists(_.trim.nonEmpty)) {
      rd = rd.findByCardTagsIncludeAll(findByCardTagsIncludeAll.toString.split(',').toList)
    }
    if (findByCardTagsIncludeAny.exists(_.trim.nonEmpty)) {
      rd = rd.findByCardTagsIncludeAny(findByCardTagsIncludeAny.toString.split(',').toList)
    }
    if (findByCardMetricsGTE.exists(_.trim.nonEmpty)) {
      rd = rd.findByCardMetricsGTE(findByCardMetricsGTE.toString.toLong)
    }
    if (findByCardMetricsLTE.exists(_.trim.nonEmpty)) {
      rd = rd.findByCardMetricsLTE(findByCardMetricsLTE.toString.toLong)
    }

    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(limit.toString.toInt)))
  }

  def is_empty(param: Option[String]): Boolean = {
    param.exists(_.trim.nonEmpty)
  }

  def getCardsInBox(
                     limit: Int,
                     findByBoxIdEqual: String) = Action {
    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)
    rd = rd.findByBoxIdEqual(findByBoxIdEqual.toString)
    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(limit)))
  }

  def getCardInBoxT = Action  { implicit request =>
    val params = request.queryString.map { case (k,v) => k -> v.mkString }

    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)
    if (params.exists(_ == "findByBoxIdEqual")) {
      rd = rd.findByBoxIdEqual(params("findByBoxIdEqual"))
    }
    if (params.exists(_ == "findByBoxCategoryEqual")) {
      rd = rd.findByBoxCategoryEqual(params("findByBoxCategoryEqual"))
    }
    if (params.exists(_ == "findByBoxPriorityGTE")) {
      rd = rd.findByBoxPriorityGTE(params("findByBoxPriorityGTE").toLong)
    }


    Ok(Json.obj("result" -> true, "data" -> params("findByBoxIdEqual")))
  }

//  def getCardsInBox(limit: Int, findByBoxCategoryEqual: String): Unit = {
//    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)
//    rd = rd.findByBoxCategoryEqual(findByBoxCategoryEqual)
//    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(limit)))
//  }
}
