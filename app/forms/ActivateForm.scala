package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class ActivateForm(email: String, password: String, token:Int)

object ActivateForm {
  val activateForm: Form[ActivateForm] = Form(
    mapping(
      "email" -> email,
      "password" -> text,
      "token" -> number,
    )(ActivateForm.apply)(ActivateForm.unapply)
  )
}