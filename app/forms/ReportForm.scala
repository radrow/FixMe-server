package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class ReportForm(title: String, description: String, location: String, tags: Seq[String])

object ReportForm {
  val reportForm: Form[ReportForm] = Form(
    mapping(
      "title" -> text,
      "description" -> text,
      "location" -> text,
      "tags" -> seq(text)
    )(ReportForm.apply)(ReportForm.unapply)
  )
}