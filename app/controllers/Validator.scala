package controllers

import models.Client
import models.Tables.Clients
import play.api.mvc.{AnyContent, Request}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import DBConnection.db

object Validator {

  def validateUser(request: Request[AnyRef]): Option[Client] =
    for {
      username <- request.headers.get("Username")
      password <- request.headers.get("Password")
      c <- Await.result(db.run(Clients.getclient(username, password)), Duration.Inf)
    } yield c

  def validateAdmin(request: Request[AnyRef]): Option[Client] =
    for {
      username <- request.headers.get("Username")
      password <- request.headers.get("Password")
      c <- Await.result(db.run(Clients.getclient(username, password)), Duration.Inf).filter(c => c.is_admin)
    } yield c

}
