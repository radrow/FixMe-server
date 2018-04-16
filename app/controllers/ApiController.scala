package controllers

import javax.inject.{Inject, Singleton}
import models.Tables
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	def hello = Action { implicit request =>
		Ok ( if (System.currentTimeMillis() / 1000 % 2 == 0)
			"spierdalać" else "jestok"
		)
	}

}
