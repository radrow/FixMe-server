package controllers

import forms.ReportForm
import javax.inject.{Inject, Singleton}
import models.{Pending, Tables, Tag}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._


@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

//	def db = Database.forURL("jdbc:postgresql://localhost:5432/FixMe-database", "fixme", "a")

	val db = Database.forConfig("mydb")

	def hello = Action { implicit request =>
		Ok ( if (System.currentTimeMillis() / 1000 % 2 == 0)
			"uciekać" else "jestok"
		)
	}

	def addreport = Action(parse.form(ReportForm.reportForm)) { implicit request =>
		val report_to_add = request.body
		println(report_to_add)

		try {
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
		} finally {
			db.close()
		}
		Ok("elo\n")
	}

	def getreports = Action { implicit request =>
		var query = request.queryString
		// TODO
		var name = query.get("name")
		if (query == null) {
			// error
		}
		Ok("aaa")
	}

}
