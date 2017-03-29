package entity

import play.api.libs.json.Json

case class Login(id: Long, name: String, password: String)

object Login {
  implicit val loginFormat = Json.format[Login]
}

