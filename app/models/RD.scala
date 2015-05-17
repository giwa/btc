package models

import services.BtcContext
import scala.collection.mutable

/*
 * Resident Dataset
 * Idea is close to Apache Spark RDD (Resident Distribute Dataset)
 * RD has immutable dataset and operator for the data
 */
class RD (
    private var _bc: BtcContext,
    private val _data: mutable.HashMap[String, Card],
    private val _index: BtcIndex){

  val btcContext = _bc
  val btc_index = _index
  val data = _data
  var _tags: List[String] = Nil

  def getByCardID(cardID: String): Card = _data(cardID)

  def getCardList(limit: Int): List[Card] = data.values.toList.take(limit)

  def findByBoxIdEqual(box_id: String): RD = {
    val cardIDs: Set[String] = _index.box2CardListIndex(box_id)
    val hm = new mutable.HashMap[String, Card]
    cardIDs.foreach(cardID => hm += cardID -> data(cardID))
    new RD(btcContext, hm, btc_index)
  }

  def findByBoxCategoryEqual(box_category: String): RD = {
    val cardIDs: Set[String] = _index.boxCategory2CardListIndex(box_category.toString)
    val hm = new mutable.HashMap[String, Card]
    cardIDs.foreach(cardID => hm += cardID -> data(cardID))
    new RD(btcContext, hm, btc_index)
  }

  def findByBoxPriorityGTE(priority: Long): RD = {
    val cardIDs: Set[String] = _index.findByPriority("gt", priority)
    val hm = new mutable.HashMap[String, Card]
    cardIDs.foreach(cardID => hm += cardID -> data(cardID))
    new RD(btcContext, hm, btc_index)
  }

  def findByBoxPriorityLTE(priority: Long): RD = {
    val cardIDs: Set[String] = _index.findByPriority("lt", priority)
    val hm = new mutable.HashMap[String, Card]
    cardIDs.foreach(cardID => hm += cardID -> data(cardID))
    new RD(btcContext, hm, btc_index)
  }

  def findByCardTagsIncludeAll(tags: List[String]): RD = {
    val cardIDs = _index.getCardIDByAllTags(tags)
    val hm = new mutable.HashMap[String, Card]
    cardIDs.foreach(cardID => hm += cardID -> data(cardID))
    new RD(btcContext, hm, btc_index)
  }

  def findByCardTagsIncludeAny(tags: List[String]): RD = {
    this._tags = tags
    val cardIDs = _index.getCardIDByAnyTags(tags)
    val hm = new mutable.HashMap[String, Card]
    for (card <- cardIDs) {
      if(data.exists(_ == card)){
        hm += card -> data(card)
      }
    }
    new RD(btcContext, hm, btc_index)
  }

  def findByCardMetricsGTE(metrics: Long): RD = {
    val hm = new mutable.HashMap[String, Card]
    for (r <- data.values) {
      if (r.metrics < metrics) {
        hm += r.cardId -> r
      }
    }
    new RD(btcContext, hm, btc_index)
  }

  def findByCardMetricsLTE(metrics: Long): RD = {
    val hm = new mutable.HashMap[String, Card]
    for (r <- data.values) {
      if (r.metrics > metrics) {
        hm += r.cardId -> r
      }
    }
    new RD(btcContext, hm, btc_index)
  }

  def findByCardTypeEqual(card_type: String): RD = {
    val cardIDs = _index.type2CardListIndex(card_type)
    val hm = new mutable.HashMap[String, Card]
    for (card <- cardIDs) {
      if (data.exists(_ == card)){
        hm += card -> data(card)
      }
    }
    new RD(btcContext, hm, btc_index)
  }
/*
  def sortByBoxCategory(ordered:String, limit:Int): SortedRD = {
    // need make index
    _index.boxCategory2CardListIndex
    data.values.toList.sortWith(_.c)
    data.toSeq.sortWith(_._2.category < _._2.category)
    data.toSeq.sortBy(_._2)

  }
  */
}

/*
class SortedRD(
    private var _bc: BtcContext,
    private val _data: List[Card],
    private var _index: BtcIndex){

  val btcContext = _bc
  val btc_index = _index
  val data = _data

  def sortByBoxCategory(orderBy: String): SortedRD = {

  }

  def sortByBoxPriority(orderBy: String): SortedRD = {

  }

  def sortByCardType(orderBy: String): SortedRD = {

  }

  def sortByCardMatchedTagNum(orderBy: String): SortedRD = {

  }

  def sortedByCardMetrics(orderBy: String): SortedRD = {

  }

  def limit(limit: Int) : SortedRD = {

  }
}
*/
