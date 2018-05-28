package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class TagForm(login: String, password: String, name: String, email: String)

object TagForm {
  val tagForm: Form[TagForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text,
      "name" -> text,
      "email" -> email,
    )(TagForm.apply)(TagForm.unapply)
  )
}
