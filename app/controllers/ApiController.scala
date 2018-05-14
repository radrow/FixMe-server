package controllers

import forms.ReportForm
import javax.inject.{Inject, Singleton}
import models.{Pending, Tables}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	//	def db = Database.forURL("jdbc:postgresql://localhost:5432/FixMe-database", "fixme", "a")

	val db = Database.forConfig("mydb")

	def hello = Action { implicit request =>
		Ok(if (System.currentTimeMillis() / 1000 % 2 == 0)
			   "uciekać" else "jestok"
		)
	}

	def addreport = Action(parse.form(ReportForm.reportForm)) { implicit request =>
		val report_to_add = request.body
		println(report_to_add)

		Await.result(db.run(DBIO.seq(
			Tables.reports += ((
					0,
					report_to_add.title,
					report_to_add.description,
					report_to_add.client_id,
					report_to_add.location,
					models.Status.toString(Pending())
			))
		)), Duration.Inf)
		Ok("elo\n")
	}

	def getreports = Action { implicit request =>
		val query = request.queryString
		val tag = query.get("tag")
		val location = query.get("location")
		val reps = Await.result(db.run(Tables.Reports.all), Duration.Inf)
		Ok(Json.stringify(Json.toJson(
			reps.map(r =>
				         Json.obj(
					         "title" -> r.title,
					         "description" -> r.description,
					         "author" -> Json.obj(
						         "email" -> r.author.email,
						         "name" -> r.author.name
					         ),
					         "location" -> r.location,
					         "status" -> models.Status.toString(r.status),
					         "upvotes" -> Json.toJson(
						         r.upvotes.map(u => Json.obj(
							         "email" -> u.email,
							         "name" -> u.name
						         ))
					         ),
					         "tags" -> Json.toJson(
						         r.tags.map(t => t.name)))))))
	}

	def getTags = Action { implicit request =>
		val tags = Await.result(db.run(Tables.Tags.list), Duration.Inf)
		Ok(Json.stringify(Json.toJson(
			tags.map(t => t.name)
		)))
	}

}
