package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class ReportForm(client_id: Int, title: String, description: String, location: String, tags: Seq[Int])

object ReportForm {
  val reportForm: Form[ReportForm] = Form(
    mapping(
      "client_id" -> number,
      "title" -> text,
      "description" -> text,
      "location" -> text,
      "tags" -> seq(number)
    )(ReportForm.apply)(ReportForm.unapply)
  )
}