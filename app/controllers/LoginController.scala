package controllers

import javax.inject.Inject

import dao.LoginDao
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

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

  def login(name: String, password: String) = Action {
    Redirect("/").withSession("user" -> "Admin")
  }

  def logout = Action {
    Redirect("/").withNewSession
  }
}

case class CreateLoginForm(name: String, password: String)