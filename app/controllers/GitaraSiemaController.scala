package controllers

import controllers.DBConnection.db
import controllers.Validator.validateAdmin
import forms._
import javax.inject.{Inject, Singleton}
import models.{Tables, Tag}
import play.api.mvc.{AbstractController, ControllerComponents, Cookie}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class GitaraSiemaController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	def addtag = Action(parse.form(TagForm.tagForm)) { implicit request =>
		validateAdmin(request) match {
			case Some(client) =>
				val tag_to_add = request.body
				try {
					Await.result(db.run(DBIO.seq(
						Tables.tags += Tag(0, tag_to_add.name, tag_to_add.email)
					)), Duration.Inf)

					Redirect("/web/tags")
				} catch {
					case e: Exception => MethodNotAllowed("coś się pojebało\n")
				}

			case None => Forbidden("wypierdalaj\n")
		}
	}

	def evacuationToggle = Action { implicit request =>
		validateAdmin(request) match {
			case Some(client) =>
				Evacuation.isNow = !Evacuation.isNow
				Evacuation.id += 1
				Redirect("/web")
			case None => Forbidden("wypierdalaj\n")
		}
	}

	def banUser = Action(parse.form(BanUserForm.banUserForm)) { implicit request =>
		validateAdmin(request) match {
			case Some(client) =>
				val banUser = request.body
				val query = for {
					client <- Tables.clients if client.email === banUser.email && !client.is_admin
				} yield client.register_code
				val updateAction = query.update(10000)
				Await.result(db.run(updateAction), Duration.Inf)
				Redirect("/web/clients")
			case None => Forbidden("wypierdalaj\n")
		}
	}

	def changeReportStatus = Action(parse.form(ChangeStatusForm.changeStatusForm)) { implicit request =>
		validateAdmin(request) match {
			case Some(client) =>
				val changeStatus = request.body
				val query = for {
					report <- Tables.reports if report.id === changeStatus.report
				} yield report.statusname
				val updateAction = query.update(changeStatus.status)
				Await.result(db.run(updateAction), Duration.Inf)

				Redirect("/web/reports")
			case None => Forbidden("wypierdalaj\n")
		}
	}

	def adminLogin = Action(parse.form(AdminLoginForm.adminLoginForm)) { implicit request =>
		validateAdmin(request.body.login, request.body.password) match {
			case Some(client) =>
				Redirect("/web").withCookies(Cookie("login", request.body.login), Cookie("password", request.body
				                                                                                     .password))
			case None => Forbidden("Złe hasło/login :)")
		}
	}

	def reports = Action { implicit request =>
		val reps = Await.result(db.run(Tables.Reports.all), Duration.Inf)
		Ok(views.html.reports(reps))
	}

	def clients = Action { implicit request =>
		val clients = Await.result(db.run(Tables.Clients.list), Duration.Inf)
		Ok(views.html.clients(clients))
	}

	def tags = Action { implicit request =>
		val tags = Await.result(db.run(Tables.Tags.list), Duration.Inf)
		Ok(views.html.tagedit(tags))
	}

	def web = Action { implicit request =>
		Ok(views.html.admin(validateAdmin(request).isDefined)(Evacuation.isNow))
	}

}
