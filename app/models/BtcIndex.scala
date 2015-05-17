package models

import scala.collection.mutable
import scala.io.Source

class BtcIndex {
  private val tagIndexFile
    = for(line <- Source.fromFile("/opt/btc/tag_index.tsv").getLines) yield line

  private val box2CardListIndexFile
    = for(line <- Source.fromFile("/opt/btc/box2card_list.tsv").getLines) yield line

  private val boxCategory2CardListIndexFile
    = for(line <- Source.fromFile("/opt/btc/boxcategory2card.tsv").getLines) yield line

  private val boxPriority2CardListIndexFile
    = for(line <- Source.fromFile("/opt/btc/priority2card.tsv").getLines) yield line

  val tagIndex: mutable.HashMap[String, Set[String]] = {
    keySetConstructor(tagIndexFile)
  }

  val box2CardListIndex: mutable.HashMap[String, Set[String]] = {
    keySetConstructor(box2CardListIndexFile)
  }

  val boxCategory2CardListIndex: mutable.HashMap[String, Set[String]] = {
    keySetConstructor(boxCategory2CardListIndexFile)
  }

  val boxPriority2CardListIndex: mutable.HashMap[Long, Set[String]] = {
    keyLongSetConstructor(boxPriority2CardListIndexFile)
  }

  def getCardIDByAllTags(tags: List[String]) = {
    val hm = new mutable.HashMap[String, Set[String]]
    var cardIDs = Set[String]()
    tags.foreach(tag => hm += tag -> tagIndex(tag))
    hm.values.foreach(t => cardIDs = cardIDs & t)

    cardIDs
  }

  def getCardIDByAnyTags(tags: List[String]) = {
    val hm = new mutable.HashMap[String, Set[String]]
    var cardIDs = Set[String]()
    tags.foreach(tag => hm += tag -> tagIndex(tag))
    hm.values.foreach(t => cardIDs = cardIDs | t)

    cardIDs
  }

  def findByPriority(cmp: String, priority: Long): Set[String] = {
    // case would be cool
    // This is not cool
    if (cmp == "gt") {
      val keys = boxPriority2CardListIndex.keys.toList.filter(p => p > priority)
      if (keys != Nil){
        val r  = List[String]()
        for (key <- keys) {
          r ::: boxPriority2CardListIndex(key).toList
        }
        r.toSet
      }else{
        Set[String]()
      }
    }else{
      val keys = boxPriority2CardListIndex.keys.toList.filter(p => p < priority)
      if (keys != Nil){
        val r  = List[String]()
        for (key <- keys) {
          r ::: boxPriority2CardListIndex(key).toList
        }
        r.toSet
      }else{
        Set[String]()
      }
    }
  }

  private def keyLongSetConstructor(it: Iterator[String]): mutable.HashMap[Long, Set[String]] = {
    val hm = new mutable.HashMap[Long, Set[String]]
    for (line <- it) {
      val tag_index = line.trim.split('\t')
      hm += tag_index(0).toLong -> tag_index(1).split(',').toSet
    }

    hm
  }

  // naming is not good this is not index
  private def keySetConstructor(it: Iterator[String]): mutable.HashMap[String, Set[String]] = {
    val hm = new mutable.HashMap[String, Set[String]]
    for (line <- it) {
      val tag_index = line.trim.split('\t')
      hm += tag_index(0) -> tag_index(1).split(',').toSet
    }

    hm
  }
}
