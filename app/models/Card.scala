package models

import play.api.Logger

import scala.io.Source

case class Card(cardID: String, message: String, `type`: String, tags: List[String], metrics: Long, owner: String)

class CardDAO {
  private val hw = {
    Logger.info("hjogehoge")

    "Hello world"
  }

  private val cards_str = {
    List("CIDhoge\tMESSAGEghoe\tTYPE\tTag1,Tag2\t1234556\tOWNERhoge",
      "CID3\tMESSAGEghoe\tTYPE\tTag1,Tag2\t1234556\tOWNERhoge")
  }

  private val cardFromFile
    = for(line <- Source.fromFile("/opt/btc/joined_card.tsv").getLines) yield line

  private val cards: Map[String, Card]= {
    val b = Map.newBuilder[String, Card]
    for (c <- cardFromFile) {
      val a = c.trim.split('\t')
      b += a(0) -> Card(a(0), a(1), a(2), a(3).split(',').toList, a(4).toLong, a(5))
    }

    b.result()
  }

  def getCard: String = {
    cards("Cdfjqzx8")
  }

  def getByID(cardID: String): Card = {
    cards(cardID)
  }

}
