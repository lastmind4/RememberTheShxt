package controllers

import javax.inject.Inject

import dao.LoginDao
import entity.Login
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

class LoginController @Inject()(loginDao: LoginDao, val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val loginFormat: Form[CreateLoginForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateLoginForm.apply)(CreateLoginForm.unapply)
  }

  def login = Action {
    Ok(views.html.login(loginFormat))
  }

  def doLogin = Action.async { implicit request =>
    loginFormat.bindFromRequest.fold(
      _ => Future.successful(Redirect(routes.LoginController.login)),
      form => {
        loginDao.login(form.name, form.password).flatMap {
          case Seq(Login(id, name, password)) => Future.successful(Redirect("/").withSession("user" -> name))
          case _ => Future.successful(Redirect(routes.LoginController.login))
        }
      })
  }

  def logout = Action {
    Redirect("/").withNewSession
  }
}

case class CreateLoginForm(name: String, password: String)