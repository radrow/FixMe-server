package controllers

import forms.{ReportForm, TagForm}
import javax.inject.{Inject, Singleton}
import models.Tables.Tags
import models.{Tables, Tag}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import DBConnection.db
import Validator.validateAdmin
import play.api.libs.json.Json

@Singleton
class GitaraSiemaController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def siema = Action { implicit request =>
    Ok(views.html.siema())
  }

  def gitara = Action { implicit request =>
    System.out.println("Gitara Siema")
    InformAll()
    Redirect(routes.GitaraSiemaController.siema())
  }

  def InformAll(): Unit = {
    //Await.result(db.run(Tables.Clients.list), 100 seconds).foreach(println)
  }

  def addtag = Action(parse.form(TagForm.tagForm)) { implicit request =>
    validateAdmin(request) match {
      case Some(client) =>
        val tag_to_add = request.body
        Await.result(db.run(DBIO.seq(
          Tables.tags += Tag(0, tag_to_add.name)
        )), Duration.Inf)
        Ok("elo\n")
      case None => Forbidden("wypierdalaj")
    }
  }


}
