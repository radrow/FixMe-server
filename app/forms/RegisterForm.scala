package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class RegisterForm(name: String, email: String, password: String)

object RegisterForm {
  val registerForm: Form[RegisterForm] = Form(
    mapping(
      "name" -> text,
      "email" -> email,
      "password" -> text,
    )(RegisterForm.apply)(RegisterForm.unapply)
  )
}