package controllers

import forms.ReportForm
import javax.inject.{Inject, Singleton}
import models.Tables.Clients
import models.{Client, Pending, Tables}
import play.api.libs.json._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

import DBConnection.db
import Validator.validateUser

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def hello = Action { implicit request =>
    Ok(if (System.currentTimeMillis() / 1000 % 2 == 0)
      "uciekać" else "jestok"
    )
  }


  def addReport = Action(parse.form(ReportForm.reportForm)) { implicit request =>
    validateUser(request) match {
      case Some(client) =>
        val report_to_add = request.body
        println(report_to_add)

        Await.result(db.run(DBIO.seq(
          Tables.reports += ((
            0,
            report_to_add.title,
            report_to_add.description,
            report_to_add.client_id,
            report_to_add.location,
            Pending().toString
          ))
        )), Duration.Inf)
        Ok("elo\n")
      case None => Forbidden("wypierdalaj")
    }
  }

  def getReports = Action { implicit request =>
    validateUser(request) match {
      case Some(client) =>
        val query = request.queryString
        val tag = query.get("tag")
        val location = query.get("location")
        val reps = Await.result(db.run(Tables.Reports.all), Duration.Inf)
        Ok(Json.stringify(Json.toJson(reps)))
      case None => Forbidden("wypierdalaj")
    }
  }

  def getTags = Action { implicit request =>
    val tags = Await.result(db.run(Tables.Tags.list), Duration.Inf)
    Ok(Json.stringify(Json.toJson(
      tags.map(t => t.name)
    )))
  }

}
