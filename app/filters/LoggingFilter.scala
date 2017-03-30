package filters


import javax.inject.{Inject, Singleton}

import akka.util.ByteString
import play.Logger
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class LoggingFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter {
  def apply(nextFilter: EssentialAction) = new EssentialAction {
    def apply(requestHeader: RequestHeader) = {
      val startTime = System.currentTimeMillis
      val accumulator: Accumulator[ByteString, Result] = nextFilter(requestHeader)
      accumulator.map { result =>
        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime
        Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> requestTime.toString)
      }
    }
  }
}