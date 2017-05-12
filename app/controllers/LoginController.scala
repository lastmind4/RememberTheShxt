package controllers

import javax.inject.Inject

import dao.LoginDao
import entity.Login
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.{ExecutionContext, Future}

class LoginController @Inject()(loginDao: LoginDao) extends Controller {

  val loginFormat: Form[CreateLoginForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateLoginForm.apply)(CreateLoginForm.unapply)
  }

  def login = Action {
    Redirect(routes.Assets.versioned("htmls/login.html"))
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