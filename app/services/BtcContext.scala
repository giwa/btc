package services

import models.Card

import scala.collection.mutable
import scala.io.Source

class BtcContext {

  // private val cardFromFile
  // = for(line <- Source.fromFile("/opt/btc/joined_card.tsv").getLines) yield line

  private val cardFromBoxFile =
    for(line <- Source.fromFile("/opt/btc/joined_card_only_box.tsv").getLines) yield line

  // val cards: mutable.HashMap[String, Card] = {
  //   cardConstructor(cardFromFile)
  // }

  val cardsInBoxs: mutable.HashMap[String, Card] = {
    cardConstructor(cardFromBoxFile)
  }

  private def cardConstructor(it: Iterator[String]): mutable.HashMap[String, Card] = {
    val b = new mutable.HashMap[String, Card]
    for (line <- it) {
      val a = line.trim.split('\t')
      b += a(0) -> Card(a(0), a(1), a(2), a(3).split(',').toList, a(4).toLong, a(5))
    }

    b
  }
}
