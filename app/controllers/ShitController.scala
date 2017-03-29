package controllers

import javax.inject.Inject

import dao.ShitDAO
import entity.Shit
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import views._

class ShitController @Inject()(shitDao: ShitDAO) extends Controller {

  def index = Action.async {
    shitDao.all().map { case (shits) => Ok(html.index(shits)) }
  }

  val shitForm = Form(
    mapping(
      "id" -> text(),
      "name" -> text(),
      "comment" -> text()
    )(Shit.apply)(Shit.unapply)
  )

  def insertShit = Action.async { implicit request =>
    val shit: Shit = shitForm.bindFromRequest.get
    shitDao.insert(shit).map(_ => Redirect(routes.ShitController.index))
  }
}