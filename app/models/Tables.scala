package models

import slick.dbio.DBIOAction
import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._

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

	class Statuses(tag: slick.lifted.Tag) extends Table[String](tag, "status") {
		def name = column[String]("name", O.PrimaryKey, O.Unique)

		override def * = name

	}

	object Statuses {
		implicit val getStatusesResult: AnyRef with GetResult[String] =
			GetResult(r => r.nextString())

		def list: DBIO[Seq[String]] = sql"SELECT * FROM status".as[String]
	}

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

	}

	object Reports {
		implicit val getReportResult: AnyRef with GetResult[ReportRow] =
			GetResult(r => (r.nextInt(), r.nextString(), r.nextString(), r.nextInt(), r.nextString(), r.nextString()))


		def list: DBIO[Seq[Report]] = for {
			// get raw rows
			reportRows: Seq[ReportRow] <- sql"SELECT * FROM report".as[ReportRow]

			// combine DB IO actions into single results
			reports <- DBIO.sequence(reportRows.map(r => {
				Status.fromString(r._6) match { // check if I can parse Status

					// if failed yield error
					case None => DBIOAction.failed(new IllegalArgumentException("Unsupported status name: " ++ r._6))

					// if succeed return DBIO action...
					case Some(status) => for {

						// yield who likes my report
						upvoters <- sql"""SELECT client.*
							              FROM client JOIN upvote ON client.id = upvote.client_id
							              WHERE report_id = ${r._1}""".as[Client]

						// yield used tags
						tags <- sql"""SELECT DISTINCT tag.*
							          FROM tag JOIN report_tag ON tag.id = report_tag.tag_id
							          WHERE report_id = ${r._1}""".as[Tag]

						author <- sql"SELECT * FROM client WHERE id = ${r._4}".as[Client]

						// return result in DBIO context
					} yield Report(r._1,
					               r._2,
					               r._3,
					               author.headOption.getOrElse(Client.unknownId(r._4)),
					               r._5,
					               status,
					               upvoters,
					               tags)
				}
			}))

		} yield reports
	}
}
