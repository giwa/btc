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

  def listGetCardInBox(
                        limit: Int,
                        findByBoxIdEqual: String,
                        findByBoxCategoryEqual: String,
                        findByBoxPriorityGTE: Long,
                        findByBoxPriorityLTE: Long,
                        findByCardTagsIncludeAll: List[String],
                        findByCardTagsIncludeAny: List[String],
                        findByCardMetricsGTE: Long,
                        findByCardMetricsLTE: Long
                        ) = Action {
    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)

    if (findByBoxIdEqual != "") {
      rd = rd.findByBoxIdEqual(findByBoxIdEqual)
    }

    if (findByBoxCategoryEqual != "") {
      rd = rd.findByBoxCategoryEqual(findByBoxCategoryEqual)
    }

    if (findByBoxPriorityGTE > 0) {
      rd = rd.findByBoxPriorityGTE(findByBoxPriorityGTE)
    }

    if (findByBoxPriorityLTE > 0) {
      rd = rd.findByBoxPriorityLTE(findByBoxPriorityLTE)
    }

    if (findByCardTagsIncludeAll != Nil) {
      rd = rd.findByCardTagsIncludeAll(findByCardTagsIncludeAll)
    }

    if (findByCardTagsIncludeAny != Nil) {
      rd = rd.findByCardTagsIncludeAny(findByCardTagsIncludeAny)
    }

    if (findByCardMetricsGTE > 0) {
      rd = rd.findByCardMetricsGTE(findByCardMetricsGTE)
    }

    if (findByCardMetricsLTE > 0) {
      rd = rd.findByCardMetricsGTE(findByCardMetricsLTE)
    }

    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(limit)))
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

  def getCardInBox = Action  { implicit request =>
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

    if (params.exists(_ == "findByBoxPriorityLTE")) {
      rd = rd.findByBoxPriorityLTE(params("findByBoxPriorityLTE").toLong)
    }

    if (params.exists(_ == "findByCardTagsIncludeAll")) {
      rd = rd.findByCardTagsIncludeAll(params("findByCardTagsIncludeAll").split(',').toList)
    }

    if (params.exists(_ == "findByCardTagsIncludeAny")) {
      rd = rd.findByCardTagsIncludeAll(params("findByCardTagsIncludeAny").split(',').toList)
    }

    if (params.exists(_ == "findByCardMetricsGTE")) {
      rd = rd.findByCardMetricsGTE(params("findByCardMetricsGTE").toLong)
    }
    if (params.exists(_ == "findByCardMetricsGTE")) {
      rd = rd.findByCardMetricsGTE(params("findByCardMetricsGTE").toLong)
    }

    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(params("limit").toInt)))
  }

//  def getCardsInBox(limit: Int, findByBoxCategoryEqual: String): Unit = {
//    var rd = new RD(bc, bc.cardsInBoxs, btcIndex)
//    rd = rd.findByBoxCategoryEqual(findByBoxCategoryEqual)
//    Ok(Json.obj("result" -> true, "data" -> rd.getCardList(limit)))
//  }
}
