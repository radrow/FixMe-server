package controllers

import forms.ReportForm
import javax.inject.{Inject, Singleton}
import models.Tables
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	def hello = Action { implicit request =>
		Ok ( if (System.currentTimeMillis() / 1000 % 2 == 0)
			"uciekać" else "jestok"
		)
	}

	def addreport = Action(parse.form(ReportForm.reportForm)) { implicit request =>
		val report = request.body
		println(report)

		Ok("elo\n")
	}

}
