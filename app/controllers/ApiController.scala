package controllers

import forms.ReportForm
import javax.inject.{Inject, Singleton}
import models.{Tag, Tag => _, _}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._


@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	def db = Database.forURL("jdbc:postgresql://localhost:5432/FixMe-database", "fixme", "a")

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
				Tables.report += Report( // brak obsługi sam nie wiem czego xD
					0,
					report_to_add.title,
					report_to_add.description,
					author: Client, // skąd brać klienta, który to zgłoszenie robi, skoro nie mamy logowania
					report_to_add.location,
					Pending(),
					null,
					report_to_add.tags, // przydałoby się mapować Int -> Tag już po stronie bazy danych, żeby tutaj nie robić dodatkowego zapytania sql
				)
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
		Ok()
	}

}
