package models

import slick.dbio.DBIOAction
import slick.jdbc.GetResult
import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContext

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

    val all = TableQuery[Clients]
  }

  class Tags(tag: slick.lifted.Tag) extends Table[Tag](tag, "tag") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.Unique)

    def name = column[String]("name", O.Unique)

    override def * = (id, name) <> (Tag.tupled, Tag.unapply)

  }

  object Tags {
    implicit val getTagsResult: AnyRef with GetResult[Tag] =
      GetResult(r => Tag(r.nextInt(), r.nextString()))

    val all = TableQuery[Tags]
  }

  class Statuses(tag: slick.lifted.Tag) extends Table[String](tag, "status") {
    def name = column[String]("name", O.PrimaryKey, O.Unique)

    override def * = name
  }

  object Statuses {
    implicit val getStatusesResult: AnyRef with GetResult[String] =
      GetResult(r => r.nextString())

    def all = TableQuery[Statuses]
  }

  class UpVotes(tag: slick.lifted.Tag) extends Table[(Int, Int)](tag, "upvote") {
    def client_id = column[Int]("client_id")

    def report_id = column[Int]("report_id")

    def * = (client_id, report_id)

    def pk = primaryKey("upvote_pk", (client_id, report_id))
  }

  object UpVotes {
    implicit val getUpVotesResult: AnyRef with GetResult[(Int, Int)] =
      GetResult(r => (r.nextInt(), r.nextInt()))
    val all = TableQuery[UpVotes]
  }

  class ReportTags(tag: slick.lifted.Tag) extends Table[(Int, Int)](tag, "report_tag") {
    def tag_id = column[Int]("tag_id")

    def report_id = column[Int]("report_id")

    def * = (tag_id, report_id)

    def pk = primaryKey("report_tag_pk", (tag_id, report_id))
  }

  object ReportTags {
    implicit val getReportTagsResult: AnyRef with GetResult[(Int, Int)] =
      GetResult(r => (r.nextInt(), r.nextInt()))
    val all = TableQuery[ReportTags]
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

    private val allRows = TableQuery[Reports]

    def all =

      for {
        r <- allRows
        tags: Seq[Tag] <- for {
          rt <- ReportTags.all if rt.report_id === r.id
          t <- Tags.all if rt.tag_id === t.id
        } yield t
        upvoters: Seq[Client] <- for {
          u <- UpVotes.all if u.report_id === r.id
          c <- Clients.all if u.client_id === c.id
        } yield c
        author: Client <- Clients.all.filter(_.id === r.clientid)
      } yield Report(r.id, r.title, r.description, author, r.location, Status.fromString(r.statusname), upvoters, tags)
  }
  class Coffees(tag: slick.lifted.Tag) extends Table[(String, Double)](tag, "COFFEES") {
    def name = column[String]("COF_NAME")
    def price = column[Double]("PRICE")
    def * = (name, price)
  }
  val coffees = TableQuery[Coffees]

  val reports = TableQuery[Reports]
}
