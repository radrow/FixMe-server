package controllers

import javax.inject.{Inject, Singleton}
import models.Tables
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class GitaraSiemaController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val db = Database.forURL("jdbc:postgresql://localhost:5432/FixMe-database", "adam", "")

  def siema = Action { implicit request =>
    Ok(views.html.siema())
  }

  def gitara = Action { implicit request =>
    System.out.println("Gitara Siema")
    InformAll()
    Redirect(routes.GitaraSiemaController.siema())
  }

  def InformAll(): Unit = {
    Await.result(db.run(Tables.Clients.list), 100 seconds).foreach(println)
  }

}
