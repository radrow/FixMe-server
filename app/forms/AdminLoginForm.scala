package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class AdminLoginForm(login: String, password: String)

object AdminLoginForm {
  val adminLoginForm: Form[AdminLoginForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text,
    )(AdminLoginForm.apply)(AdminLoginForm.unapply)
  )
}