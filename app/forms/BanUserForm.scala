package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class BanUserForm(email: String)

object BanUserForm {
  val banUserForm: Form[BanUserForm] = Form(
    mapping(
      "email" -> text
    )(BanUserForm.apply)(BanUserForm.unapply)
  )
}