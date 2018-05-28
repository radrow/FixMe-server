package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class BanUserForm(login: String, password: String, email: String)

object BanUserForm {
  val banUserForm: Form[BanUserForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text,
      "email" -> text
    )(BanUserForm.apply)(BanUserForm.unapply)
  )
}