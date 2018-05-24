package controllers

import slick.jdbc.JdbcBackend.Database

object DBConnection {

  val db = Database.forConfig("mydb")

}
