package models

import play.api.libs.json.{Json, Writes}

case class Client(id: Int,
                  email: String,
                  name: String,
                  password: String
                 )

case object Client {
	implicit val clientWrites: Writes[Client] = (client: Client) => Json.obj(
		"id" -> client.id,
		"email" -> client.email,
		"name" -> client.name
	)
}

case class Tag(id: Int, name: String)

abstract class Status
case class Pending() extends Status {
	override val toString = "pending"
}
case class Accepted() extends Status {
	override val toString = "accepted"
}
case class Fixed() extends Status {
	override val toString = "fixed"
}
case class Unknown(value: String) extends Status {
	override val toString = value
}
object Status {
	def fromString(s: String): Status = s match {
		case "pending" => Pending()
		case "accepted" => Accepted()
		case "fixed" => Fixed()
		case ss => Unknown(ss)
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

object Report {
	implicit val reportWrites: Writes[Report] = (r: Report) =>
		Json.obj(
			"title" -> r.title,
			"description" -> r.description,
			"author" -> Json.toJson(r.author),
			"location" -> r.location,
			"status" -> r.status.toString,
			"upvotes" -> Json.toJson(r.upvotes),
			"tags" -> Json.toJson(
				r.tags.map(t => t.name)))
}