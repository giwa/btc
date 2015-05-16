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

  private val cardFromFIle
    = for(line <- Source.fromFile("/opt/btc/test.tsv").getLines) yield line

  private val cards: Map[String, Card]= {
    val b = Map.newBuilder[String, Card]
    for (c <- cards_str) {
      Logger.info(c)
      val a = c.split('\t')
      b += a(0) -> Card(a(0), a(1), a(2), a(3).split(',').toList, a(4).toLong, a(5))
    }

    b.result()
  }

  def getCard: String = {
    cardFromFIle.foreach(Logger.info(_))
    cards("CID3").message
  }


}
