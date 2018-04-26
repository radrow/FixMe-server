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

@Singleton
class GitaraSiemaController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def db = Database.forURL("jdbc:postgresql://localhost:5432/FixMe-database", "adam", "a")

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
    val tag_to_add = request.body
    val tagDAO = TableQuery[Tags]
    try {
      Await.result(db.run(DBIO.seq(
        tagDAO += Tag(0, tag_to_add.name)
      )), Duration.Inf)
    }
    finally {
      db.close()
    }

    Ok("elo\n")
  }


}
