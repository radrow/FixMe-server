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
case class Unknown(value: String) extends Status
object Status {
    def fromString(s: String): Status = s match {
        case "pending" => Pending()
        case "accepted" => Accepted()
        case "fixed" => Fixed()
        case ss => Unknown(ss)
    }

    def toString(status: Status) = status match {
      case Pending() => "pending"
      case Accepted() => "accepted"
      case Fixed() => "fixed"
      case Unknown(s) => s
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
