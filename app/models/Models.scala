package models

import models.Tables.ReportRow

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

    def toString(status: Status) = status match {
      case Pending() => "pending"
      case Accepted() => "accepted"
      case Fixed() => "fixed"
      case _ => ""
    }
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
