package controllers

import forms.{ActivateForm, RegisterForm, ReportForm}
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
import services.SenderTool
import Evacuation.isEvacuation

import scala.util.Random

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val random: Random.type = Random

  def evacuation = Action { implicit request =>
    isEvacuation match {
      case Some(id) => Ok(id.toString)
      case None => Ok("no evacuation\n")
    }
  }
  def sendmail = Action { implicit request =>
    SenderTool.sendEmail("XDDDDDD O KURWA SPOCK XDDDDDD ŚMIESZNE XDDDD\n", "thewiztory@gmail.com")
    Ok("XDDDD O PANIE XDDD")
  }

  def addReport = Action(parse.form(ReportForm.reportForm)) { implicit request =>
    validateUser(request) match {
      case Some(client) =>
        val report_to_add = request.body
        println(report_to_add)

        try {
          Await.result(db.run(DBIO.seq(
            Tables.reports += ((
              0,
              report_to_add.title,
              report_to_add.description,
              client.id,
              report_to_add.location,
              Pending().toString
            ))
          )), Duration.Inf)
          val report_id = Await.result(db.run(Tables.Reports.max_id), Duration.Inf)
          val all_tags = Await.result(db.run(Tables.Tags.list), Duration.Inf).map(t => t.name -> t.id).toMap
          report_to_add.tags.foreach(t =>
            Await.result(db.run(DBIO.seq(Tables.reportTags += (all_tags(t), report_id))), Duration.Inf)
          )
          Await
            .result(db.run(Tables.Tags.list), Duration.Inf)
            .filter(t => report_to_add.tags.contains(t.name))
            .foreach(t => SenderTool.sendEmail(report_to_add.toString, t.email))
          Ok("elo\n")
        } catch {
          case e: Exception => MethodNotAllowed("coś się pojebało\n")
        }
      case None => Forbidden("wypierdalaj\n")
    }
  }

  def getReports = Action { implicit request =>
    validateUser(request) match {
      case Some(client) =>
        val query = request.queryString
        val tagP: Seq[models.Tag] => Boolean = query.get("tag").map(s =>
          (rt: Seq[models.Tag]) => rt.map(_.name).intersect(s).nonEmpty).getOrElse((_: Seq[models.Tag]) => true)
        val locationP: String => Boolean = query.get("location").flatMap(_.headOption).map(a =>
          (b: String) => a == b).getOrElse(a => true)
        val reps = Await.result(db.run(Tables.Reports.all), Duration.Inf).filter(
          r => tagP(r.tags) && locationP(r.location)
        )
        Ok(Json.stringify(Json.toJson(reps)))
      case None => Forbidden("wypierdalaj\n")
    }
  }

  def getTags = Action { implicit request =>
    val tags = Await.result(db.run(Tables.Tags.list), Duration.Inf)
    Ok(Json.stringify(Json.toJson(
      tags.map(t => t.name)
    )))
  }

  def isValidUser = Action { implicit request =>
    validateUser(request) match {
      case Some(client) => Ok("valid\n")
      case None => Forbidden("not valid\n")
    }
  }

  def emailNotUnique(email: String): Boolean = {
    Await.result(db.run(Clients.list), Duration.Inf).exists(c => c.email.equals(email))
  }

  def registerUser = Action(parse.form(RegisterForm.registerForm)) { implicit request =>
    val register = request.body
    if (emailNotUnique(register.email)) {
      BadRequest("This email is already used\n")
    } else {
      val token = random.nextInt(10000)
      println(token)
      Await.result(db.run(DBIO.seq(
        Tables.clients += Client(
          0,
          register.email,
          register.name,
          register.password,
          false,
          false,
          token
        )
      )), Duration.Inf)
      SenderTool.sendEmail("Your token for FixMe app is: " ++ token.toString ++ ".\n", register.email)
      Ok("tmp: " ++ token.toString ++ "\n")
    }
  }

  def readyToActivate(form: ActivateForm): Boolean = {
    Await
      .result(db.run(Tables.Clients.list), Duration.Inf)
      .exists(c =>
        c.is_activated == false &&
        c.email.equals(form.email) &&
        c.password.equals(form.password) &&
        c.register_code == form.token
      )
  }

  def activateUser = Action(parse.form(ActivateForm.activateForm)) { implicit request =>
    val activate = request.body
    if (readyToActivate(activate)) {
      val query = for {
        client <- Tables.clients if client.email === activate.email &&
          client.password === activate.password &&
          client.register_code === activate.token &&
          client.is_activated === false
      } yield (client.is_activated, client.register_code)
      val updateAction = query.update(true, 0)
      Await.result(db.run(updateAction), Duration.Inf)
      Ok("elo\n")
    } else {
      BadRequest("coś nie pykło\n")
    }
  }

}
