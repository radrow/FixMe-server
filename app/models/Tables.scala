package models

import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._

object Tables {

//  class Clients(tag : Tag) extends Table[Client](tag, "client") {
//
//    def id = column[Int]("id", O.PrimaryKey)
//
//    def name = column[String]("name")
//
//    override def * = (id, name) <> (Client.tupled, Client.unapply)
//
//  }
//
//  object Clients {
//    implicit val getClientsResult: AnyRef with GetResult[Client] =
//      GetResult(r => Client(r.nextInt, r.nextString))
//
//    def list: DBIO[Seq[Client]] = sql"SELECT * FROM client".as[Client]
//  }

  class Tags(tag: slick.lifted.Tag) extends Table[Tag](tag, "tag") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.Unique)
    def name = column[String]("name", O.Unique)
    override def * = (id, name) <> (Tag.tupled, Tag.unapply)

  }

  object Tags {
    implicit val getTagsResult: AnyRef with GetResult[Tag] =
      GetResult(r => Tag(r.nextInt(), r.nextString()))

    def list: DBIO[Seq[Tag]] = sql"SELECT * FROM tag".as[Tag]

//    def add(tagg: Tag): DBIO[Tag] = sql"INSERT INTO tag VALUES (tagg.id, tagg.name)"
  }
}
