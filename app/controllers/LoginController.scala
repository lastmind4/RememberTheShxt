package controllers

import javax.inject.Inject

import dao.LoginDao
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

class LoginController @Inject()(repo: LoginDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val loginFormat: Form[CreateShitForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateShitForm.apply)(CreateShitForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(loginFormat))
  }

  def login = Action.async { implicit request =>
    loginFormat.bindFromRequest.fold(
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
}

case class CreateLoginForm(name: String, password: String)