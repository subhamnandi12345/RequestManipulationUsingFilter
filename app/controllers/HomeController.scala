package controllers
import javax.inject._
import play.api._
import play.api.mvc._

import java.nio.file.{Files, Path, Paths}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def myAction() = Action(parse.multipartFormData) { request =>

    val uploadDirectory = Paths.get("C:\\Users\\GS-3932\\Desktop\\scalaPlay\\uploadfiles\\app\\controllers")
    Files.createDirectories(uploadDirectory)

    val uploadedFiles: Seq[Path] = request.body.files.map { filePart =>
      val filename = filePart.filename
      val filePath = uploadDirectory.resolve(filename)
      filePart.ref.moveTo(filePath, replace = true)
      filePath
    }
    if (uploadedFiles.nonEmpty) {
      Ok(s"${uploadedFiles.size} files uploaded successfully.")
    } else {
      BadRequest("No files found in the request.")
    }
  }
}
//val files = request.body.files
//    if (files.nonEmpty) {
//      files.foreach { filePart =>
//        val filename = filePart.filename
//        println(s"Received file: $filename")
//      }
//      Ok("All files uploaded successfully")
//    } else {
//      BadRequest("No files found in request")
//    }