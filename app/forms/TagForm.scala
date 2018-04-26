package forms

import models.Tag
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class TagForm(name: String)

object TagForm {
  val tagForm: Form[TagForm] = Form(
    mapping(
      "name" -> text,
    )(TagForm.apply)(TagForm.unapply)
  )
}