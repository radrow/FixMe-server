package models

case class Client(id: Int,
                  email: String,
                  name: String,
                  password: String
                 )

case class Tag(id: Int, name: String)

abstract class Status
case class Pending() extends Status
case class Accepted() extends Status
case class Fixed() extends Status
object Status {
	def fromString(s: String): Option[Status] = s match {
		case "pending" => Some(Pending())
		case "accepted" => Some(Accepted())
		case "fixed" => Some(Fixed())
		case _ => None
	}

	def fromStringUnsafe(s: String): String =
		fromString(s).getOrElse[String]("unknown")
}

case class Report(id: Int,
                  title: String,
                  description: String,
                  author: Client,
                  location: String,
                  status: Status,
                  upvotes: Seq[Client],
                  tags: Seq[Tag],
                 )
