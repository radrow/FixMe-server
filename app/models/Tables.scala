package models

import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._

object Tables {

  class Clients(tag : Tag) extends Table[Client](tag, "client") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[String]("name")

    override def * = (id, name) <> (Client.tupled, Client.unapply)

  }

  object Clients {
    implicit val getClientsResult: AnyRef with GetResult[Client] =
      GetResult(r => Client(r.nextInt, r.nextString))

    def list: DBIO[Seq[Client]] = sql"SELECT * FROM client".as[Client]
  }
}
