package entity

import play.api.libs.json.Json

case class Shit(id: Long, name: String, comment: String)

object Shit {
  implicit val shitFormat = Json.format[Shit]
}
