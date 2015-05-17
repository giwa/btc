package models

case class Card(cardId: String, message: String, `type`: String, tags: List[String], metrics: Long, owner: String)
