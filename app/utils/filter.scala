import org.apache.pekko.stream.Materializer
import play.api.http.DefaultHttpFilters

import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class SessionAuthFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val maybeSessionKey=requestHeader.headers.get("sessionKey")
    maybeSessionKey match {
      case Some(sessionKey) =>
        if (sessionKey == "adminSessionKey123") {
          nextFilter(requestHeader)
        } else {
          Future.successful(Results.Unauthorized("Invalid admin session key"))
        }
      case None =>
        Future.successful(Results.Unauthorized("Session key missing"))
    }
  }

  // Example method to validate session key
  //  private def isValidSessionKey(sessionKey: String): Boolean = {
  //    // Your validation logic goes here
  //    // For example, you can check if the sessionKey exists in the database or matches some criteria
  //    // This is a placeholder method, replace it with your actual implementation
  //    sessionKey.nonEmpty && sessionKey == "validSessionKey"
  //  }
}

//class Filters @Inject()(sessionAuthFilter: SessionAuthFilter) extends
//  DefaultHttpFilters(sessionAuthFilter)
