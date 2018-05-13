package models

import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._
import slick.lifted.PrimaryKey

object Tables {

	class Clients(tag: slick.lifted.Tag) extends Table[Client](tag, "client") {

		def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.Unique)

		def name = column[String]("name")

		def email = column[String]("email")

		def password = column[String]("password")

		override def * = (id, email, name, password) <> (Client.tupled, Client.unapply)
	}

	object Clients {
		implicit val getClientsResult: AnyRef with GetResult[Client] =
			GetResult(r => Client(r.nextInt, r.nextString, r.nextString(), r.nextString()))

		def list: DBIO[Seq[Client]] = sql"SELECT * FROM client".as[Client]
	}
	val clients = TableQuery[Clients]

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
	val tags = TableQuery[Tags]

	class Statuses(tag: slick.lifted.Tag) extends Table[String](tag, "status") {
		def name = column[String]("name", O.PrimaryKey, O.Unique)

		override def * = name
	}

	object Statuses {
		implicit val getStatusesResult: AnyRef with GetResult[String] =
			GetResult(r => r.nextString())

		def list: DBIO[Seq[String]] = sql"SELECT * FROM status".as[String]
	}
	val statuses = TableQuery[Statuses]

	class Upvotes[tag: slick.lifted.Tag] extends Table[(Int, Int)](tag, "upvote") {
		def client = column[Int]("client_id")
		def report = column[Int]("report_id")

		def * = (client, report)

		def pk = primaryKey("pk", (client, report))
	}
	val upvotes = TableQuery[Upvotes]


	type ReportRow = (Int, String, String, Int, String, String)
	class Reports(tag: slick.lifted.Tag) extends Table[ReportRow](tag, "report") {
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.Unique)

		def description = column[String]("description")

		def title = column[String]("title")

		def clientid = column[Int]("client_id")

		def location = column[String]("location")

		def statusname = column[String]("status_name")

		override def * =
			(id, title, description, clientid, location, statusname)

		def client = foreignKey("clientFK", clientid, clients)(
			_.id,
			onUpdate = ForeignKeyAction.Restrict,
			onDelete = ForeignKeyAction.Cascade)
	}

	object Reports {
		implicit val getReportResult: AnyRef with GetResult[ReportRow] =
			GetResult(r => (r.nextInt(), r.nextString(), r.nextString(), r.nextInt(), r.nextString(), r.nextString()))

		val s = clients.result.

		def list: DBIO[Seq[Report]] = for {

		}
	}
	val reports = TableQuery[ReportRow]
}
