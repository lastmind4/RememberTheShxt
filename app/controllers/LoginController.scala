package controllers

import javax.inject.Inject

import dao.LoginDao
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

class LoginController @Inject()(repo: LoginDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val loginFormat: Form[CreateLoginForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateLoginForm.apply)(CreateLoginForm.unapply)
  }

  def index = Action {
    Ok(views.html.login(loginFormat))
  }

  def login = Action { implicit request =>
    val form : CreateLoginForm  = loginFormat.bindFromRequest.get
    Redirect("/").withSession("user" -> form.name)
  }

  def logout = Action {
    Redirect("/").withNewSession
  }
}

case class CreateLoginForm(name: String, password: String)