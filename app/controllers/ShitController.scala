package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import javax.inject._

import dao.ShitDao


class ShitController @Inject()(repo: ShitDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val shitForm: Form[CreateShitForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "comment" -> nonEmptyText
    )(CreateShitForm.apply)(CreateShitForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(shitForm))
  }

  def addShit = Action.async { implicit request =>
    shitForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      shit => {
        repo.create(shit.name, shit.comment).map { _ =>
          Redirect(routes.ShitController.index)
        }
      }
    )
  }

  def getShits = Action.async {
    repo.list().map { shit =>
      Ok(Json.toJson(shit))
    }
  }
}

case class CreateShitForm(name: String, comment: String)