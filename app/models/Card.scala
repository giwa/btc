package models

case class Card(cardID: String, message: String, `type`: String, tags: List[String], metrics: Long, owner: String)
