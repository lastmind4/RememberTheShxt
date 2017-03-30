package filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import controllers.routes
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import scala.concurrent.Future

@Singleton
class SessionFilter @Inject()(implicit val mat: Materializer) extends Filter {
  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    if (!requestHeader.session.get("user").isDefined && !requestHeader.path.contains("/login") && !requestHeader.path.contains("/assets/")) {
      Future.successful(Results.Redirect(routes.LoginController.login()))
    } else {
      nextFilter(requestHeader)
    }
  }
}