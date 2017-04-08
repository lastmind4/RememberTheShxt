package controllers

import javax.inject._

import dao.ShitDao
import entity.Shit
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class ShitController @Inject()(repo: ShitDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val shitForm: Form[ShitForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "comment" -> nonEmptyText
    )(ShitForm.apply)(ShitForm.unapply)
  }

  def index = Action.async {
    repo.list().map {
      shits => Ok(views.html.index(shits, shitForm))
    }
  }

  def addShit = Action.async { implicit request =>
    shitForm.bindFromRequest.fold(
      _ => Future.successful(Redirect(routes.ShitController.index)),
      shit => {
        repo.create(shit.name, shit.comment).map { _ =>
          Redirect(routes.ShitController.index)
        }
      }
    )
  }

  def editShit(id: Long) = Action.async { implicit request =>
    shitForm.bindFromRequest.fold(
      _ => Future.successful(Redirect(routes.ShitController.index)),
      shit => {
        repo.edit(id, Shit(id, shit.name, shit.comment)).map { result =>
          Ok(result.toString)
        }
      }
    )
  }

  def delShit(id: Long) = Action.async {
    repo.remove(id).map { result =>
      Ok(result.toString)
    }
  }

  def getShits = Action.async {
    repo.list().map { shit =>
      Ok(Json.toJson(shit))
    }
  }
}

case class ShitForm(name: String, comment: String)