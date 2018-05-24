package models

import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global

object Tables {

	class Clients(tag: slick.lifted.Tag) extends Table[Client](tag, "client") {

		def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.Unique)

		def name = column[String]("name")

		def email = column[String]("email")

		def password = column[String]("password")

		override def * = (id, email, name, password) <> ((Client.apply _).tupled, Client.unapply)
	}

	object Clients {
		implicit val getClientsResult: AnyRef with GetResult[Client] =
			GetResult(r => Client(r.nextInt, r.nextString, r.nextString(), r.nextString()))

		def list: DBIO[Seq[Client]] = sql"SELECT * FROM client".as[Client]

		def getclient(u: String, p: String): DBIO[Option[Client]] =
			clients.filter(c => c.name === u && c.password === p).result.map(_.headOption)
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

	class UpVotes(tag: slick.lifted.Tag) extends Table[(Int, Int)](tag, "upvote") {
		def client = column[Int]("client_id")
		def report = column[Int]("report_id")
		def * = (client, report)
		def pk = primaryKey("upvote_pk", (client, report))
	}
	object UpVotes {
		implicit val getUpVotesResult: AnyRef with GetResult[(Int, Int)] =
			GetResult(r => (r.nextInt(), r.nextInt()))
	}
	val upvotes = TableQuery[UpVotes]

	class ReportTags(tag: slick.lifted.Tag) extends Table[(Int, Int)](tag, "report_tag") {
		def tag_id = column[Int]("tag_id")
		def report = column[Int]("report_id")
		def * = (tag_id, report)
		def pk = primaryKey("report_tag_pk", (tag_id, report))
	}
	object ReportTags {
		implicit val getReportTagsResult: AnyRef with GetResult[(Int, Int)] =
			GetResult(r => (r.nextInt(), r.nextInt()))
	}
	val reportTags = TableQuery[ReportTags]

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

		def listRows: DBIO[Seq[ReportRow]] = sql"SELECT * FROM report".as[ReportRow]

		def max_id: DBIO[Int] = {
			for {
				reportRows: Seq[ReportRow] <- listRows
			} yield reportRows.map(_._1).max
		}

		def all: DBIO[Seq[Report]] = {
			for {
				// get raw rows
				reportRows: Seq[ReportRow] <- listRows

				// combine DB IO actions into single results
				reports <- DBIO.sequence(reportRows.map(r => {
					for {
						// yield who likes my report
						upvoters <- (for {
							uv <- upvotes
							c <- clients if uv.report === r._1 && uv.client === c.id
						} yield c).result

						// yield used tags
						tags <- (for {
							rt <- reportTags
							t <- tags if rt.report === r._1 && rt.tag_id === t.id
						} yield t).result

						author <- clients.filter(_.id === r._4).result

						// return result in DBIO context
					} yield Report(r._1,
					               r._2,
					               r._3,
					               author.head,
					               r._5,
					               Status.fromString(r._6),
					               upvoters,
					               tags)
				}))
			} yield reports
		}
	}
	val reports = TableQuery[Reports]
}
