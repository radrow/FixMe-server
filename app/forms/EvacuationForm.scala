package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class EvacuationForm(login: String, password: String)

object EvacuationForm {
  val evacuationForm: Form[EvacuationForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(EvacuationForm.apply)(EvacuationForm.unapply)
  )
}