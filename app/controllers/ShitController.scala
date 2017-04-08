package controllers

import javax.inject._

import dao.ShitDao
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class ShitController @Inject()(repo: ShitDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val shitForm: Form[CreateShitForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "comment" -> nonEmptyText
    )(CreateShitForm.apply)(CreateShitForm.unapply)
  }

  def index = Action.async {
    repo.list().map {
      shits => Ok(views.html.index(shits, shitForm))
    }
  }

  def addShit = Action.async { implicit request =>
    shitForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Redirect(routes.ShitController.index))
      },
      shit => {
        repo.create(shit.name, shit.comment).map { _ =>
          Redirect(routes.ShitController.index)
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

case class CreateShitForm(name: String, comment: String)