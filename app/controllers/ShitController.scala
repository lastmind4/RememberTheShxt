package controllers

import javax.inject._

import play.api.mvc._
import play.libs.Json
import services.ShitService

@Singleton
class ShitController @Inject() extends Controller {
  def getShits() = Action {
    Ok
  }

  def getShit(id: String) = Action {
    ShitService.getShit(id) match {
      case None => NotFound("error")
      case Some(box) => Ok(box.name)
    }
  }

  //  def addShit(name: String, comment: String)
  //  def removeShit(name: String, comment: String)
}