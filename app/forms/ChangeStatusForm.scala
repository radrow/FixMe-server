package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class ChangeStatusForm(login: String, password: String, report: Int, status: String)

object ChangeStatusForm {
  val changeStatusForm: Form[ChangeStatusForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text,
      "report" -> number,
      "status" -> text
    )(ChangeStatusForm.apply)(ChangeStatusForm.unapply)
  )
}